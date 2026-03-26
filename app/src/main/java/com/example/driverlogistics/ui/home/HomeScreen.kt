package com.example.driverlogistics.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.driverlogistics.domain.model.Delivery
import com.example.driverlogistics.domain.model.DeliveryStatus
import com.example.driverlogistics.ui.components.containerColor
import com.example.driverlogistics.ui.components.contentColor
import com.example.driverlogistics.ui.components.toSpanishLabel

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onDevResetClick: () -> Unit,
    onDeliveryClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val deliveredCount = uiState.deliveries.count { it.status == DeliveryStatus.Delivered }
    val pendingCount = uiState.deliveries.count { it.status == DeliveryStatus.Pending }

    Box(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFFDF7F2), Color(0xFFF4F8FF))
                )
            )
    ) {
        when {
            uiState.isLoading -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                    Text(
                        text = "Cargando entregas...",
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }
            }

            uiState.deliveries.isEmpty() -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Aun no hay entregas",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Cuando lleguen nuevas entregas apareceran aqui",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                }
            }

            else -> {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 16.dp, end = 16.dp, top = 22.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    HomeHeaderCard(
                        deliveredCount = deliveredCount,
                        pendingCount = pendingCount,
                        totalCount = uiState.deliveries.size,
                        onDevResetClick = onDevResetClick
                    )
                }

                items(uiState.deliveries, key = { it.id }) { delivery ->
                    DeliveryRow(
                        delivery = delivery,
                        onClick = { onDeliveryClick(delivery.id) }
                    )
                }
            }

            uiState.errorMessage?.let { message ->
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                )
            }
            }
        }
    }
}

@Composable
private fun HomeHeaderCard(
    deliveredCount: Int,
    pendingCount: Int,
    totalCount: Int,
    onDevResetClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = "Panel de entregas",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Total: $totalCount",
                color = Color(0xFFCBD5E1),
                modifier = Modifier.padding(top = 4.dp)
            )
            FilledTonalButton(
                onClick = onDevResetClick,
                modifier = Modifier.padding(top = 12.dp)
            ) {
                Text(text = "Reset DEV estados")
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFF1E293B))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                SummaryPill("Pendientes", pendingCount, Color(0xFFFFF3E0), Color(0xFF9A3412), Modifier.weight(1f))
                SummaryPill("Entregadas", deliveredCount, Color(0xFFE8F5E9), Color(0xFF1B5E20), Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun SummaryPill(
    label: String,
    value: Int,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier, colors = CardDefaults.cardColors(containerColor = containerColor)) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(text = label, color = contentColor, style = MaterialTheme.typography.labelMedium)
            Text(
                text = value.toString(),
                color = contentColor,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun DeliveryRow(
    delivery: Delivery,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .fillMaxHeight()
                    .background(delivery.status.contentColor())
            )
            Column(modifier = Modifier.padding(14.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        text = delivery.packageId,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(
                                color = delivery.status.containerColor(),
                                shape = CircleShape
                            )
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Spacer(
                            modifier = Modifier
                                .width(6.dp)
                                .height(6.dp)
                                .background(delivery.status.contentColor(), CircleShape)
                        )
                        Text(
                            text = delivery.status.toSpanishLabel(),
                            color = delivery.status.contentColor(),
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier.padding(start = 6.dp)
                        )
                    }
                }
                Text(
                    text = delivery.address,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF334155),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}
