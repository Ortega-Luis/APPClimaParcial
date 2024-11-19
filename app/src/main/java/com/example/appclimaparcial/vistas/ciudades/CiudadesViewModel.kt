package com.example.appclimaparcial.vistas.ciudades

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.appclimaparcial.repositorio.Repositorio
import com.example.appclimaparcial.repositorio.modelos.Ciudad
import com.example.appclimaparcial.router.Router
import com.example.appclimaparcial.router.Ruta
import kotlinx.coroutines.launch


class CiudadesViewModel(
    val repositorio: Repositorio,
    val router: Router
): ViewModel(){
    var uiState by mutableStateOf<CiudadesEstado>(CiudadesEstado.Vacio)
    var ciudades : List<Ciudad> = emptyList()

    fun ejecutar(opciones: CiudadesOpciones){
        when(opciones){
            is CiudadesOpciones.Buscar -> buscar(nombre = opciones.nombre)
            is  CiudadesOpciones.Seleccionar -> seleccionar(ciudad = opciones.ciudad)

        }
    }

    private fun buscar(nombre: String){
        uiState = CiudadesEstado.Cargando
        viewModelScope.launch {
            try {
                ciudades = repositorio.CiudadBuscada(nombre)
                if (ciudades.isEmpty()){
                    uiState = CiudadesEstado.Vacio
                }
                else{
                    uiState = CiudadesEstado.Resultado(ciudades)
                }
            } catch (exception: Exception){
                uiState = CiudadesEstado.Error(exception.message ?: "error")
            }
        }
    }

    private fun seleccionar(ciudad: Ciudad){
        val ruta = Ruta.Clima(
            lat = ciudad.lat,
            lon = ciudad.lon,
            nombre = ciudad.name
        )
        router.navegar(ruta)
    }




    class CiudadesViewModelFactory(
        private val repositorio: Repositorio,
        private val router: Router
    ): ViewModelProvider.Factory{
        @Suppress("unchecked_cast")
        override fun <T : ViewModel> create(modelClass: Class<T>): T{
            if (modelClass.isAssignableFrom(CiudadesViewModel::class.java)){
                return CiudadesViewModel(repositorio,router) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

