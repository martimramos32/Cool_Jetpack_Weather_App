package com.example.cooljetpackweatherapp.ui

import android.content.res.Configuration
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import dam_A51736.coolweatherapp.WMO_WeatherCode
import dam_A51736.coolweatherapp.getWeatherCodeMap
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlin.text.get

@Composable
fun WeatherUI(weatherViewModel: WeatherViewModel = viewModel()) {
    val weatherUIState by weatherViewModel.uiState.collectAsState()

    val latitude = weatherUIState.latitude
    val longitude = weatherUIState.longitude
    val temperature = weatherUIState.temperature
    val windSpeed = weatherUIState.windspeed
    val windDirection = weatherUIState.winddirection
    val weathercode = weatherUIState.weathercode
    val seaLevelPressure = weatherUIState.seaLevelPressure
    val time = weatherUIState.time

    val configuration = LocalConfiguration.current
    val day = true // Must change this in the future

    val mapt = getWeatherCodeMap()
    val wCode = mapt[weathercode]
    val wImage = when (wCode) {
        WMO_WeatherCode.CLEAR_SKY,
        WMO_WeatherCode.MAINLY_CLEAR,
        WMO_WeatherCode.PARTLY_CLOUDY -> {
            if (day){
                wCode.image // Se for de dia, mantém a imagem do código, por exemplo "clear_day"
            } else{
                wCode.image.replace("_day", "_night") //Se for de noite, substitui "_day" por "_night", por exemplo "clear_day" -> "clear_night"
            }
        }
        else -> wCode?.image ?: "cloudy"
    }

    val context = LocalContext.current
    val wIcon = context.resources.getIdentifier(wImage, "drawable", context.packageName)

    if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        LandscapeWeatherUI(
            wIcon = wIcon,
            latitude = latitude,
            longitude = longitude,
            temperature = temperature,
            windSpeed = windSpeed,
            windDirection = windDirection,
            weathercode = weathercode,
            seaLevelPressure = seaLevelPressure,
            time = time,
            onLatitudeChange = { newValue ->
                newValue.toFloatOrNull()?.let { weatherViewModel.updateLatitude(it) }
            },
            onLongitudeChange = { newValue ->
                newValue.toFloatOrNull()?.let { weatherViewModel.updateLongitude(it) }
            },
            onUpdateButtonClick = { weatherViewModel.fetchWeather() }
        )
    } else {
        PortraitWeatherUI(
            wIcon = wIcon,
            latitude = latitude,
            longitude = longitude,
            temperature = temperature,
            windSpeed = windSpeed,
            windDirection = windDirection,
            weathercode = weathercode,
            seaLevelPressure = seaLevelPressure,
            time = time,
            onLatitudeChange = { newValue ->
                newValue.toFloatOrNull()?.let { weatherViewModel.updateLatitude(it) }
            },
            onLongitudeChange = { newValue ->
                newValue.toFloatOrNull()?.let { weatherViewModel.updateLongitude(it) }
            },
            onUpdateButtonClick = { weatherViewModel.fetchWeather() }
        )
    }
}

@Composable
fun PortraitWeatherUI(
    wIcon: Int, latitude: Float, longitude: Float, temperature: Float,
    windSpeed: Float, windDirection: Int, weathercode: Int,
    seaLevelPressure: Float, time: String,
    onLatitudeChange: (String) -> Unit, onLongitudeChange: (String) -> Unit,
    onUpdateButtonClick: () -> Unit
) {
    // ToDo: Desenhar a interface em modo retrato
}

@Composable
fun LandscapeWeatherUI(
    wIcon: Int, latitude: Float, longitude: Float, temperature: Float,
    windSpeed: Float, windDirection: Int, weathercode: Int,
    seaLevelPressure: Float, time: String,
    onLatitudeChange: (String) -> Unit, onLongitudeChange: (String) -> Unit,
    onUpdateButtonClick: () -> Unit
) {
    // ToDo: Desenhar a interface em modo paisagem
}