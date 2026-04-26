package com.example.cooljetpackweatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cooljetpackweatherapp.ui.WeatherUI
import com.example.cooljetpackweatherapp.ui.theme.CoolJetpackWeatherAppTheme
import com.example.cooljetpackweatherapp.viewmodel.WeatherViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            //Criamos o cérebro da aplicação
            val weatherViewModel: WeatherViewModel = viewModel()

            //Lemos o estado para saber se é de dia ou de noite
            val uiState by weatherViewModel.uiState.collectAsState()

            //Chamamos o Tema e ativamos o modo escuro (darkTheme) se não for de dia
            CoolJetpackWeatherAppTheme(darkTheme = !uiState.isDay) {
                //O Surface pinta todo o ecrã com a cor base do tema atual
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //Desenhamos o ecrã principal aqui dentro
                    WeatherUI(weatherViewModel = weatherViewModel)
                }
            }
        }
    }
}