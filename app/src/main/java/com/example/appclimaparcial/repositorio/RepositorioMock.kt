package com.example.appclimaparcial.repositorio

import com.example.appclimaparcial.repositorio.modelos.Ciudad
import com.example.appclimaparcial.repositorio.modelos.Clima
import com.example.appclimaparcial.repositorio.modelos.ListForecast

class RepositorioMock : Repositorio {
    override suspend fun CiudadBuscada(ciudad: String): List<Ciudad> {
        TODO("Not yet implemented")
    }

    override suspend fun mostrarClima(lat: Float, lon: Float): Clima {
        TODO("Not yet implemented")
    }

    override suspend fun mostrarPronostico(nombre: String, lat: Float, lon: Float): List<ListForecast> {
        TODO("Not yet implemented")
    }
}