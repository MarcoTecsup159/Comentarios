package com.example.comentarios

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.room.Room
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenReseña() {
    val context = LocalContext.current
    val db: ReseñaDatabase = crearDatabase(context)
    val dao = db.reseñaDao()
    val coroutineScope = rememberCoroutineScope()

    var bus by remember { mutableStateOf("") }
    var comentario by remember { mutableStateOf("") }
    var calificacion by remember { mutableStateOf("") }
    var reseñas = remember { mutableStateListOf<Reseña>() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestión de Reseñas") },
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                Spacer(Modifier.height(16.dp))

                //mostrar reseñas
                reseñas.forEach { reseña ->
                    ReseñaCard(reseña, dao) { updatedReseña ->
                        bus = updatedReseña.bus.toString()
                        comentario = updatedReseña.comentario.toString()
                        calificacion = updatedReseña.calificacion.toString()
                        //eliminar la reseña actual de la lista para que no se muestre duplicada
                        reseñas.remove(reseña)
                    }
                }

                Spacer(Modifier.height(20.dp))

                TextField(
                    value = bus,
                    onValueChange = { bus = it },
                    label = { Text("Bus") },
                    singleLine = true
                )
                TextField(
                    value = comentario,
                    onValueChange = { comentario = it },
                    label = { Text("Comentario") },
                    singleLine = true
                )
                TextField(
                    value = calificacion,
                    onValueChange = { calificacion = it },
                    label = { Text("Calificación") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )

                Spacer(Modifier.height(20.dp))

                Button(
                    onClick = {
                        val calificacionFloat = calificacion.toFloatOrNull()
                        if (bus.isNotEmpty() && comentario.isNotEmpty() && calificacionFloat != null) {
                            coroutineScope.launch {
                                val nuevaReseña = Reseña(0, bus, comentario, calificacionFloat)
                                dao.insert(nuevaReseña)
                                reseñas.add(nuevaReseña)
                                bus = ""
                                comentario = ""
                                calificacion = ""
                            }
                        }
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Agregar nueva reseña")
                }
            }
        }
    )
}

@Composable
fun crearDatabase(context: Context): ReseñaDatabase {
    return Room.databaseBuilder(
        context,
        ReseñaDatabase::class.java,
        "reseña_db"
    ).build()
}

@Composable
fun ReseñaCard(reseña: Reseña, dao: ReseñaDao, onEdit: (Reseña) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Bus: ${reseña.bus}", style = MaterialTheme.typography.titleLarge)
            Text(text = "Comentario: ${reseña.comentario}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Calificación: ${reseña.calificacion}", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    onEdit(reseña)
                }
            ) {
                Text("Editar")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewScreenReseña() {
    ScreenReseña()
}
