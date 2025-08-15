package com.bekircaglar.wepick

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        handleIntent(intent)

        println("onCreate called with action: ${intent.action} and data: ${intent.data}")

        setContent {
            App()
        }
    }
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
        println("onNewIntent called with action: ${intent.action} and data: ${intent.data}")
    }

    private fun handleIntent(intent: Intent) {
        when (intent.action) {
            Intent.ACTION_VIEW -> {
                intent.data?.toString()?.let { uri ->
                    ExternalUriHandler.onNewUri(uri)
                }
                println("Intent received with action: ${intent.action} and data: ${intent.data}")
            }
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}