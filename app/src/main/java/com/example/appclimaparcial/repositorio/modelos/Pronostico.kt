package com.example.appclimaparcial.repositorio.modelos

import kotlinx.serialization.Serializable

@Serializable
data class ForecastDTO (
    val cod: String,
    val message: Long,
    val cnt: Long,
    val list: List<ListForecast>
)

@Serializable
data class ListForecast(
    val dt_txt: String,
    val main: MainForecast
)

@Serializable
data class MainForecast(
    val temp: Double,
    val temp_min: Double,
    val temp_max: Double,

)