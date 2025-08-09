package com.bekircaglar.wepick.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.bekircaglar.wepick.presentation.screens.category.CategoryScreen
import com.bekircaglar.wepick.presentation.screens.createroom.CreateRoomScreen
import com.bekircaglar.wepick.presentation.screens.joinroom.JoinRoomScreen
import com.bekircaglar.wepick.presentation.screens.launch.LaunchScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screens.LAUNCH) {
        composable(
            route = Screens.LAUNCH,
        ) {
            LaunchScreen(navController)
        }

        composable(
            route = Screens.CATEGORY,
        ) {
            CategoryScreen(navController)
        }

        composable(
            route = Screens.CREATE_ROOM,
        ) {
            CreateRoomScreen(navController) // Uncomment when CreateRoomScreen is implemented
        }

        composable(
            route = Screens.JOIN_ROOM,
        ) {
            JoinRoomScreen(navController) // Uncomment when JoinRoomScreen is implemented
        }


    }
}

object Screens {
    const val LAUNCH = "launch"
    const val CATEGORY = "category"
    const val CREATE_ROOM = "create_room"

    const val JOIN_ROOM = "join_room"

}