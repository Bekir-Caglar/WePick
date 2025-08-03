package com.bekircaglar.wepick.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.bekircaglar.wepick.presentation.screens.home.HomeScreen
import com.bekircaglar.wepick.presentation.screens.detail.DetailScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home") {
        composable(
            route = Screens.HOME,
        ) {
            HomeScreen(navController)
        }
        composable(
            route = Screens.DETAIL,
        ) {
            DetailScreen(navController)
        }
    }
}

object Screens {
    const val HOME = "home"
    const val DETAIL = "detail"
}