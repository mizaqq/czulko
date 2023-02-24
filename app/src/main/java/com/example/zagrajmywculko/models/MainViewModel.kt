package com.example.zagrajmywculko.models


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


class MainViewModel():ViewModel(){
    var points = 0
    val currentState: MutableLiveData<Int> by lazy{
        MutableLiveData<Int>()
    }
    val currentVal: MutableLiveData<Int> by lazy{
        MutableLiveData<Int>()
    }
}