package com.example.appclimaparcial.repositorio

import com.example.appclimaparcial.repositorio.modelos.Clima
import com.example.appclimaparcial.repositorio.modelos.ListForecast
import com.example.appclimaparcial.repositorio.modelos.Ciudad

interface Repositorio {
    suspend fun CiudadBuscada(ciudad: String): List<Ciudad>
    suspend fun mostrarClima(lat: Float, lon: Float) : Clima
    suspend fun mostrarPronostico(nombre: String) : List<ListForecast>

}

