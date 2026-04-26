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

//Esta classe tem como função atualizar o estado da ui consoante os dados recebidos da API
class WeatherViewModel : ViewModel() {

    // O estado interno (escondido e modificável)
    private val _uiState = MutableStateFlow(WeatherUIState())
    // O estado público (apenas de leitura para o ecrã)
    val uiState: StateFlow<WeatherUIState> = _uiState.asStateFlow()

    fun updateLatitude(newLat: String) {
        _uiState.update { it.copy(latitude = newLat) }
    }

    fun updateLongitude(newLon: String) {
        _uiState.update { it.copy(longitude = newLon) }
    }

    fun fetchWeather() {
        viewModelScope.launch {
            //Antes de aceder à internet é necessário converter as strings  da latitude e longitude recebidas em float para haver um processamento de dados correto
            val latFloat = _uiState.value.latitude.toFloatOrNull() ?: 0.0f
            val lonFloat = _uiState.value.longitude.toFloatOrNull() ?: 0.0f

            // Vai à internet buscar os dados usando as coordenadas atuais
            val data = WeatherApiClient.getWeather(latFloat, lonFloat)

            if (data != null) {
                _uiState.update { currentState ->
                    currentState.copy(
                        temperature = data.current_weather.temperature,
                        windspeed = data.current_weather.windspeed,
                        winddirection = data.current_weather.winddirection,
                        weathercode = data.current_weather.weathercode,
                        time = data.current_weather.time,
                        seaLevelPressure = data.hourly.pressure_msl.firstOrNull()?.toFloat() ?: 0.0f
                    )
                }
            }
        }
    }
}