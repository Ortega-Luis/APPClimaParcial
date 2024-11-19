package com.example.appclimaparcial.vistas.ciudades

import com.example.appclimaparcial.repositorio.modelos.Ciudad

sealed class CiudadesOpciones {
    data class Buscar( val nombre:String) : CiudadesOpciones()
    data class Seleccionar( val ciudad: Ciudad) : CiudadesOpciones()
}