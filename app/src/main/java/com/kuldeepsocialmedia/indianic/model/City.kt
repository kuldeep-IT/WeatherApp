package com.kuldeepsocialmedia.indianic.model

data class City(
    val weather:ArrayList<Weather>,  // val weather:Weather
    val main:Main,
    val wind:Wind,
    val name:String
) {
}