package com.example.cooljetpackweatherapp.ui

data class WeatherUIState(
    val latitude: Float = 38.7223f, // Coordenadas iniciais (ex: Lisboa)
    val longitude: Float = -9.1393f,
    val temperature: Float = 0.0f,
    val seaLevelPressure: Float = 0.0f,
    val winddirection: Int = 0,
    val windspeed: Float = 0.0f,
    val weathercode: Int = 0,
    val time: String = "--:--",
)
