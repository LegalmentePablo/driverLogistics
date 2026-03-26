package com.example.driverlogistics

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.driverlogistics.ui.detail.DetailScreen
import com.example.driverlogistics.ui.detail.DetailViewModel
import com.example.driverlogistics.ui.detail.DetailViewModelFactory
import com.example.driverlogistics.ui.home.HomeScreen
import com.example.driverlogistics.ui.home.HomeViewModel
import com.example.driverlogistics.ui.home.HomeViewModelFactory
import com.example.driverlogistics.ui.theme.DriverLogisticsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val homeViewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(
            observeDeliveriesUseCase = (application as DriverLogisticsApp)
                .appContainer
                .observeDeliveriesUseCase,
            resetDeliveriesUseCase = (application as DriverLogisticsApp)
                .appContainer
                .resetDeliveriesUseCase
        )
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
    val appContainer = (navController.context.applicationContext as DriverLogisticsApp).appContainer

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                uiState = uiState,
                onDevResetClick = homeViewModel::resetStatesForDev,
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

            val detailViewModel: DetailViewModel = viewModel(
                factory = DetailViewModelFactory(
                    deliveryId = deliveryId,
                    observeDeliveryByIdUseCase = appContainer.observeDeliveryByIdUseCase,
                    observePendingSyncForDeliveryUseCase = appContainer.observePendingSyncForDeliveryUseCase,
                    markDeliveryAsCompletedUseCase = appContainer.markDeliveryAsCompletedUseCase
                )
            )
            val detailUiState by detailViewModel.uiState.collectAsStateWithLifecycle()

            DetailScreen(
                uiState = detailUiState,
                onMarkAsDeliveredClick = detailViewModel::markAsDelivered
            )
        }
    }
}