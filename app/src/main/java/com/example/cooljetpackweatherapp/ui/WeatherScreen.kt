package com.example.cooljetpackweatherapp.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cooljetpackweatherapp.viewmodel.WeatherViewModel
import dam_A51736.coolweatherapp.WMO_WeatherCode
import dam_A51736.coolweatherapp.getWeatherCodeMap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.app.Activity
import android.content.Intent


//Função responsável por apresentar o layout principal da aplicação
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

    val context = LocalContext.current // Permite aceder ao contexto da aplicação

    // O launcher que abre a janela e espera pela resposta do utilizador
    val mapLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val lat = result.data?.getStringExtra("latitude")
            val lon = result.data?.getStringExtra("longitude")

            if (lat != null && lon != null) {
                // Atualiza as caixas de texto na atividade principal
                weatherViewModel.updateLatitude(lat)
                weatherViewModel.updateLongitude(lon)
            }
        }
    }

    val configuration = LocalConfiguration.current // Permite aceder à configuração do dispositivo
    //val day = true // Must change this in the future
    val day = weatherUIState.isDay //variável dinâmica que muda consoante as horas de nascer e pôr do sol na localização selecionada pelo utilizador

    val mapt = getWeatherCodeMap() //hashMap que contém os códigos e as respetivas imagens
    val wCode = mapt[weathercode] //pega apenas no código da imagem
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

    val wIcon = context.resources.getIdentifier(wImage, "drawable", context.packageName) //mostra no layout a imagem correspondente à imagem do código processado

    //Se o dispositivo estiver em modo paisagem chama a função LandscapeWeatherUI, caso esteja em modo retrato chama a função PortraitWeatherUI
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
            onLatitudeChange = { newValue -> weatherViewModel.updateLatitude(newValue) },
            onLongitudeChange = { newValue -> weatherViewModel.updateLongitude(newValue)},
            onUpdateButtonClick = { weatherViewModel.fetchWeather() },
            //Opcional: ação de clicar no ícone do mapa
            onMapIconClick = {
                val intent = Intent(context, LocationPickerActivity::class.java)
                mapLauncher.launch(intent)
            }
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
            onLatitudeChange = { newValue -> weatherViewModel.updateLatitude(newValue) },
            onLongitudeChange = { newValue -> weatherViewModel.updateLongitude(newValue)},
            onUpdateButtonClick = { weatherViewModel.fetchWeather() },
            //Opcional: ação de clicar no ícone do mapa
            onMapIconClick = {
                val intent = Intent(context, LocationPickerActivity::class.java)
                mapLauncher.launch(intent)
            }
        )
    }
}

//Função responsável por apresentar a UI no modo retrato
@Composable
fun PortraitWeatherUI(
    wIcon: Int, latitude: String, longitude: String, temperature: Float,
    windSpeed: Float, windDirection: Int, weathercode: Int,
    seaLevelPressure: Float, time: String,
    onLatitudeChange: (String) -> Unit, onLongitudeChange: (String) -> Unit,
    onUpdateButtonClick: () -> Unit,
    onMapIconClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()), // Permite deslizar o ecrã
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //Ícone do tempo, temperatura e hora (última modificação)
        WeatherCard(temperature = temperature, wIcon = wIcon, time = time)

        //O cartão das coordenadas latitude e longitude
        CoordinatesCard(
            latitude = latitude,
            longitude = longitude,
            onLatitudeChange = onLatitudeChange,
            onLongitudeChange = onLongitudeChange,
            onUpdateButtonClick = onUpdateButtonClick,
            onMapIconClick = onMapIconClick
        )

        //O cartão com os detalhes recebidos da API
        WeatherDetails(
            windSpeed = windSpeed,
            windDirection = windDirection,
            seaLevelPressure = seaLevelPressure
        )
    }
}

//Função responsável por apresentar a UI no modo paisagem
@Composable
fun LandscapeWeatherUI(
    wIcon: Int, latitude: String, longitude: String, temperature: Float,
    windSpeed: Float, windDirection: Int, weathercode: Int,
    seaLevelPressure: Float, time: String,
    onLatitudeChange: (String) -> Unit, onLongitudeChange: (String) -> Unit,
    onUpdateButtonClick: () -> Unit,
    onMapIconClick: () -> Unit

) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, top = 16.dp, bottom = 16.dp, end = 64.dp), //dá um espaço de 64 dp no fim para não sobrepor-se à barra virtual do dispositivo
        verticalAlignment = Alignment.CenterVertically, // Centra tudo verticalmente
        horizontalArrangement = Arrangement.SpaceEvenly // Dá um espaço igual entre as colunas
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            WeatherCard(temperature = temperature, wIcon = wIcon, time = time)
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CoordinatesCard(
                latitude = latitude,
                longitude = longitude,
                onLatitudeChange = onLatitudeChange,
                onLongitudeChange = onLongitudeChange,
                onUpdateButtonClick = onUpdateButtonClick,
                onMapIconClick = onMapIconClick
            )

            WeatherDetails(
                windSpeed = windSpeed,
                windDirection = windDirection,
                seaLevelPressure = seaLevelPressure
            )
        }
    }
}