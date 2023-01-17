package com.example.zagrajmywculko

import com.example.zagrajmywculko.models.Card

interface MyCallback {
    fun onCallback(value: List<Card>)
}