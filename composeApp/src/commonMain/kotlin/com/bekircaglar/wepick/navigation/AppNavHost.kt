package com.bekircaglar.wepick.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.bekircaglar.wepick.presentation.screens.categoryscreen.CategoryScreen
import com.bekircaglar.wepick.presentation.screens.createroom.CreateRoomScreen
import com.bekircaglar.wepick.presentation.screens.joinroom.JoinRoomScreen
import com.bekircaglar.wepick.presentation.screens.launch.LaunchScreen
import com.bekircaglar.wepick.presentation.screens.selectionscreen.SelectionScreen
import kotlinx.serialization.Serializable

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

        composable<RoomCode>() {
            val roomCode: RoomCode = it.toRoute()
            CreateRoomScreen(
                navController = navController,
                roomCode = roomCode.roomCode
            )
        }

        composable(
            route = Screens.JOIN_ROOM,
        ) {
            JoinRoomScreen(navController)
        }

        composable(
            route = Screens.SELECTION,
        ) {
            SelectionScreen(navController)
        }
    }
}

@Serializable
data class RoomCode(
    val roomCode: String,
) {
}

object Screens {
    const val LAUNCH = "launch"
    const val CATEGORY = "category"
    const val CREATE_ROOM = "create_room"

    const val JOIN_ROOM = "join_room"
    const val SELECTION = "selection"
}