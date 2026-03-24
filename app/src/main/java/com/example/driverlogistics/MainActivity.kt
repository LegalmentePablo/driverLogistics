package com.example.driverlogistics

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.driverlogistics.ui.home.HomeScreen
import com.example.driverlogistics.ui.home.HomeViewModel
import com.example.driverlogistics.ui.home.HomeViewModelFactory
import com.example.driverlogistics.ui.theme.DriverLogisticsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val homeViewModel: HomeViewModel by viewModels {
        HomeViewModelFactory((application as DriverLogisticsApp).appContainer.observeDeliveriesUseCase)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DriverLogisticsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { _ ->
                    DriverLogisticsNavHost(homeViewModel = homeViewModel)
                }
            }
        }
    }
}

@Composable
private fun DriverLogisticsNavHost(homeViewModel: HomeViewModel) {
    val navController = rememberNavController()
    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                uiState = uiState,
                onDeliveryClick = { deliveryId ->
                    navController.navigate("detail/$deliveryId")
                }
            )
        }

        composable(
            route = "detail/{deliveryId}",
            arguments = listOf(navArgument("deliveryId") { type = NavType.StringType })
        ) { backStackEntry ->
            val deliveryId = backStackEntry.arguments?.getString("deliveryId").orEmpty()
            DetailPlaceholderScreen(deliveryId = deliveryId)
        }
    }
}

@Composable
private fun DetailPlaceholderScreen(deliveryId: String) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Detalle de entrega: $deliveryId",
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}