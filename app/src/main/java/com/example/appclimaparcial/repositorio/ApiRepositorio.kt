package com.example.appclimaparcial.repositorio
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.runtime.clearCompositionErrors
import androidx.core.app.ActivityCompat
import com.example.appclimaparcial.repositorio.modelos.Ciudad
import com.example.appclimaparcial.repositorio.modelos.Clima
import com.example.appclimaparcial.repositorio.modelos.ForecastDTO
import com.example.appclimaparcial.repositorio.modelos.ListForecast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class ApiRepositorio: Repositorio{

    private val apiKey = "625f54f0ba19b17169903c58a48378f0"

    private val cliente = HttpClient(){
        install(ContentNegotiation){
            json(Json { ignoreUnknownKeys = true})
        }
    }


    override suspend fun CiudadBuscada(ciudad: String): List<Ciudad> {
        val respuesta = cliente.get("https://api.openweathermap.org/geo/1.0/direct"){
            parameter("q", ciudad)
            parameter("limit",100)
            parameter("appid", apiKey)
        }
        if (respuesta.status == HttpStatusCode.OK){
            val ciudades = respuesta.body<List<Ciudad>>()
            return ciudades
        }
        else{
            throw Exception()
        }
    }


    override suspend fun mostrarClima(lat: Float, lon: Float): Clima {
        val respuesta = cliente.get("https://api.openweathermap.org/data/2.5/weather"){
            parameter("lat", lat)
            parameter("lon", lon)
            parameter("units", "metric")
            parameter("appid", apiKey)
        }
        if (respuesta.status == HttpStatusCode.OK){
            val clima = respuesta.body<Clima>()
            return clima
        }
        else{
            throw Exception()
        }
    }

    override suspend fun mostrarPronostico(nombre: String): List<ListForecast> {
        val respuesta = cliente.get("https://api.openweathermap.org/data/2.5/forecast"){
            parameter("q", nombre)
            parameter("units", "metric")
            parameter("ctn",20)
            parameter("lang","sp")
            parameter("appid", apiKey)
        }
        if (respuesta.status == HttpStatusCode.OK){
            val forecast = respuesta.body<ForecastDTO>()
            println("Pronostico Obtenido: $forecast")
            return forecast.list
        }
        else{
            println("error ${respuesta.status}")
            throw Exception("error al obtener el pronostico")
        }
    }

}