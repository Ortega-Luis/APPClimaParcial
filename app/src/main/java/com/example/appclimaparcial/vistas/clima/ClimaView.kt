package com.example.appclimaparcial.vistas.clima

import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.NotificationCompat.Style
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.appclimaparcial.R
import io.ktor.client.request.request
import io.ktor.http.ContentType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClimaView(
    modifier: Modifier = Modifier.padding(top = 60.dp),
    state: ClimaEstado,
    onAction: (ClimaOpcion) -> Unit
){
    var datoCompartir by remember { mutableStateOf("Compartir dato") }
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

            ){ Row {
                Text(text = "Clima Actual",
                    color = Color.White
                )
                Spacer(modifier = Modifier.padding(5.dp))
                botonCompartir(textToShere = datoCompartir)
            }
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
fun botonCompartir(textToShere : String){
    val context = LocalContext.current
    val shareLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {_ -> context }

    Button(
        onClick = {
            val sendIntent = Intent(Intent.ACTION_SEND).apply {
                type= "text/plain"
                putExtra(Intent.EXTRA_TEXT, textToShere)
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            shareLauncher.launch(shareIntent)
        },
        modifier = Modifier.padding(4.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Share,
            contentDescription = "Compartir",
            tint = Color.White
        )
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
fun ClimaDetailsView(
    ciudad: String,
    temperatura: Double,
    descripcion: String,
    st: Double,
    humedad: Long,
    viento:Double,
    icono: String,
    temp_max:Double,
    temp_min:Double
){
    val url = "https://openweathermap.org/img/wn/${icono}@2x.png"
    Log.d( "ClimaDetailsView: ", "URL del icono: $url")
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
                    model = url,
                    contentDescription = "Icono Clima Actual",
                    modifier = Modifier.size(60.dp)
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