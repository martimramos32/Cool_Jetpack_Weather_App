package com.example.cooljetpackweatherapp.ui

//Classe responsável por guardar os dados da UI
data class WeatherUIState(
    // Dados iniciais hardcoded para Lisboa para quando a aplicação inicia pela primeira vez
    val latitude: String = "38.76",
    val longitude: String = "-9.12",
    val temperature: Float = 15.9f,
    val seaLevelPressure: Float = 1026.4f,
    val winddirection: Int = 214,
    val windspeed: Float = 13.0f,
    val weathercode: Int = 0,
    val time: String = "--:--",
    val isDay: Boolean = true //Esta variável vai mudar dinamicamente consoante a hora atual na localização pesquisada
)
