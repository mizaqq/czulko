package com.example.zagrajmywculko.models


import android.app.Application
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.TextView
import com.example.zagrajmywculko.MyCallback
import com.example.zagrajmywculko.R
import com.example.zagrajmywculko.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.random.Random
import android.view.View
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.zagrajmywculko.MainActivity
import com.example.zagrajmywculko.databinding.ContentMainBinding
import java.lang.ref.WeakReference


class Game (
    private val gyroscopeSensor: MeasurableSensor
        ){
    val points = 0
    var listener = WeakReference<ChangeColorListener>(null)



    fun addListener(listener: ChangeColorListener){
        this.listener = WeakReference(listener)
    }
}

