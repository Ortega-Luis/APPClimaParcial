package com.example.appclimaparcial.vistas.ciudades

import android.graphics.drawable.Icon
import android.provider.CalendarContract.Colors
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appclimaparcial.repositorio.modelos.Ciudad
import com.example.appclimaparcial.router.Ruta
import okhttp3.internal.wait

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CiudadesView(
    modifier: Modifier = Modifier,
    state : CiudadesEstado,
    onAction: (CiudadesOpciones)-> Unit

){
    var value by remember { mutableStateOf("")}

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Buscar Ciudades")},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2196F3),
                    titleContentColor = Color.White
                )
            )
        },
        content = { paddingValues ->
            Column(
                modifier = modifier
                    .padding(paddingValues)
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)

            ) {
                    OutlinedTextField(
                        value = value,
                        label = { Text(text = "Buscar por nombre", color = Color.White) },
                        onValueChange = {
                            value = it
                            onAction(CiudadesOpciones.Buscar(value))
                        },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null)
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF2196F3),
                            unfocusedBorderColor = Color(0xFFBBDEFB)
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        )

                    when (state) {
                        CiudadesEstado.Cargando -> Text(text = "Cargando")
                        is CiudadesEstado.Error -> Text(text = state.mensaje)
                        is CiudadesEstado.Resultado -> ListaCiudades(state.ciudades) {
                            onAction(
                                CiudadesOpciones.Seleccionar(it)
                            )
                        }

                        CiudadesEstado.Vacio -> Text(text = "No hay resultados")
                    }
            }
        }
    )
}


@Composable
fun ListaCiudades(ciudades: List<Ciudad>, onSelect: (Ciudad)-> Unit){
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(items = ciudades){
            Card(
                onClick = {onSelect(it)},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF2196F3))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Place, contentDescription = null)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = it.name, style = MaterialTheme.typography.bodyLarge, color = Color.White)
                    Text(text = it.country, style = MaterialTheme.typography.bodyLarge, color = Color.White)

                }

            }
        }
    }
}

