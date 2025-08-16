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
import com.bekircaglar.wepick.data.UserSession
import com.bekircaglar.wepick.di.AppModule
import com.bekircaglar.wepick.navigation.AppNavHost
import com.bekircaglar.wepick.utils.StatusManagerFactory
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import com.bekircaglar.wepick.utils.UserStatusManager

@Composable
@Preview()
fun App() {
    KoinApplication(
        application = {
            modules(AppModule().appModule)
        }
    ) {
        WePickTheme {
            val navController = rememberNavController()
            val statusManagerFactory: StatusManagerFactory = koinInject<StatusManagerFactory>()
            val scope = rememberCoroutineScope()

            var statusManager by remember { mutableStateOf<UserStatusManager?>(null) }

            // UserStatusManager'ı initialize et
            LaunchedEffect(UserSession) {
                try {
                    val currentUser = UserSession.getCurrentUserSession()// veya userSession.userId
                    if (currentUser != null && statusManager == null) {
                        val manager = statusManagerFactory.create()
                        manager.initialize(currentUser)
                        statusManager = manager
                        println("✅ UserStatusManager initialized for user: $currentUser")
                    }
                } catch (e: Exception) {
                    println("❌ Failed to initialize UserStatusManager: ${e.message}")
                }
            }

            // Cleanup when app is disposed
            DisposableEffect(Unit) {
                // Sets up the listener to call `NavController.navigate()`
                // for the composable that has a matching `navDeepLink` listed
                ExternalUriHandler.listener = { uri ->
                    navController.navigate(NavUri(uri))
                }

                // Removes the listener when the composable is no longer active
                onDispose {
                    ExternalUriHandler.listener = null

                    // StatusManager'ı cleanup et
                    scope.launch {
                        try {
                            statusManager?.cleanup()
                            statusManager = null
                            println("🔴 UserStatusManager cleaned up")
                        } catch (e: Exception) {
                            println("❌ Error cleaning up UserStatusManager: ${e.message}")
                        }
                    }
                }
            }

            AppNavHost(navController)
        }
    }
}