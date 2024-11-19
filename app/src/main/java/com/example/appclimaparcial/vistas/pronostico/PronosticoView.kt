package com.example.appclimaparcial.vistas.pronostico

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appclimaparcial.repositorio.modelos.ListForecast
import com.example.appclimaparcial.repositorio.modelos.Main
import com.example.appclimaparcial.repositorio.modelos.MainForecast
import com.example.appclimaparcial.vistas.clima.ClimaViewModel
import com.example.appclimaparcial.vistas.clima.ErrorView
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PronosticoView(
    modifier: Modifier = Modifier,
    state: PronosticoEstado,
    onAction: (PronosticoOpcion) -> Unit
){
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        onAction(PronosticoOpcion.actualizarClima)

    }
    Text(
        text = "Pronosticos"
    )
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        when (state){
            is PronosticoEstado.Error -> ErrorView(mensaje = state.mensaje)
            is PronosticoEstado.Exito -> PronosticoListView(state.climas)
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
            Icon(Icons.Default.Warning, contentDescription = "Sin nada que mostrar", tint = MaterialTheme.colorScheme.error)
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun convertirTimestamp(timestamp: Long): String {
    val instant = Instant.ofEpochSecond(timestamp)
    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm")
        .withZone(ZoneId.systemDefault())
    return formatter.format(instant)
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PronosticoListView(climas: List<ListForecast>){
    LazyColumn {
        items(items = climas) {
            Card(modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()) {
                val fecha = convertirTimestamp(it.dt)
                Text(text = fecha,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary)
                Text(text = "Temperatura: ${it.main.temp}º",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary)
                Text(text = "Minima: ${it.main.temp_min}º -> Maxima:${it.main.temp_max}º",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
            }
        }
    }
}


