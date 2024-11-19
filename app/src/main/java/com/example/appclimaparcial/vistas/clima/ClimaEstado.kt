package com.example.appclimaparcial.vistas.clima

sealed class ClimaEstado {
    data class Exito(
        val ciudad: String = "",
        val temperatura: Double = 0.0,
        val descripcion: String = "",
        val st: Double = 0.0,
        val humedad: Long = 0,
        val viento: Double = 0.0,
        val icono: String = "",
        val temp_min: Double,
        val temp_max: Double
    ): ClimaEstado()

    data class Error(
        val mensaje: String = ""
    ): ClimaEstado()

    data object  Vacio: ClimaEstado()
    data object  Cargando: ClimaEstado()
}