package com.example.zagrajmywculko

import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.zagrajmywculko.databinding.FragmentSecondBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.Source


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
        var list: List<String> = listOf()
        readData(object: MyCallback {
            override fun onCallback(value: List<String>) {
                Log.d("TAG", list.toString())
            }
        })


        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

fun readData(myCallback: MyCallback) {
    val db = FirebaseFirestore.getInstance()
    val docRef = db.collection("test")
    docRef.get().addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val list = ArrayList<String>()
            for (document in task.result) {
                val name = document.data["test"].toString()
                list.add(name)
            }
            myCallback.onCallback(list)
        }
    }
}

fun addData(name:String){
    val db = FirebaseFirestore.getInstance()
    val user: MutableMap<String,Any> = HashMap()
    user["name"]=name
    val docRef = db.collection("test")
    docRef.add(user).addOnSuccessListener {
        Toast.makeText(this,"", Toast.LENGTH_SHORT).show()
    }.addOnFailureListener{

    }
}