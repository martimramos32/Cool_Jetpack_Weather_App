package com.example.cooljetpackweatherapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.example.cooljetpackweatherapp.ui.theme.CoolJetpackWeatherAppTheme

class LocationPickerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CoolJetpackWeatherAppTheme { // Para que esta nova atividade fique dentro dos temas usados na atividade principal

                //Posição inicial:Lisboa
                var posicaoSelecionada by remember { mutableStateOf(LatLng(38.76, -9.12)) }

                //Controlador da câmara (zoom e posição)
                val cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(posicaoSelecionada, 8f)
                }

                //Desenhar o ecrã desta atividade
                Box(modifier = Modifier.fillMaxSize()) {

                    // Mapa que preenche todo o ecrã
                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        cameraPositionState = cameraPositionState,
                        // Quando clica-se no mapa, o pino muda de sítio
                        onMapClick = { novaPosicao -> posicaoSelecionada = novaPosicao }
                    ) {
                        // Desenha o pino vermelho na posição selecionada
                        Marker(
                            state = MarkerState(position = posicaoSelecionada),
                            title = "Local Escolhido"
                        )
                    }

                    // Botão de Confirmar na parte inferior do ecrã
                    Button(
                        onClick = {
                            // Preparar as coordenadas
                            //A função do Intent é passar os dados desta atividade para a anterior(MainActivity)
                            val resultadoIntent = Intent().apply {
                                putExtra("latitude", posicaoSelecionada.latitude.toString())
                                putExtra("longitude", posicaoSelecionada.longitude.toString())
                            }
                            setResult(RESULT_OK, resultadoIntent)
                            finish() // Fecha o ecrã do mapa
                        },
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(32.dp)
                            .fillMaxWidth()
                    ) {
                        Text("Confirmar Localização")
                    }
                }
            }
        }
    }
}