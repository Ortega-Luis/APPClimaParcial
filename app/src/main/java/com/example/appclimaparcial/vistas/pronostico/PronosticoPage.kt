package com.example.appclimaparcial.vistas.pronostico

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.appclimaparcial.repositorio.ApiRepositorio
import com.example.appclimaparcial.router.Enrutador
import com.example.appclimaparcial.router.Router

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PronosticoPage(
    navHostController: NavHostController,
    nombre: String
){
    val pronosticoViewModel : PronosticoViewModel = viewModel(
        factory = PronosticoViewModelFactory(
            repositorio = ApiRepositorio(),
            router = Enrutador(navHostController),
            nombre = nombre

        )
    )
    Column {
        PronosticoView(
            state = pronosticoViewModel.uiState,
            onAction = { opcion ->
                pronosticoViewModel.ejecutar(opcion)
            }
        )
    }
}
