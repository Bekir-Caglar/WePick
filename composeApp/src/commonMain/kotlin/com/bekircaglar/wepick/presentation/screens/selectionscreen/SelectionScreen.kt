package com.bekircaglar.wepick.presentation.screens.selectionscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.bekircaglar.wepick.navigation.Screens
import com.bekircaglar.wepick.presentation.screens.selectionscreen.components.ActionButtons
import com.bekircaglar.wepick.presentation.screens.selectionscreen.components.AnimatedMatchLogoDialog
import com.bekircaglar.wepick.presentation.screens.selectionscreen.components.MovieSwipeCard
import com.bekircaglar.wepick.presentation.screens.selectionscreen.components.SwipeCard
import com.bekircaglar.wepick.theme.WePickTheme
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.animateLottieCompositionAsState
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import wepick.composeapp.generated.resources.Res
import wepick.composeapp.generated.resources.ic_exit
import wepick.composeapp.generated.resources.ic_menu
import wepick.composeapp.generated.resources.ic_rotate_right

@Composable
fun SelectionScreen(navHostController: NavHostController, roomCode: String) {
    val viewModel: SelectionViewModel = koinViewModel()
    val roomData by viewModel.roomData.collectAsStateWithLifecycle()
    val isInitialLoading by viewModel.isInitialLoading.collectAsStateWithLifecycle()
    val isLoadingMore by viewModel.isLoadingMore.collectAsStateWithLifecycle()
    val selectionItemList by viewModel.selectionItemList.collectAsStateWithLifecycle()
    val hasMorePages by viewModel.hasMorePages.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()
    val matchId by viewModel.matchFound.collectAsStateWithLifecycle()
    var showDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    var isCountdownFinished by remember { mutableStateOf(false) }

    LaunchedEffect(roomCode) {
        if (roomCode.isNotEmpty()) {
            viewModel.getRoom(roomCode = roomCode)
        }
    }

    LaunchedEffect(matchId) {
        matchId?.let { matchedMovieId ->
            showDialog = true
        }
    }
    if (showDialog) {
        AnimatedMatchLogoDialog(onDismissRequest = {
            showDialog = false
        })
    }

    LaunchedEffect(errorMessage) {
        errorMessage?.let { message ->
            coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    message = message,
                    duration = SnackbarDuration.Short
                )
            }
            viewModel.clearError()
        }
    }

    if (!isCountdownFinished) {
        CountDownContent(
            onFinished = { isCountdownFinished = true }
        )
        return
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { _ ->
        when {
            isInitialLoading -> {
                LoadingContent()
            }

            selectionItemList.isEmpty() && !isInitialLoading -> {
                EmptyContent(
                    onRefresh = { viewModel.refreshMovies() }
                )
            }

            else -> {
                ContentUI(
                    items = selectionItemList,
                    isLoadingMore = isLoadingMore,
                    hasMorePages = hasMorePages,
                    onLoadNewMovie = {
                        if (viewModel.shouldShowLoadMoreButton()) {
                            viewModel.loadMoreMovies()
                        }
                    },
                    onNavigationBack = {
                        navHostController.navigate(route = Screens.LAUNCH) {
                            popUpTo(Screens.SELECTION) {
                                inclusive = true
                            }
                        }
                    },
                    onLike = { item ->
                        when (item) {
                            is SelectionItem.MovieItem -> {
                                if (roomData?.id != null && item.data?.imdbID != null && roomData != null) {
                                    viewModel.likeSelectionItem(
                                        roomId = roomData!!.id!!,
                                        likedItemId = item.data.imdbID
                                    )
                                }
                            }
                        }
                    },
                    onDislike = { item ->
                        when (item) {
                            is SelectionItem.MovieItem -> {
                                if (roomData?.id != null && item.data?.imdbID != null && roomData != null) {
                                    viewModel.dislikeSelectionItem(
                                        roomId = roomData!!.id!!,
                                        likedItemId = item.data.imdbID
                                    )
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun CountDownContent(
    onFinished: (() -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WePickTheme.colors.background),
        contentAlignment = Alignment.Center
    ) {
        val composition by rememberLottieComposition {
            LottieCompositionSpec.JsonString(
                Res.readBytes("files/countdown.json").decodeToString()
            )
        }
        val progress by animateLottieCompositionAsState(
            composition = composition,
            iterations = 1,
            isPlaying = true,
            speed = 1.0f,
            restartOnPlay = false
        )

        LaunchedEffect(progress) {
            if (progress >= 1f) {
                onFinished?.invoke()
            }
        }

        Image(
            painter = rememberLottiePainter(
                composition = composition,
                iterations = 1,
            ),
            modifier = Modifier.size(250.dp),
            contentDescription = "Lottie animation"
        )
    }
}

@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WePickTheme.colors.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                color = WePickTheme.colors.primary,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Text(
                text = "Filmler yükleniyor...",
                style = MaterialTheme.typography.bodyLarge,
                color = WePickTheme.colors.onBackground
            )
        }
    }
}

@Composable
private fun EmptyContent(
    onRefresh: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WePickTheme.colors.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Henüz film bulunamadı",
                style = MaterialTheme.typography.headlineMedium,
                color = WePickTheme.colors.onBackground,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Text(
                text = "Yeni filmler için yenile butonuna basın",
                style = MaterialTheme.typography.bodyMedium,
                color = WePickTheme.colors.onBackground.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.padding(16.dp))
            IconButton(
                onClick = onRefresh,
                colors = IconButtonDefaults.iconButtonColors().copy(
                    containerColor = WePickTheme.colors.primary,
                    contentColor = WePickTheme.colors.onPrimary
                )
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_rotate_right),
                    contentDescription = "Refresh",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ContentUI(
    items: List<SelectionItem>,
    isLoadingMore: Boolean,
    hasMorePages: Boolean,
    onLoadNewMovie: () -> Unit,
    onNavigationBack: () -> Unit,
    onLike: (SelectionItem) -> Unit,
    onDislike: (SelectionItem) -> Unit,
) {
    var currentIndex by remember { mutableIntStateOf(0) }
    var triggerSwipe by remember { mutableStateOf<SwipeDirection?>(null) }

    LaunchedEffect(currentIndex, items.size, hasMorePages) {
        if (currentIndex >= items.size - 5 && hasMorePages && !isLoadingMore) {
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
                title = {},
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
                when {
                    currentIndex < items.size -> {
                        // Show next card in background if available
                        if (currentIndex + 1 < items.size) {
                            SelectionItemCard(item = items[currentIndex + 1])
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
                                    SelectionItemCard(item = items[currentIndex])
                                }
                            )
                        }
                    }

                    hasMorePages && isLoadingMore -> {
                        // Show loading while fetching more (only if no items left)
                        if (items.isEmpty()) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CircularProgressIndicator(
                                    color = WePickTheme.colors.primary,
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.padding(8.dp))
                                Text(
                                    text = "Yeni filmler yükleniyor...",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = WePickTheme.colors.onBackground,
                                    textAlign = TextAlign.Center
                                )
                            }
                        } else {
                            // Show end message while background loading
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Filmler hazırlanıyor...",
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = WePickTheme.colors.onBackground,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }

                    else -> {
                        // No more items available
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Tüm filmler gösterildi!",
                                style = MaterialTheme.typography.headlineMedium,
                                color = WePickTheme.colors.onBackground,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.padding(8.dp))
                            Text(
                                text = "Yeni filmler için yenile butonuna basabilirsiniz",
                                style = MaterialTheme.typography.bodyMedium,
                                color = WePickTheme.colors.onBackground.copy(alpha = 0.7f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SelectionItemCard(item: SelectionItem) {
    when (item) {
        is SelectionItem.MovieItem -> {
            item.data?.let { MovieSwipeCard(movie = it) }
        }
    }
}

enum class SwipeDirection {
    LEFT, RIGHT
}