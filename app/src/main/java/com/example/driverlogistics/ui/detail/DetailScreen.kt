package com.example.driverlogistics.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.driverlogistics.domain.model.DeliveryStatus
import com.example.driverlogistics.ui.components.containerColor
import com.example.driverlogistics.ui.components.contentColor
import com.example.driverlogistics.ui.components.toSpanishLabel

@Composable
fun DetailScreen(
    uiState: DetailUiState,
    onMarkAsDeliveredClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFF4F8FF), Color(0xFFFDF7F2))
                )
            )
    ) {
        when {
            uiState.isLoading -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                    Text(text = "Cargando detalle...", modifier = Modifier.padding(top = 12.dp))
                }
            }

            uiState.delivery == null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = uiState.errorMessage ?: "Entrega no encontrada",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            else -> {
                val delivery = uiState.delivery
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Detalle de entrega",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(text = "Paquete", style = MaterialTheme.typography.labelLarge)
                            Text(text = delivery.packageId, style = MaterialTheme.typography.titleLarge)

                            Text(text = "Direccion", style = MaterialTheme.typography.labelLarge)
                            Text(text = delivery.address, color = Color(0xFF334155))

                            Text(text = "Estado", style = MaterialTheme.typography.labelLarge)
                            Text(
                                text = delivery.status.toSpanishLabel(),
                                color = delivery.status.contentColor(),
                                modifier = Modifier
                                    .background(
                                        color = delivery.status.containerColor(),
                                        shape = androidx.compose.foundation.shape.RoundedCornerShape(999.dp)
                                    )
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }

                    Button(
                        onClick = onMarkAsDeliveredClick,
                        enabled = delivery.status != DeliveryStatus.Delivered && !uiState.isMarkingDelivered,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                    ) {
                        Text(
                            text = if (uiState.isMarkingDelivered) {
                                "Guardando cambios..."
                            } else {
                                "Marcar como entregado"
                            }
                        )
                    }

                    if (uiState.wasQueuedForSync) {
                        Text(
                            text = "Sin internet: la accion quedo en cola para sincronizar.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF9A3412)
                        )
                    }

                    uiState.errorMessage?.let { message ->
                        Text(text = message, color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}
