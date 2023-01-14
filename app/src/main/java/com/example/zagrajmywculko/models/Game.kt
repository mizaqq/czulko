package com.example.zagrajmywculko.models


import kotlin.random.Random



class Game {
    val points = 0
    val names: List<Card>
    var inputNames: List<String> = listOf("","")
        set(value){
        field= value
    }

    init {
        var chosenId:List<Int> = listOf(0)
        for(i in 1..10) {
            chosenId += Random.nextInt(0, 10)
        }
        names = makeList(chosenId,inputNames)
    }
}

fun makeList(chosenId: List<Int>,inputNames: List<String>): MutableList<Card> {
    val lista= mutableListOf<Card>()
    for(i in chosenId){
        lista+=(Card(i,inputNames[i]))
    }
    return lista
}