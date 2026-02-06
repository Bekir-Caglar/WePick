package com.bekircaglar.wepick.presentation.screens.results


import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material3.Text
import androidx.compose.ui.draw.shadow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush.Companion.verticalGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.bekircaglar.wepick.domain.model.Movie
import com.bekircaglar.wepick.navigation.RoomCode
import com.bekircaglar.wepick.presentation.screens.selectionscreen.SelectionItem
import com.bekircaglar.wepick.presentation.screens.selectionscreen.SelectionViewModel
import com.bekircaglar.wepick.theme.WePickTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import wepick.composeapp.generated.resources.netflix_logo
import wepick.composeapp.generated.resources.disney_logo
import wepick.composeapp.generated.resources.prime_logo
import wepick.composeapp.generated.resources.apple_tv_logo
import wepick.composeapp.generated.resources.hbo_logo
import wepick.composeapp.generated.resources.blu_tv_logo
import wepick.composeapp.generated.resources.gain_logo
import wepick.composeapp.generated.resources.exxen_logo
import wepick.composeapp.generated.resources.mubi_logo
import wepick.composeapp.generated.resources.paramount_logo
import wepick.composeapp.generated.resources.youtube_logo
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import androidx.compose.foundation.Image
import org.koin.compose.viewmodel.koinViewModel
import wepick.composeapp.generated.resources.Res
import wepick.composeapp.generated.resources.actors
import wepick.composeapp.generated.resources.director
import wepick.composeapp.generated.resources.plot
import wepick.composeapp.generated.resources.results
import wepick.composeapp.generated.resources.return_room


@Composable
fun ResultScreen(navHostController: NavHostController) {
    val viewModel: SelectionViewModel = koinViewModel()
    val selectionItem = viewModel.selectedItem.value
    val roomData by viewModel.roomData.collectAsStateWithLifecycle()


    LaunchedEffect(Unit) {
        viewModel.resetRoom()
    }

    when (selectionItem) {
        is SelectionItem.MovieItem -> {
            val movie = selectionItem.movie
            movie?.let {
                MovieResult(
                    movie = it,
                    onBackRoom = {
                        roomData?.roomCode?.let { roomCode ->
                            navHostController.navigate(RoomCode(roomCode = roomCode))
                        }
                    }
                )
            }
        }

        else -> {

        }
    }
}


