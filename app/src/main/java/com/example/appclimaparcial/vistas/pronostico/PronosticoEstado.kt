package com.example.appclimaparcial.vistas.pronostico

import com.example.appclimaparcial.repositorio.modelos.ListForecast

sealed class PronosticoEstado {
    data class Exito(
        val climas: List<ListForecast>,
    ) : PronosticoEstado()

    data class Error(
        val mensaje: String = "",
    ): PronosticoEstado()

    data object Vacio: PronosticoEstado()
    data object Cargando: PronosticoEstado()
}