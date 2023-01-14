package com.example.zagrajmywculko.models

import com.google.firebase.storage.StorageReference
import jxl.Workbook
import kotlin.random.Random

val storageRef = storage.reference

class Game {
    val points = 0
    val names: List<Card>
    var inputNames: List<String>=

    init {
        val chosenId = for(i in 1..10){ (Random.nextInt(0,10)}
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
fun getItems(){
    Workbook.getWorkbook(file)
}