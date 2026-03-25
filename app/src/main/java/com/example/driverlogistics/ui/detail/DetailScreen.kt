package com.example.driverlogistics.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.driverlogistics.domain.model.DeliveryStatus

@Composable
fun DetailScreen(
    uiState: DetailUiState,
    onMarkAsDeliveredClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    when {
        uiState.isLoading -> {
            Column(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.padding(horizontal = 16.dp))
            }
        }

        uiState.delivery == null -> {
            Column(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = uiState.errorMessage ?: "Entrega no encontrada",
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }

        else -> {
            val delivery = uiState.delivery
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(text = "Paquete: ${delivery.packageId}", style = MaterialTheme.typography.titleLarge)
                Text(text = "Direccion: ${delivery.address}")
                Text(
                    text = "Estado: ${delivery.status.name}",
                    color = if (delivery.status == DeliveryStatus.Delivered) {
                        Color(0xFF1B5E20)
                    } else {
                        Color(0xFF9A3412)
                    }
                )

                Button(
                    onClick = onMarkAsDeliveredClick,
                    enabled = delivery.status != DeliveryStatus.Delivered && !uiState.isMarkingDelivered,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (uiState.isMarkingDelivered) {
                            "Guardando..."
                        } else {
                            "Marcar como entregado"
                        }
                    )
                }

                if (uiState.wasQueuedForSync) {
                    Text(
                        text = "Sin internet: accion guardada para sincronizacion",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                uiState.errorMessage?.let { message ->
                    Text(text = message, color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}
