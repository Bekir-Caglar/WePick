package com.bekircaglar.wepick

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.navigation.NavUri
import com.bekircaglar.wepick.theme.WePickTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.bekircaglar.wepick.di.AppModule
import com.bekircaglar.wepick.navigation.AppNavHost
import org.koin.compose.KoinApplication
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
            DisposableEffect(Unit) {
                // Sets up the listener to call `NavController.navigate()`
                // for the composable that has a matching `navDeepLink` listed
                ExternalUriHandler.listener = { uri ->
                    navController.navigate(NavUri(uri))
                }
                // Removes the listener when the composable is no longer active
                onDispose {
                    ExternalUriHandler.listener = null
                }
            }
            AppNavHost(navController)
        }
    }

}
