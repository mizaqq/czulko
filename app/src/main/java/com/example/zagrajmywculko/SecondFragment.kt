package com.example.zagrajmywculko

import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.PorterDuff
import android.hardware.Sensor
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import com.example.zagrajmywculko.databinding.ContentMainBinding
import com.example.zagrajmywculko.databinding.FragmentSecondBinding
import com.example.zagrajmywculko.models.*
import com.google.android.gms.tasks.Tasks.await
import com.google.common.base.Functions.compose
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*
import kotlin.concurrent.timer
import kotlin.random.Random
import kotlin.system.measureTimeMillis


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
const val gameTime: Long = 90
class SecondFragment : Fragment() {

    //lateinit var viewModel: MainViewModel
    val viewModel: MainViewModel by activityViewModels()
    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        //viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding.root.background.setColorFilter(Color.parseColor("#0000ff"), PorterDuff.Mode.DARKEN)
        viewModel.currentState.observe(viewLifecycleOwner,Observer{
            if(it==1){
                lifecycleScope.launch{
                    binding.root.background.setColorFilter(Color.parseColor("#00ff00"), PorterDuff.Mode.DARKEN)
                    delay(500)
                    viewModel.currentVal.value= ++viewModel.points
                    viewModel.cards.add(binding.textviewSecond.text.toString())
                    viewModel.cardsCorrect.add(true)
                    delay(3000)
                    viewModel.currentState.value=0
                    readData(binding)
                    binding.root.background.setColorFilter(Color.parseColor("#0000ff"), PorterDuff.Mode.DARKEN)
                }

            }
            else if(it == 2){
                lifecycleScope.launch{
                    binding.root.background.setColorFilter(Color.parseColor("#ff0000"), PorterDuff.Mode.DARKEN)
                    delay(500)
                    viewModel.cards.add(binding.textviewSecond.text.toString())
                    viewModel.cardsCorrect.add(false)
                    delay(3000)
                    readData(binding)
                    binding.root.background.setColorFilter(Color.parseColor("#0000ff"), PorterDuff.Mode.DARKEN)
                    viewModel.currentState.value=0
                }
            }
        })
        viewModel.currentVal.observe(viewLifecycleOwner,Observer{
            binding.textviewPoints.text=it.toString()
        })
        viewModel.currentTime.observe(viewLifecycleOwner, Observer {
            binding.textviewTimer.text=it.toString()
        })
        viewModel.currentTimerState.observe(viewLifecycleOwner, Observer {
            if(it==false && viewModel.isTimer){
                gameFinished()
            }
        })

        changeColors(binding,requireContext())
        readData(binding)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val timer = StartTimer()
        timer.start()


        binding.buttonSecond.setOnClickListener {
            timer.cancel()
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
            requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            getActivity()?.setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun changeColors(binding: FragmentSecondBinding,context: Context){
        val gyroscopeSensor = GyroscopeSensor(context)
        gyroscopeSensor.startListening()
        gyroscopeSensor.setOnSensorValuesChangedListener { values ->
            val position = values[1]
            if (position>3.5f){
                if(viewModel.currentState.value!=1 && viewModel.currentState.value!=2) {
                    viewModel.currentState.value = 2
                }
            }
            if (position<-3.5F){
                if(viewModel.currentState.value!=1 && viewModel.currentState.value!=2) {
                    viewModel.currentState.value = 1
                }
            }

        }
    }

    fun StartTimer(): CountDownTimer {
        val timer= object : CountDownTimer(1000 * (gameTime+1),1000){
            override fun onTick(millisUntilFinished: Long) {
                viewModel.currentTimerState.value=true
                viewModel.isTimer=true
                var diff = millisUntilFinished
                val sec:Long=1000
                val min:Long=sec*60
                val minLeft = diff/min
                diff%=min
                val secLeft= diff/sec
                if(minLeft>0 && secLeft>9){
                    viewModel.currentTime.value= "0$minLeft:$secLeft"
                }
                else if(minLeft>0){
                    viewModel.currentTime.value= "0$minLeft:0$secLeft"
                }
                else if(secLeft>9){
                    viewModel.currentTime.value= "00:$secLeft"
                }
                else{
                    viewModel.currentTime.value= "00:0$secLeft"
                }
                if(secLeft<1 && minLeft<1){
                    lifecycleScope.launch() {
                        delay(1200)
                        viewModel.currentTimerState.value = false
                    }
                }

            }
            override fun onFinish() {
                lifecycleScope.launch() {
                    delay(800)
                    viewModel.isTimer = false
                }
            }
        }
        return timer
    }

    private fun gameFinished() {
        findNavController().navigate(R.id.action_SecondFragment_to_fragmentFinish)
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        getActivity()?.setRequestedOrientation(
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        )
    }
}


suspend fun newData(binding: FragmentSecondBinding){
    delay(1000)
    readData(binding)
    binding.root.background.setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.DARKEN)
}



fun readData(binding: FragmentSecondBinding) {
    val db = FirebaseFirestore.getInstance()
    val docRef = db.collection("esnerzy").document("esnerzy")
    var lista= mutableListOf<Card>()
    docRef.get().addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val document = task.result
            document.data?.let { data ->
                data.forEach { (key, x) ->
                    lista.add(Card(key.toInt(), x.toString()))
                }
            }
        }
        val i = Random.nextInt(0, 5)
        Log.d(TAG, "Cached document data: ${lista[i]}")
        binding.textviewSecond.text=lista[i].title
    }
}