@Composable
fun MovieResult(
    movie: Movie,
    onBackRoom: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        AsyncImage(
            model = movie.poster,
            contentDescription = "${movie.title} Poster",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .blur(24.dp)
                .align(Alignment.TopCenter)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.3f)
                .align(Alignment.TopCenter)
                .background(
                    brush = verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.8f),
                            Color.Black.copy(alpha = 0.6f),
                            Color.Black.copy(alpha = 0.4f),
                            Color.Black.copy(alpha = 0.2f),
                            Color.Black.copy(0f)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 80.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // 1. The White Background Container (Full Height)
                        Box(
                            modifier = Modifier
                                .padding(top = 140.dp)
                                .padding(horizontal = 16.dp)
                                .fillMaxSize() // Use fillMaxSize to occupy the rest of the screen
                                .background(
                                    color = WePickTheme.colors.background,
                                    shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
                                )
                        ) {
                            // Content inside White Card
                            Column(
                                modifier = Modifier
                                    .fillMaxSize(), // Fill the white card
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // Spacer for poster overlap (Fixed space)
                                Spacer(modifier = Modifier.height(160.dp))

                                // Title (Fixed)
                                Text(
                                    text = movie.title,
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    color = WePickTheme.colors.onBackground,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 32.dp)
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                // Scrollable Area using Column with verticalScroll
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f) // Take remaining space
                                        .verticalScroll(rememberScrollState())
                                        .padding(horizontal = 32.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    FlowRow(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        movie.genre.split(",").take(4).forEach { genre ->
                                            MovieChip(text = genre.trim())
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(16.dp))

                                    Text(
                                        text = stringResource(Res.string.plot),
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.SemiBold
                                        ),
                                        color = WePickTheme.colors.onBackground,
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Start
                                    )

                                    Text(
                                        text = movie.plot,
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Start,
                                        color = WePickTheme.colors.onBackground.copy(alpha = 0.8f),
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))

                                    Text(
                                        text = stringResource(Res.string.director),
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.SemiBold
                                        ),
                                        color = WePickTheme.colors.onBackground,
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Start
                                    )

                                    Text(
                                        text = movie.director,
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Start,
                                        color = WePickTheme.colors.onBackground.copy(alpha = 0.8f),
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))

                                    Text(
                                        text = stringResource(Res.string.actors),
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.SemiBold
                                        ),
                                        color = WePickTheme.colors.onBackground,
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Start
                                    )

                                    Text(
                                        text = movie.actors,
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Start,
                                        color = WePickTheme.colors.onBackground.copy(alpha = 0.8f),
                                    )
                                    
                                    if (movie.platforms.isNotEmpty()) {
                                        Spacer(modifier = Modifier.height(16.dp))

                                        // Title for platforms, optional
                                        Text(
                                            text = "İzlencek yer",
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = WePickTheme.colors.primary
                                            ),
                                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                                            textAlign = TextAlign.Start
                                        )

                                        FlowRow(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            verticalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            movie.platforms.forEach { platform ->
                                                // Option 1: White with Logo
                                                PlatformChipWithLogo(platformName = platform)

                                                // Option 2: Full Color (Commented out)
                                                // PlatformChipFull(platformName = platform)
                                            }
                                        }
                                    }

                                    // Space for the sticky button
                                    Spacer(modifier = Modifier.height(100.dp))
                                }
                            }

                            // Button pinned to bottom of White Card
                            Button(
                                onClick = onBackRoom,
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp)
                                    .padding(bottom = 32.dp)
                                    .height(48.dp),
                                shape = RoundedCornerShape(12.dp),
                                elevation = ButtonDefaults.elevatedButtonElevation(
                                    defaultElevation = 3.dp,
                                    pressedElevation = 0.dp,
                                    hoveredElevation = 0.dp,
                                    focusedElevation = 0.dp
                                ),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = WePickTheme.colors.primary,
                                    contentColor = Color.White,
                                    disabledContentColor = Color.White.copy(alpha = 0.7f),
                                    disabledContainerColor = WePickTheme.colors.primaryVariant.copy(
                                        alpha = 0.2f
                                    )
                                )
                            ) {
                                Text(
                                    text = stringResource(Res.string.return_room),
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                            }
                        }

                        // 2. Poster (Overlapping Top)
                        Box(
                            contentAlignment = Alignment.TopCenter,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        color = Color.White,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .width(200.dp)
                                    .aspectRatio(0.7f)
                                    .shadow(8.dp, RoundedCornerShape(12.dp))
                            ) {
                                AsyncImage(
                                    model = movie.poster,
                                    contentDescription = "${movie.title} Poster",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(12.dp))
                                )

                                Row(
                                    modifier = Modifier
                                        .padding(all = 4.dp)
                                        .align(Alignment.BottomStart)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(
                                            color = Color(0xFFF5C518),
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .height(24.dp)
                                        .padding(horizontal = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = "IMDb Star",
                                        modifier = Modifier.size(18.dp),
                                        tint = Color.Black
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = movie.imdbRating,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black
                                    )
                                }

                                Row(
                                    modifier = Modifier
                                        .padding(all = 4.dp)
                                        .align(Alignment.BottomEnd)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(
                                            color = WePickTheme.colors.lightPrimaryVariant,
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .height(24.dp)
                                        .padding(horizontal = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (movie.year.isNotEmpty()) {
                                        Text(
                                            text = movie.year,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = WePickTheme.colors.primary
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Box(
                                            modifier = Modifier
                                                .size(4.dp)
                                                .background(WePickTheme.colors.primary, RoundedCornerShape(50))
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                    }
                                    
                                    Text(
                                        text = movie.runtime,
                                        fontSize = 14.sp,
                                        color = WePickTheme.colors.primary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MovieChip(text: String, modifier: Modifier = Modifier) {
    val isDarkTheme = isSystemInDarkTheme()
    Box(
        modifier = modifier
            .background(
                color = WePickTheme.colors.primaryVariant.copy(0.4f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = if (isDarkTheme) WePickTheme.colors.lightPrimaryVariant else WePickTheme.colors.primary,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun PlatformChipWithLogo(
    platformName: String,
    modifier: Modifier = Modifier,
    showLogo: Boolean = true,
    onClick: (() -> Unit)? = null
) {
    val platformInfo = getPlatformInfo(platformName)

    Row(
        modifier = modifier
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(50.dp)
            )
            .background(
                color = Color.White,
                shape = RoundedCornerShape(50.dp)
            )
            .border(
                width = 1.dp,
                color = platformInfo.backgroundColor.copy(alpha = 0.3f),
                shape = RoundedCornerShape(50.dp)
            )
            .then(
                if (onClick != null) {
                    Modifier.clickable { onClick() }
                } else {
                    Modifier
                }
            )
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Logo göster
        if (showLogo && platformInfo.logoResource != null) {
            Image(
                painter = painterResource(platformInfo.logoResource),
                contentDescription = platformName,
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Fit
            )
        }

        Text(
            text = platformName,
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            ),
            color = Color(0xFF333333)
        )
    }
}


// FlowRow implementasyonu
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FlowRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: @Composable () -> Unit
) {
    androidx.compose.foundation.layout.FlowRow(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        verticalArrangement = verticalArrangement
    ) {
        content()
    }
}

data class PlatformInfo(
    val backgroundColor: Color,
    val textColor: Color,
    val logoResource: DrawableResource?
)

fun getPlatformInfo(platformName: String): PlatformInfo {
    return when {
        platformName.contains("Netflix", ignoreCase = true) -> PlatformInfo(Color(0xFFE50914), Color.White, Res.drawable.netflix_logo)
        platformName.contains("Disney", ignoreCase = true) -> PlatformInfo(Color(0xFF113CCF), Color.White, Res.drawable.disney_logo)
        platformName.contains("Prime", ignoreCase = true) || platformName.contains("Amazon", ignoreCase = true) -> PlatformInfo(Color(0xFF00A8E1), Color.White, Res.drawable.prime_logo)
        platformName.contains("Apple", ignoreCase = true) -> PlatformInfo(Color(0xFF000000), Color.White, Res.drawable.apple_tv_logo)
        platformName.contains("HBO", ignoreCase = true) || platformName.contains("Max", ignoreCase = true) -> PlatformInfo(Color(0xFF240E3E), Color.White, Res.drawable.hbo_logo)
        platformName.contains("BluTV", ignoreCase = true) -> PlatformInfo(Color(0xFF1E88E5), Color.White, Res.drawable.blu_tv_logo)
        platformName.contains("Gain", ignoreCase = true) -> PlatformInfo(Color(0xFFFF6B00), Color.White, Res.drawable.gain_logo)
        platformName.contains("Exxen", ignoreCase = true) -> PlatformInfo(Color(0xFFFFD700), Color.Black, Res.drawable.exxen_logo)
        platformName.contains("Mubi", ignoreCase = true) -> PlatformInfo(Color(0xFF0F2646), Color.White, Res.drawable.mubi_logo)
        platformName.contains("Paramount", ignoreCase = true) -> PlatformInfo(Color(0xFF0064FF), Color.White, Res.drawable.paramount_logo)
        platformName.contains("YouTube", ignoreCase = true) -> PlatformInfo(Color(0xFFFF0000), Color.White, Res.drawable.youtube_logo)
        else -> PlatformInfo(Color.Gray, Color.White, null)
    }
}

@Preview
@Composable
fun ResultScreenPreview() {

    val sampleMovie = Movie(
        title = "The Shawshank Redemption",
        poster = "https://m.media-amazon.com/images/I/51NiGlapXlL._AC_.jpg",
        plot = "Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.",
        director = "Frank Darabont",
        actors = "Tim Robbins, Morgan Freeman, Bob Gunton",
        genre = "Drama, Crime, Thriller, Mystery, Sci-Fi",
        imdbRating = "9.3",
        year = "1994",
        runtime = "143m",
        platforms = listOf("Netflix", "Disney Plus","Prime")
    )

    MovieResult(movie = sampleMovie, {})
}