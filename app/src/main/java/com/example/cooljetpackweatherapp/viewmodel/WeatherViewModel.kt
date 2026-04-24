package com.example.cooljetpackweatherapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cooljetpackweatherapp.data.WeatherApiClient
import com.example.cooljetpackweatherapp.ui.WeatherUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    // O estado interno (escondido e modificável)
    private val _uiState = MutableStateFlow(WeatherUIState())
    // O estado público (apenas de leitura para o ecrã)
    val uiState: StateFlow<WeatherUIState> = _uiState.asStateFlow()

    fun updateLatitude(newLat: Float) {
        _uiState.update { it.copy(latitude = newLat) }
    }

    fun updateLongitude(newLon: Float) {
        _uiState.update { it.copy(longitude = newLon) }
    }

    fun fetchWeather() {
        viewModelScope.launch {
            // Vai à internet buscar os dados usando as coordenadas atuais
            val data = WeatherApiClient.getWeather(_uiState.value.latitude, _uiState.value.longitude)

            if (data != null) {
                _uiState.update { currentState ->
                    currentState.copy(
                        temperature = data.current_weather.temperature,
                        windspeed = data.current_weather.windspeed,
                        winddirection = data.current_weather.winddirection,
                        weathercode = data.current_weather.weathercode,
                        time = data.current_weather.time,
                        // A pressão atmosférica vem da lista hourly. Vamos buscar a primeira hora.
                        seaLevelPressure = data.hourly.pressure_msl.firstOrNull()?.toFloat() ?: 0.0f
                    )
                }
            }
        }
    }
}