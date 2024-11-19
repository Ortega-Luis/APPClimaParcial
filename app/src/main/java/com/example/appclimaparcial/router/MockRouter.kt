package com.example.appclimaparcial.router

class MockRouter : Router {
    override fun navegar(ruta: Ruta) {
        println("navegar a: ${ruta.id}")
    }
}