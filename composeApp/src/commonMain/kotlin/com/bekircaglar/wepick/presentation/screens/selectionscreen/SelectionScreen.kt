package com.bekircaglar.wepick.presentation.screens.selectionscreen

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
import androidx.navigation.NavHostController
import com.bekircaglar.wepick.domain.model.Movie
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
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.painterResource
import wepick.composeapp.generated.resources.Res
import wepick.composeapp.generated.resources.ic_exit
import wepick.composeapp.generated.resources.ic_hearth_filled
import wepick.composeapp.generated.resources.ic_lighning_filled
import wepick.composeapp.generated.resources.ic_menu
import wepick.composeapp.generated.resources.ic_rotate_left
import wepick.composeapp.generated.resources.ic_x

@Composable
fun SelectionScreen(navHostController: NavHostController) {
    var sampleMovies by remember { mutableStateOf<List<Movie>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }



    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Filmler yükleniyor...")
        }
    } else {
        ContentUI(
            items = sampleMovies,
            onLoadNewMovie = {},
            onNavigationBack = {
                navHostController.navigate(route = Screens.LAUNCH) {
                    popUpTo(Screens.SELECTION) {
                        inclusive = true
                    }
                }
            },
            onLike = { item ->
                println("Liked: ${item.title}")
            },
            onDislike = { item ->
                println("Disliked: ${item.title}")
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ContentUI(
    items: List<Movie>,
    onLoadNewMovie: () -> Unit,
    onNavigationBack: () -> Unit,
    onLike: (Movie) -> Unit,
    onDislike: (Movie) -> Unit,
) {
    var currentIndex by remember { mutableIntStateOf(0) }
    var triggerSwipe by remember { mutableStateOf<SwipeDirection?>(null) }

    // Kullanıcı son 3 filme geldiğinde yeni film yükle
    LaunchedEffect(currentIndex) {
        if (currentIndex >= items.size - 3) {
            onLoadNewMovie()
        }
    }

    Scaffold(
        containerColor = WePickTheme.colors.background,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = WePickTheme.colors.surface.copy(0.2f),
                    titleContentColor = WePickTheme.colors.onBackground
                ),
                title = { },
                navigationIcon = {
                    IconButton(
                        onClick = {},
                        colors = IconButtonDefaults.iconButtonColors().copy(
                            contentColor = WePickTheme.colors.onBackground,
                            disabledContentColor = WePickTheme.colors.onBackground.copy(alpha = 0.38f),
                        )
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_menu),
                            contentDescription = "Menu",
                            modifier = Modifier.size(20.dp),
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = onNavigationBack,
                        colors = IconButtonDefaults.iconButtonColors().copy(
                            contentColor = WePickTheme.colors.onBackground,
                            disabledContentColor = WePickTheme.colors.onBackground.copy(alpha = 0.38f),
                        )
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_exit),
                            contentDescription = "Back",
                            modifier = Modifier.size(20.dp),
                        )
                    }
                }
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(bottom = 24.dp)
            ) {
                ActionButtons(
                    onDislike = {
                        if (currentIndex < items.size) {
                            triggerSwipe = SwipeDirection.LEFT
                        }
                    },
                    onLike = {
                        if (currentIndex < items.size) {
                            triggerSwipe = SwipeDirection.RIGHT
                        }
                    },
                    onRewind = {
                        if (currentIndex > 0) {
                            currentIndex--
                            triggerSwipe = null
                        }
                    },
                    onBoost = {
                        if (currentIndex < items.size) {
                            triggerSwipe = SwipeDirection.RIGHT
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(WePickTheme.colors.background)
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                if (currentIndex < items.size) {
                    if (currentIndex + 1 < items.size) {
                        MovieSwipeCard(movie = items[currentIndex + 1])
                    }

                    key(currentIndex) {
                        SwipeCard(
                            triggerSwipe = triggerSwipe,
                            onSwipeLeft = {
                                onDislike(items[currentIndex])
                                currentIndex++
                                triggerSwipe = null
                            },
                            onSwipeRight = {
                                onLike(items[currentIndex])
                                currentIndex++
                                triggerSwipe = null
                            },
                            content = {
                                MovieSwipeCard(movie = items[currentIndex])
                            }
                        )
                    }
                } else {
                    Text(
                        text = "No more movies",
                        style = MaterialTheme.typography.headlineMedium,
                        color = WePickTheme.colors.onBackground,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}



@Composable
private fun ActionButtons(
    onDislike: () -> Unit,
    onLike: () -> Unit,
    onRewind: () -> Unit,
    onBoost: () -> Unit,

    ) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 40.dp)
    ) {
        // Rewind button
        FloatingActionButton(
            onClick = onRewind,
            modifier = Modifier
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

        Spacer(modifier = Modifier.weight(1f))

        // Dislike button
        FloatingActionButton(
            onClick = onDislike,
            modifier = Modifier
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

        Spacer(modifier = Modifier.width(16.dp))

        // Like button
        FloatingActionButton(
            onClick = onLike,
            modifier = Modifier
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

        Spacer(modifier = Modifier.weight(1f))

        // Boost button
        FloatingActionButton(
            onClick = onBoost,
            modifier = Modifier
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
    }
}

@Composable
private fun ActionButtonsOutlined(
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

enum class SwipeDirection {
    LEFT, RIGHT
}
