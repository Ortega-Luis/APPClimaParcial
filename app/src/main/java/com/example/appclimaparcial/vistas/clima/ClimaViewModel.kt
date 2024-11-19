package com.example.appclimaparcial.vistas.clima

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.appclimaparcial.repositorio.Repositorio
import com.example.appclimaparcial.repositorio.modelos.ListForecast
import com.example.appclimaparcial.router.Router
import kotlinx.coroutines.launch

class ClimaViewModel(
    val repositorio: Repositorio,
    val router: Router,
    val lat: Float,
    val lon: Float,
    val nombre: String
) : ViewModel(){
    var uiState by mutableStateOf<ClimaEstado>(ClimaEstado.Vacio)
    fun ejecutar(opcion: ClimaOpcion){
        when(opcion){
            ClimaOpcion.actualizarClima -> mostrarClima()
        }
    }

    fun mostrarClima(){
        uiState = ClimaEstado.Cargando
        viewModelScope.launch {
            try {
                val clima = repositorio.mostrarClima(lat = lat, lon = lon)
                uiState = ClimaEstado.Exito(
                    icono = clima.weather.first().icon,
                    ciudad = clima.name,
                    temperatura = clima.main.temp,
                    descripcion = clima.weather.first().description,
                    humedad = clima.main.humidity,
                    viento = clima.wind.speed,
                    temp_min = clima.main.temp_min,
                    temp_max = clima.main.temp_min,
                    st = clima.main.feels_like,
                )
            }
            catch (exception: Exception){
                uiState = ClimaEstado.Error(exception.localizedMessage ?: "Error")
            }
        }
    }

}

class ClimaViewModelFactory(
    private val repositorio: Repositorio,
    private val router: Router,
    private val lat: Float,
    private val lon: Float,
    private val nombre: String
) : ViewModelProvider.Factory{
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T{
        if (modelClass.isAssignableFrom(ClimaViewModel::class.java)){
            return ClimaViewModel(repositorio,router,lat,lon,nombre) as T
            }
            throw IllegalArgumentException("Unknow ViewModel Class")
    }
}