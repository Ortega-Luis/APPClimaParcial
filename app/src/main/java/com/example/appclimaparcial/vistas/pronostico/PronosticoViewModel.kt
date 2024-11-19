package com.example.appclimaparcial.vistas.pronostico

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.appclimaparcial.repositorio.Repositorio
import com.example.appclimaparcial.router.Router
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class PronosticoViewModel(
    val repositorio: Repositorio,
    val router: Router,
    val nombre: String,
    val lat: Float,
    val lon: Float
): ViewModel(){
    var uiState by mutableStateOf<PronosticoEstado>(PronosticoEstado.Vacio)

    @RequiresApi(Build.VERSION_CODES.O)
    fun ejecutar(opcion: PronosticoOpcion){
        when(opcion){
            PronosticoOpcion.actualizarClima -> traerPronostico()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun traerPronostico(){
        uiState = PronosticoEstado.Cargando
        viewModelScope.launch {
            try {
                val forecast = repositorio.mostrarPronostico(nombre,lat,lat).filter { pronostico ->
                    val currentDate = LocalDate.now()
                    val forecastDate = LocalDate.parse(pronostico.dt_txt.substring(0,10),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    forecastDate == currentDate
                }
                uiState = PronosticoEstado.Exito(forecast)
            } catch (exception: Exception){
                uiState = PronosticoEstado.Error(exception.localizedMessage ?: "error")
            }
        }
    }

}

class PronosticoViewModelFactory(
    private val repositorio: Repositorio,
    private val router: Router,
    private val nombre: String,
    private val lat: Float,
    private val lon: Float
): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T{
        if (modelClass.isAssignableFrom(PronosticoViewModel::class.java)){
            return PronosticoViewModel(repositorio,router,nombre,lat,lon) as T
        }
        throw IllegalArgumentException("unknown ViewModel Class")
    }
}