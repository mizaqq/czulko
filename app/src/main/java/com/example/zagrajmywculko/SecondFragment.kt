package com.example.zagrajmywculko

import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.PorterDuff
import android.hardware.Sensor
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import com.example.zagrajmywculko.databinding.ContentMainBinding
import com.example.zagrajmywculko.databinding.FragmentSecondBinding
import com.example.zagrajmywculko.models.*
import com.google.android.gms.tasks.Tasks.await
import com.google.common.base.Functions.compose
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*
import kotlin.random.Random
import kotlin.system.measureTimeMillis


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    lateinit var viewModel: MainViewModel
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
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        viewModel.currentState.observe(viewLifecycleOwner,Observer{
            if(it==1){
                Handler().post {
                    binding.root.background.setColorFilter(Color.parseColor("#00ff00"), PorterDuff.Mode.DARKEN)
                }
                Handler().postDelayed({
                    viewModel.currentState.value=0
                    viewModel.currentVal.value= ++viewModel.points
                    readData(binding)
                    binding.root.background.setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.DARKEN)
                }, 3000)

            }
            else if(it == 2){
                Handler().post {
                    binding.root.background.setColorFilter(Color.parseColor("#FF0000"), PorterDuff.Mode.DARKEN)
                }
                Handler().postDelayed({
                    viewModel.currentState.value=0
                    readData(binding)
                    //binding.root.background.setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.DARKEN)
                }, 3000)
            }
        })
        viewModel.currentVal.observe(viewLifecycleOwner,Observer{
            binding.textviewThird.text=it.toString()
        })
        changeColors(binding,requireContext())
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        readData(binding)

        binding.buttonSecond.setOnClickListener {
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
                    viewModel.currentState.value = 1
                }
            }
            if (position<-3.5F){
                if(viewModel.currentState.value!=1 && viewModel.currentState.value!=2) {
                    viewModel.currentState.value = 2
                }
            }

        }
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
/*
fun changeColor(binding: FragmentSecondBinding,context: Context){
    val gyroscopeSensor = GyroscopeSensor(context)
    gyroscopeSensor.startListening()
    runBlocking {
    val job =  async{
        gyroscopeSensor.setOnSensorValuesChangedListener { values ->
            val position = values[1]
            if(position > 0.5f && !isCorrect){
                isCorrect =true
                Log.i(TAG,"Values5 $isCorrect")
                gyroscopeSensor.stopListening()
            }
        }
        delay(10000)
        Log.i(TAG,"ValuesW2 $isCorrect")
    }.await()
    runBlocking {
        Log.i(TAG,"Values0 $isCorrect")
        if(isCorrect){
            Log.i(TAG,"Values6 $isCorrect")
            binding.root.background.setColorFilter(Color.parseColor("#00ff00"), PorterDuff.Mode.DARKEN)
        }
    }
    }
}
*/



