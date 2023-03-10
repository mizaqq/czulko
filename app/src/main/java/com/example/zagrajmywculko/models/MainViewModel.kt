package com.example.zagrajmywculko.models


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


class MainViewModel:ViewModel(){
    var points = 0
    var isTimer=false
    var cards = mutableListOf<String>()
    var cardsCorrect = mutableListOf<Boolean>()
    val currentState: MutableLiveData<Int> by lazy{
        MutableLiveData<Int>()
    }
    val currentVal: MutableLiveData<Int> by lazy{
        MutableLiveData<Int>()
    }
    val currentTime: MutableLiveData<String> by lazy{
        MutableLiveData<String>()
    }
    val currentTimerState: MutableLiveData<Boolean> by lazy{
        MutableLiveData<Boolean>()
    }
}