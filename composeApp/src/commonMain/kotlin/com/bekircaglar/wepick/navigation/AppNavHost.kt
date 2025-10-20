package com.bekircaglar.wepick.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.bekircaglar.wepick.domain.model.Movie
import com.bekircaglar.wepick.presentation.screens.categoryscreen.CategoryScreen
import com.bekircaglar.wepick.presentation.screens.createroom.CreateRoomScreen
import com.bekircaglar.wepick.presentation.screens.innercategory.InnerCategoryScreen
import com.bekircaglar.wepick.presentation.screens.joinroom.JoinRoomScreen
import com.bekircaglar.wepick.presentation.screens.launch.LaunchScreen
import com.bekircaglar.wepick.presentation.screens.results.MovieResult
import com.bekircaglar.wepick.presentation.screens.results.ResultScreen
import com.bekircaglar.wepick.presentation.screens.selectionscreen.SelectionScreen
import kotlinx.serialization.Serializable
import org.jetbrains.compose.ui.tooling.preview.Preview

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

        composable<RoomCode>(
            deepLinks = listOf(
                navDeepLink<RoomCode>(basePath = "we-pick://join") {
                    uriPattern = "we-pick://join?code={roomCode}"
                }
            )
        ) {
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

        composable<Selection> {
            val selection: Selection = it.toRoute()
            SelectionScreen(navController, selection.roomCode)
        }

        composable<InnerCategory> {
            val categoryId: InnerCategory = it.toRoute()

            InnerCategoryScreen(navController, categoryId.categoryId)
        }

        composable(route = Screens.RESULT) {
            ResultScreen(navController)
        }
    }
}

@Serializable
data class RoomCode(
    val roomCode: String,
) {}

@Serializable
data class InnerCategory(
    val categoryId: String,
)

@Serializable
data class Selection(
    val roomCode: String,
)

object Screens {
    const val LAUNCH = "launch"
    const val CATEGORY = "category"
    const val RESULT = "result"
    const val INNER_CATEGORY = "inner_category"
    const val CREATE_ROOM = "create_room"
    const val JOIN_ROOM = "join_room"
    const val SELECTION = "selection"
}