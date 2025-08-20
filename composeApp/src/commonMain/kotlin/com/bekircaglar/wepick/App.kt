package com.bekircaglar.wepick

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavUri
import com.bekircaglar.wepick.theme.WePickTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.bekircaglar.wepick.data.manager.UserSession
import com.bekircaglar.wepick.di.AppModule
import com.bekircaglar.wepick.navigation.AppNavHost
import com.bekircaglar.wepick.utils.StatusManagerFactory
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import com.bekircaglar.wepick.data.repository.FirebaseStatusRepository
import com.bekircaglar.wepick.utils.UserStatusManager

@Composable
@Preview()
fun App() {
    KoinApplication(
        application = {
            modules(AppModule().appModule)
        }
    ) {
        val statusManagerFactory: StatusManagerFactory = koinInject<StatusManagerFactory>()
        WePickTheme {
            val navController = rememberNavController()

            val scope = rememberCoroutineScope()
            var statusManager by remember { mutableStateOf<UserStatusManager?>(null) }



            LaunchedEffect(UserSession) {
                try {
                    val currentUser = UserSession.getCurrentUserSession()// veya userSession.userId
                    if (currentUser != null && statusManager == null) {
                        val manager = statusManagerFactory.create()
                        manager.initialize(currentUser)
                        statusManager = manager
                    }
                } catch (e: Exception) {
                }
            }

            DisposableEffect(Unit) {
                ExternalUriHandler.listener = { uri ->
                    navController.navigate(NavUri(uri))
                }
                onDispose {
                    ExternalUriHandler.listener = null

                    scope.launch {
                        try {
                            statusManager?.cleanup()
                            statusManager = null
                        } catch (e: Exception) {
                        }
                    }
                }
            }

            AppNavHost(navController)
        }
    }
}