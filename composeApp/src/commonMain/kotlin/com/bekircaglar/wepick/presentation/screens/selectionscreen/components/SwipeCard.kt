package com.bekircaglar.wepick.presentation.screens.selectionscreen.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import com.bekircaglar.wepick.presentation.screens.selectionscreen.SwipeDirection
import kotlinx.coroutines.delay
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun SwipeCard(
    triggerSwipe: SwipeDirection? = null,
    onSwipeLeft: () -> Unit = {},
    onSwipeRight: () -> Unit = {},
    swipeThreshold: Float = 300f,
    sensitivityFactor: Float = 4f,
    content: @Composable () -> Unit
) {
    var offset by remember { mutableStateOf(0f) }
    var isDismissed by remember { mutableStateOf(false) }
    var dismissDirection by remember { mutableStateOf<String?>(null) }
    val density = LocalDensity.current.density

    LaunchedEffect(triggerSwipe) {
        if (triggerSwipe != null && !isDismissed) {
            when (triggerSwipe) {
                SwipeDirection.LEFT -> {
                    offset = -swipeThreshold * 1.5f
                    delay(150)
                    isDismissed = true
                    dismissDirection = "left"
                }
                SwipeDirection.RIGHT -> {
                    offset = swipeThreshold * 1.5f
                    delay(150)
                    isDismissed = true
                    dismissDirection = "right"
                }
            }
        }
    }

    // Animasyonlu offset
    val animatedOffset by animateFloatAsState(
        targetValue = when {
            dismissDirection == "right" -> 1200f
            dismissDirection == "left" -> -1200f
            else -> offset
        },
        animationSpec = androidx.compose.animation.core.spring(
            dampingRatio = 1f,
            stiffness = 300f // Daha düşük stiffness
        ),
        finishedListener = {
            if (dismissDirection != null) {
                when (dismissDirection) {
                    "right" -> onSwipeRight()
                    "left" -> onSwipeLeft()
                }
            }
        }
    )

    // Reset state when card changes
    LaunchedEffect(Unit) {
        offset = 0f
        isDismissed = false
        dismissDirection = null
    }

    // Kart dismiss edildiyse gösterme
    if (!isDismissed) {
        Box(
            modifier = Modifier
                .offset { IntOffset(animatedOffset.roundToInt(), 0) }
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            when {
                                offset > swipeThreshold -> {
                                    isDismissed = true
                                    dismissDirection = "right"
                                }
                                offset < -swipeThreshold -> {
                                    isDismissed = true
                                    dismissDirection = "left"
                                }
                                else -> {
                                    offset = 0f
                                }
                            }
                        }
                    ) { change, dragAmount ->
                        if (!isDismissed) {
                            offset += (dragAmount / density) * sensitivityFactor
                            if (change.positionChange() != Offset.Zero) change.consume()
                        }
                    }
                }
                .graphicsLayer(
                    alpha = when {
                        isDismissed -> 0f
                        else -> (1f - (kotlin.math.abs(animatedOffset) / (swipeThreshold * 2))).coerceIn(0.2f, 1f)
                    },
                    rotationZ = (animatedOffset / 50).coerceIn(-15f, 15f),
                    scaleX = when {
                        isDismissed -> 0.7f
                        else -> (1f - (kotlin.math.abs(animatedOffset) / (swipeThreshold * 6))).coerceIn(0.95f, 1f)
                    },
                    scaleY = when {
                        isDismissed -> 0.7f
                        else -> (1f - (kotlin.math.abs(animatedOffset) / (swipeThreshold * 6))).coerceIn(0.95f, 1f)
                    }
                )
        ) {
            content()
        }
    }
}