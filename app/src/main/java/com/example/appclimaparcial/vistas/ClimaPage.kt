package com.example.appclimaparcial.vistas

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.appclimaparcial.repositorio.ApiRepositorio
import com.example.appclimaparcial.router.Enrutador
import com.example.appclimaparcial.vistas.clima.ClimaView
import com.example.appclimaparcial.vistas.clima.ClimaViewModel
import com.example.appclimaparcial.vistas.clima.ClimaViewModelFactory
import com.example.appclimaparcial.vistas.pronostico.PronosticoListView
import com.example.appclimaparcial.vistas.pronostico.PronosticoView
import com.example.appclimaparcial.vistas.pronostico.PronosticoViewModel
import com.example.appclimaparcial.vistas.pronostico.PronosticoViewModelFactory

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ClimaPage(
    navController: NavController,
    lat: Float,
    lon: Float,
    nombre: String
){
    val viewModel: ClimaViewModel = viewModel(
        factory = ClimaViewModelFactory(
            repositorio = ApiRepositorio(),
            router = Enrutador(navController),
            lat = lat,
            lon = lon,
            nombre = nombre
        )
    )
    val pronosticoViewModel : PronosticoViewModel = viewModel(
        factory = PronosticoViewModelFactory(
            repositorio = ApiRepositorio(),
            router = Enrutador(navController),
            nombre= nombre,
        )
    )

    Column {
        ClimaView(
            state = viewModel.uiState,
            onAction = {opciones -> viewModel.ejecutar(opciones)
            }
        )
        PronosticoView(
            state = pronosticoViewModel.uiState,
            onAction = {opciones -> pronosticoViewModel.ejecutar(opciones)}
        )

    }
}