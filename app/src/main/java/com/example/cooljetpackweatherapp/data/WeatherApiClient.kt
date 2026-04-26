package com.example.cooljetpackweatherapp.data

import dam_A51736.coolweatherapp.WeatherData
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

//Objeto em Kotlin que permite fazer os devidos requests à API
object WeatherApiClient {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            }) // Ignora campos JSON que não precisamos
        }
    }

    suspend fun getWeather(lat: Float, lon: Float): WeatherData? {
        val reqString = buildString {
            append("https://api.open-meteo.com/v1/forecast?")
            append("latitude=${lat}&longitude=${lon}&")
            append("current_weather=true&")
            append("timezone=auto&") //Permite que a API retorne a hora correta das coordenadas inseridas
            append("daily=sunrise,sunset&") //Recebe as horas de nascer e pôr do sol
            append("hourly=temperature_2m,weathercode,pressure_msl,windspeed_10m")
        }
        System.out.println("Getting URL: $reqString")
        return try {
            client.get(reqString).body() // Transforma o JSON em WeatherData
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}