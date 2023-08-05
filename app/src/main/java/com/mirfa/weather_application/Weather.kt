package com.mirfa.weather_application

data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)