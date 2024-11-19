package com.example.appclimaparcial.vistas.pronostico

import android.annotation.SuppressLint
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
): ViewModel(){
    var uiState by mutableStateOf<PronosticoEstado>(PronosticoEstado.Vacio)

    @RequiresApi(Build.VERSION_CODES.O)
    fun ejecutar(opcion: PronosticoOpcion){
        when(opcion){
            PronosticoOpcion.actualizarClima -> traerPronostico()
        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun traerPronostico() {
        uiState = PronosticoEstado.Cargando
        viewModelScope.launch {
            try{
                val forecast = repositorio.mostrarPronostico(nombre)
                uiState = PronosticoEstado.Exito(forecast)
            } catch (exception: Exception){
                uiState = PronosticoEstado.Error(exception.localizedMessage ?: "error desconocido")
            }
        }
    }
}

class PronosticoViewModelFactory(
    private val repositorio: Repositorio,
    private val router: Router,
    private val nombre: String,
): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T{
        if (modelClass.isAssignableFrom(PronosticoViewModel::class.java)){
            return PronosticoViewModel(repositorio,router,nombre) as T
        }
        throw IllegalArgumentException("unknown ViewModel Class")
    }
}