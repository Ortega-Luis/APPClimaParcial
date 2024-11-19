package com.example.appclimaparcial.vistas.clima

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.NotificationCompat.Style
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import io.ktor.client.request.request
import io.ktor.http.ContentType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClimaView(
    modifier: Modifier = Modifier,
    state: ClimaEstado,
    onAction: (ClimaOpcion) -> Unit
){
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        onAction(ClimaOpcion.actualizarClima)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text("Pronostico del Clima")},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2196F3),
                    titleContentColor = Color.White
                )
            )
        },
        content = { paddingValues ->
            Column (
                modifier = modifier
                    .fillMaxWidth()
                    .padding(paddingValues)
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)

            ){
                when(state){
                    ClimaEstado.Cargando -> LoadingView()
                    is ClimaEstado.Error -> ErrorView(mensaje = state.mensaje)
                    is ClimaEstado.Exito -> ClimaDetailsView(
                        ciudad = state.ciudad,
                        temperatura = state.temperatura,
                        descripcion = state.descripcion,
                        humedad = state.humedad,
                        viento = state.viento,
                        st = state.st,
                        icono = state.icono,
                        temp_max = state.temp_max,
                        temp_min = state.temp_min

                    )
                    ClimaEstado.Vacio -> EmptyView()
                }
                Spacer(modifier = Modifier.height(100.dp))
            }

        }
    )
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
fun ClimaDetailsView(ciudad: String, temperatura: Double, descripcion: String, st: Double, humedad: Long, viento:Double, icono: String, temp_max:Double,temp_min:Double){
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2196F3))

    ) {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                AsyncImage(
                    model = "https://openweathermap.org/img/wn/${icono}@2x.png",
                    contentDescription = "Icono Clima Actual",
                    modifier = Modifier.size(40.dp)
                )
                Text(
                    text = ciudad,
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White
                )
                Text(
                    text = "${temperatura} °c",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White
                )
                Text(
                    text = descripcion,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White
                )

                Text(
                    text = "Sensacion Termica: ${st} °c",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        text = "Min: ${temp_min} °c",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )
                    Text(
                        text = "Max: ${temp_max} °c",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        text = "Humedad: ${humedad}%",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )
                    Text(
                        text = "Viento: ${viento}Km/h",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ClimaViewPreview(){
    ClimaView(
        state = ClimaEstado.Exito(
            ciudad = "Buenos Aires",
            temperatura = 20.1,
            descripcion = "soleado",
            st = 21.4,
            humedad = 54,
            viento = 65.6,
            icono = "01d",
            temp_min = 23.3,
            temp_max = 12.3
        ),
        onAction = {}
    )
}

@Preview(showBackground = true)
@Composable
fun LoadingViewPreview() { LoadingView() }

@Preview(showBackground = true)
@Composable fun ErrorViewPreview() {
    ErrorView(mensaje = "Ha ocurrido un error")
}

@Preview(showBackground = true)
@Composable fun EmptyViewPreview() {
    EmptyView()
}