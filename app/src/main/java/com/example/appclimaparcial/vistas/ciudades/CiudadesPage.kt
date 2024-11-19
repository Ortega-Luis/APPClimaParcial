package com.example.appclimaparcial.vistas.ciudades

import android.content.Context
import android.location.LocationProvider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.appclimaparcial.repositorio.ApiRepositorio
import com.example.appclimaparcial.router.Enrutador

@Composable
fun CiudadesPage(
    navHostController: NavHostController
){
    val viewModel : CiudadesViewModel = viewModel(
        factory = CiudadesViewModel.CiudadesViewModelFactory(
           repositorio = ApiRepositorio(),
            router = Enrutador(navHostController)
        )
    )
    CiudadesView(
        state = viewModel.uiState,
        onAction = {
            opcion -> viewModel.ejecutar(opcion)
        }
    )
}
