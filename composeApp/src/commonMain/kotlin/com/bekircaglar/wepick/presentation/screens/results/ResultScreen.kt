package com.bekircaglar.wepick.presentation.screens.results

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.offset
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush.Companion.verticalGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.bekircaglar.wepick.domain.model.Movie
import com.bekircaglar.wepick.presentation.screens.selectionscreen.SelectionItem
import com.bekircaglar.wepick.presentation.screens.selectionscreen.SelectionViewModel
import com.bekircaglar.wepick.theme.WePickTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.min


@Composable
fun ResultScreen(navHostController: NavHostController) {
    val viewModel: SelectionViewModel = koinViewModel()
    val selectionItem by viewModel.selectedItem.collectAsStateWithLifecycle()

    when(selectionItem){
        is SelectionItem.MovieItem -> {
            val movie = selectionItem.let { it as SelectionItem.MovieItem }.movie
            movie?.let {
                MovieResult(it)
            }
        }
        else -> {

        }
    }
}


@Composable
fun MovieResult(movie: Movie) {
    val scrollState = rememberScrollState()
    val scrollProgress = min(scrollState.value / 500f, 1f)

    val posterScale by animateFloatAsState(
        targetValue = 1f - (scrollProgress * 0.65f),
        animationSpec = tween(durationMillis = 100)
    )

    val posterOffsetX by animateFloatAsState(
        targetValue = scrollProgress * 280f,
        animationSpec = tween(durationMillis = 100)
    )

    val posterOffsetY by animateFloatAsState(
        targetValue = scrollProgress * 100f,
        animationSpec = tween(durationMillis = 100)
    )

    val titleOffsetX by animateFloatAsState(
        targetValue = scrollProgress * -250f,
        animationSpec = tween(durationMillis = 100)
    )

    val titleOffsetY by animateFloatAsState(
        targetValue = scrollProgress * -450f,
        animationSpec = tween(durationMillis = 100)
    )

    val titleScale by animateFloatAsState(
        targetValue = 1f - (scrollProgress * 0.4f),
        animationSpec = tween(durationMillis = 100)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)
                    .statusBarsPadding()
            ) {
                Text(
                    text = "Sonuçlar",
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.White
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .fillMaxHeight(0.8f)
                        .padding(bottom = 32.dp)
                        .background(
                            color = WePickTheme.colors.background,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .align(Alignment.CenterHorizontally)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                            .offset(y = (-150).dp)
                            .padding(top = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(400.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 32.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            movie.genre.split(",").forEach { genre ->
                                MovieChip(text = genre.trim())
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Özet",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = WePickTheme.colors.onBackground,
                        )

                        Text(
                            text = movie.plot,
                            modifier = Modifier.padding(horizontal = 32.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = WePickTheme.colors.onBackground.copy(alpha = 0.8f),
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Yönetmen",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = WePickTheme.colors.onBackground,
                        )

                        Text(
                            text = movie.director,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = WePickTheme.colors.onBackground.copy(alpha = 0.8f),
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Oyuncular",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = WePickTheme.colors.onBackground,
                        )

                        Text(
                            text = movie.actors,
                            modifier = Modifier.padding(horizontal = 32.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = WePickTheme.colors.onBackground.copy(alpha = 0.8f),
                        )

                        Spacer(modifier = Modifier.height(100.dp))
                    }

                    // Poster - Fixed position with animation
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset(y = (-150).dp)
                            .graphicsLayer {
                                translationY = posterOffsetY
                                translationX = posterOffsetX
                                scaleX = posterScale
                                scaleY = posterScale
                            }
                    ) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopCenter)
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
                                Text(
                                    text = movie.runtime,
                                    fontSize = 14.sp,
                                    color = WePickTheme.colors.primary
                                )
                            }
                        }
                    }

                    Text(
                        text = movie.title,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = WePickTheme.colors.onBackground,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp)
                            .offset(y = 150.dp)
                            .graphicsLayer {
                                translationY = titleOffsetY
                                translationX = titleOffsetX
                                scaleX = titleScale
                                scaleY = titleScale
                            }
                    )

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Bottom,
                    ) {
                        Button(
                            onClick = {},
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(all = 16.dp)
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
                                text = "Kaydırmaya devam et",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
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

@Preview
@Composable
fun ResultScreenPreview() {

    val sampleMovie = Movie(
        title = "The Shawshank Redemption",
        poster = "https://m.media-amazon.com/images/I/51NiGlapXlL._AC_.jpg",
        plot = "Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.",
        director = "Frank Darabont",
        actors = "Tim Robbins, Morgan Freeman, Bob Gunton",
        genre = "Drama, Crime, Thriller, Mystery",
        imdbRating = "9.3",
        runtime = "2h 22min"
    )

    MovieResult(movie = sampleMovie)
}