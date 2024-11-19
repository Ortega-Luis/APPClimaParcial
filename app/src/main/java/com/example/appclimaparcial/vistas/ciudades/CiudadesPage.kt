package com.example.appclimaparcial.vistas.ciudades

import androidx.compose.runtime.Composable
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
