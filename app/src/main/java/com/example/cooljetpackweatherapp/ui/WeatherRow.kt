package com.example.cooljetpackweatherapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cooljetpackweatherapp.R

@Composable
fun WeatherDetails(windSpeed: Float, windDirection: Int, seaLevelPressure: Float) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            // Chamamos a linha individual dos dados recebidos
            WeatherRowItem(label = stringResource(R.string.wind_label), value = "$windSpeed km/h")
            WeatherRowItem(label = stringResource(R.string.direction_label), value = "$windDirection º")
            WeatherRowItem(label = stringResource(R.string.pressure_label), value = "$seaLevelPressure hPa")
        }
    }
}

// A estrutura de cada linha individual apresentada no cartão dos campos recebidos da API
@Composable
private fun WeatherRowItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
        Text(text = value, fontSize = 18.sp)
    }
}