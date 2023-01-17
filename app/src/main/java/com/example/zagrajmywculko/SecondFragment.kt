package com.example.zagrajmywculko

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Context.SENSOR_SERVICE
import android.content.pm.ActivityInfo
import android.hardware.Sensor
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.zagrajmywculko.databinding.FragmentSecondBinding
import com.example.zagrajmywculko.models.Card
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.Source
import kotlinx.coroutines.awaitAll
import kotlin.coroutines.*
import kotlin.random.Random
import kotlin.system.*


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

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


        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        readData(binding)




        binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
            requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            getActivity()?.setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
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
        Log.d(ContentValues.TAG, "Cached document data: ${lista[i]}")
        binding.textviewSecond.text=lista[i].title
    }
}
