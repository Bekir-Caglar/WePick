package com.bekircaglar.wepick.presentation.screens.selectionscreen.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource
import wepick.composeapp.generated.resources.Res
import wepick.composeapp.generated.resources.go_results

@Composable
fun AnimatedMatchLogoDialog(
    onDismissRequest: () -> Unit
) {
    Dialog(onDismissRequest = {}) {
        val scale = remember { Animatable(0.5f) }

        LaunchedEffect(Unit) {
            scale.animateTo(1.2f, tween(500))
            scale.animateTo(1f, tween(500))
            while (true) {
                scale.animateTo(1.08f, tween(900))
                scale.animateTo(1f, tween(900))
            }
        }

        LaunchedEffect(Unit) {
            delay(2000)
            onDismissRequest()
        }

        val buttonBrush = Brush.horizontalGradient(
            colors = listOf(
                Color(0xFF238AFF),
                Color(0xFF96C3FE),
                Color(0xFFC5DDFC)
            )
        )

        Column(
            modifier = Modifier
                .size(350.dp)
                .background(Color.Transparent),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .scale(scale.value),
                contentAlignment = Alignment.Center
            ) {
                MatchLogo()
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onDismissRequest,
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .align(Alignment.CenterHorizontally),
                shape = MaterialTheme.shapes.medium.copy(
                    all = CornerSize(12.dp)
                ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                ),
                contentPadding = PaddingValues()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            buttonBrush,
                            shape = MaterialTheme.shapes.medium.copy(
                                all = CornerSize(12.dp)
                            )
                        )
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(Res.string.go_results),
                        color = Color.White
                    )
                }
            }
        }
    }
}