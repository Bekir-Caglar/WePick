package com.bekircaglar.wepick.presentation.screens.selectionscreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.bekircaglar.wepick.domain.model.Movie
import com.bekircaglar.wepick.domain.model.sampleMovie
import com.bekircaglar.wepick.navigation.Screens
import com.bekircaglar.wepick.presentation.screens.selectionscreen.components.MovieSwipeCard
import com.bekircaglar.wepick.presentation.screens.selectionscreen.components.SwipeCard
import com.bekircaglar.wepick.theme.WePickTheme
import com.bekircaglar.wepick.theme.back1
import com.bekircaglar.wepick.theme.back2
import com.bekircaglar.wepick.theme.dislike1
import com.bekircaglar.wepick.theme.dislike2
import com.bekircaglar.wepick.theme.like1
import com.bekircaglar.wepick.theme.like2
import com.bekircaglar.wepick.theme.superLike1
import com.bekircaglar.wepick.theme.superLike2
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import wepick.composeapp.generated.resources.Res
import wepick.composeapp.generated.resources.ic_exit
import wepick.composeapp.generated.resources.ic_hearth_filled
import wepick.composeapp.generated.resources.ic_lighning_filled
import wepick.composeapp.generated.resources.ic_menu
import wepick.composeapp.generated.resources.ic_rotate_left
import wepick.composeapp.generated.resources.ic_x

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.scale

@Composable
fun ActionButtons(
    onDislike: () -> Unit,
    onLike: () -> Unit,
    onRewind: () -> Unit,
    onBoost: () -> Unit,
) {
    // Her buton için scale state
    var rewindPressed by remember { mutableStateOf(false) }
    var dislikePressed by remember { mutableStateOf(false) }
    var likePressed by remember { mutableStateOf(false) }
    var boostPressed by remember { mutableStateOf(false) }

    val rewindScale by animateFloatAsState(
        targetValue = if (rewindPressed) 0.85f else 1f,
        animationSpec = tween(100),
        label = "rewindScale"
    )
    val dislikeScale by animateFloatAsState(
        targetValue = if (dislikePressed) 0.85f else 1f,
        animationSpec = tween(100),
        label = "dislikeScale"
    )
    val likeScale by animateFloatAsState(
        targetValue = if (likePressed) 0.85f else 1f,
        animationSpec = tween(100),
        label = "likeScale"
    )
    val boostScale by animateFloatAsState(
        targetValue = if (boostPressed) 0.85f else 1f,
        animationSpec = tween(100),
        label = "boostScale"
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 40.dp)
    ) {
        // Rewind button
        FloatingActionButton(
            onClick = {
                rewindPressed = true
                onRewind()
            },
            modifier = Modifier
                .scale(rewindScale)
                .size(40.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(back1, back2),
                        startX = 0f,
                        endX = 80f
                    ),
                    shape = CircleShape
                ),
            containerColor = Color.Transparent,
            contentColor = Color(0xFF999999),
            shape = CircleShape,
            elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp)
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_rotate_left),
                contentDescription = "Rewind",
                modifier = Modifier.size(20.dp),
                tint = Color.White
            )
        }
        LaunchedEffect(rewindPressed) {
            if (rewindPressed) {
                kotlinx.coroutines.delay(100)
                rewindPressed = false
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Dislike button
        FloatingActionButton(
            onClick = {
                dislikePressed = true
                onDislike()
            },
            modifier = Modifier
                .scale(dislikeScale)
                .size(56.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(dislike1, dislike2),
                        startX = 0f,
                        endX = 80f
                    ),
                    shape = CircleShape
                ),
            containerColor = Color.Transparent,
            contentColor = Color(0xFFFF4458),
            shape = CircleShape,
            elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp)
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_x),
                contentDescription = "Dislike",
                modifier = Modifier.size(24.dp),
                tint = Color.White
            )
        }
        LaunchedEffect(dislikePressed) {
            if (dislikePressed) {
                kotlinx.coroutines.delay(100)
                dislikePressed = false
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Like button
        FloatingActionButton(
            onClick = {
                likePressed = true
                onLike()
            },
            modifier = Modifier
                .scale(likeScale)
                .size(56.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(like1, like2),
                        startX = 0f,
                        endX = 80f
                    ),
                    shape = CircleShape
                ),
            containerColor = Color.Transparent,
            contentColor = Color(0xFF42DCA3),
            shape = CircleShape,
            elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp)
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_hearth_filled),
                contentDescription = "Like",
                modifier = Modifier.size(40.dp),
                tint = Color.White
            )
        }
        LaunchedEffect(likePressed) {
            if (likePressed) {
                kotlinx.coroutines.delay(100)
                likePressed = false
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Boost button
        FloatingActionButton(
            onClick = {
                boostPressed = true
                onBoost()
            },
            modifier = Modifier
                .scale(boostScale)
                .size(40.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(superLike1, superLike2),
                        startX = 0f,
                        endX = 80f
                    ),
                    shape = CircleShape
                ),
            containerColor = Color.Transparent,
            contentColor = Color.White,
            shape = CircleShape,
            elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp)
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_lighning_filled),
                contentDescription = "Boost",
                modifier = Modifier.size(24.dp),
                tint = Color.White
            )
        }
        LaunchedEffect(boostPressed) {
            if (boostPressed) {
                kotlinx.coroutines.delay(100)
                boostPressed = false
            }
        }
    }
}

@Composable
fun ActionButtonsOutlined(
    onDislike: () -> Unit,
    onLike: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 48.dp)
    ) {
        // Rewind button
        FloatingActionButton(
            onClick = { /* Geri alma işlemi */ },
            modifier = Modifier
                .size(40.dp)
                .background(
                    color = WePickTheme.colors.background,
                    shape = CircleShape
                )
                .border(
                    width = 2.dp,
                    color = back1,
                    shape = CircleShape
                ),
            containerColor = WePickTheme.colors.background,
            shape = CircleShape,
            elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp)
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_rotate_left),
                contentDescription = "Rewind",
                modifier = Modifier.size(20.dp),
                tint = back2
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Dislike button
        FloatingActionButton(
            onClick = onDislike,
            modifier = Modifier
                .size(56.dp)
                .background(
                    color = WePickTheme.colors.background,
                    shape = CircleShape
                )
                .border(
                    width = 2.dp,
                    color = dislike1,
                    shape = CircleShape
                ),
            containerColor = WePickTheme.colors.background,
            shape = CircleShape,
            elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp)
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_x),
                contentDescription = "Dislike",
                modifier = Modifier.size(24.dp),
                tint = dislike2
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Like button
        FloatingActionButton(
            onClick = onLike,
            modifier = Modifier
                .size(56.dp)
                .background(
                    color = WePickTheme.colors.background,
                    shape = CircleShape
                )
                .border(
                    width = 2.dp,
                    color = like1,
                    shape = CircleShape
                ),
            containerColor = WePickTheme.colors.background,
            shape = CircleShape,
            elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp)
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_hearth_filled),
                contentDescription = "Like",
                modifier = Modifier.size(40.dp),
                tint = like2
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Boost button
        FloatingActionButton(
            onClick = { /* Boost işlemi */ },
            modifier = Modifier
                .size(40.dp)
                .background(
                    color = WePickTheme.colors.background,
                    shape = CircleShape
                )
                .border(
                    width = 2.dp,
                    color = superLike1,
                    shape = CircleShape
                ),
            containerColor = WePickTheme.colors.background,
            shape = CircleShape,
            elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp)
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_lighning_filled),
                contentDescription = "Boost",
                modifier = Modifier.size(24.dp),
                tint = superLike2
            )
        }
    }
}