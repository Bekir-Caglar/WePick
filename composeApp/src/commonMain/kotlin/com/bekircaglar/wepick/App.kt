package com.bekircaglar.wepick

import androidx.compose.runtime.Composable
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
            AppNavHost(navController)
        }
    }

}
