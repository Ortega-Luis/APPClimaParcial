package com.example.appclimaparcial.vistas.pronostico

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appclimaparcial.repositorio.modelos.ListForecast
import com.example.appclimaparcial.repositorio.modelos.Main
import com.example.appclimaparcial.repositorio.modelos.MainForecast
import com.example.appclimaparcial.vistas.clima.ClimaViewModel
import com.example.appclimaparcial.vistas.clima.ErrorView

@Composable
fun PronosticoView(
    modifier: Modifier = Modifier,
    state: PronosticoEstado,
    viewModel: ClimaViewModel,
    onAction: (PronosticoOpcion) -> Unit
){
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        onAction(PronosticoOpcion.actualizarClima)

    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        when (state){
            is PronosticoEstado.Error -> ErrorView(mensaje = state.mensaje)
            is PronosticoEstado.Exito -> PronosticoListView(state.climas, viewModel)
            PronosticoEstado.Vacio -> LoadingView()
            PronosticoEstado.Cargando -> EmptyView()


        }
    }
}


@Composable
fun EmptyView(){
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(Icons.Default.Info,
                contentDescription = null,
                tint = Color(0xFF2196F3),
                modifier = Modifier.size(46.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Sin nada que mostrar", color = Color(0xFF2196F3), style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
fun ErrorView(mensaje: String){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally

        )
        {
            Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = mensaje, color = Color(0xFF2196F3), style = MaterialTheme.typography.bodyLarge)

        }
    }
}
@Composable
fun LoadingView(){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            CircularProgressIndicator(
                modifier = Modifier.size(46.dp),
                color = Color (0xFF2196F3))
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Cargando...", color = Color(0xFF2196F3), style = MaterialTheme.typography.bodyLarge)
        }
    }
}
@Composable
fun PronosticoListView(climas: List<ListForecast>, viewModel: ClimaViewModel){
    val context = LocalContext.current
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 8.dp)
    ){
        items(items = climas) { clima ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFBBDEFB))


            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)

                ) {
                    Text(text = "Fecha: ${clima.dt_txt}",color = Color(0xFF2196F3), style = MaterialTheme.typography.bodyMedium)
                    Text(text = "Maxima: ${clima.main.temp_min}", style = MaterialTheme.typography.bodyMedium)
                    Text(text = "Maxima: ${clima.main.temp_max}", style = MaterialTheme.typography.bodyMedium)

                    Button(
                        onClick = {
                            viewModel.compartirPronostico(context, clima )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) { Text("Compartir")}
                }
            }
        }
    }
}


