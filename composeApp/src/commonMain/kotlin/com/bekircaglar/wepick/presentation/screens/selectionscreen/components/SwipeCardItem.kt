package com.bekircaglar.wepick.presentation.screens.selectionscreen.components

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush.Companion.verticalGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil3.compose.AsyncImage
import com.bekircaglar.wepick.domain.model.Movie
import com.bekircaglar.wepick.domain.model.Rating
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
fun MovieSwipeCard(movie: Movie) {
    var isExpanded by remember(movie) { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
        modifier = Modifier
            .fillMaxWidth(0.92f)
            .aspectRatio(0.65f)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { isExpanded = !isExpanded }
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            AsyncImage(
                model = movie.poster,
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp))
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .zIndex(1f)
                    // .height(160.dp) // Removed fixed height to allow expansion
                    .fillMaxSize() // Cover full size but gradient handles visibility
                    .align(Alignment.BottomCenter)
                    .background(
                        brush = verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0f),
                                Color.Black.copy(alpha = 0.4f), // Darker start
                                Color.Black.copy(alpha = 0.6f),
                                Color.Black.copy(alpha = 0.8f),
                                Color.Black.copy(alpha = 0.9f), // More opacity at bottom
                                Color.Black.copy(alpha = 1f)
                            )
                        ),
                        shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
                    )
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .zIndex(2f)
                    .padding(20.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = movie.title,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 2
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = movie.genre,
                        color = Color.White.copy(0.8f),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Light,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "|",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Light,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = movie.runtime,
                        color = Color.White.copy(0.8f),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Light,
                        maxLines = 1
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val maxLines by animateIntAsState(targetValue = if (isExpanded) 10 else 2, label = "maxLines")
                    
                    Text(
                        text = movie.plot,
                        color = Color.White.copy(0.9f),
                        fontSize = 14.sp,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Normal,
                        maxLines = maxLines
                    )
                }

                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val rating = movie.imdbRating.toDoubleOrNull() ?: 0.0
                    val stars = (rating / 2).toInt()

                    repeat(5) { index ->
                        val starIcon = if (index < stars) "★" else "☆"
                        Text(
                            text = starIcon,
                            color = Color(0xFFFFD700),
                            fontSize = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = movie.imdbRating,
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "(${movie.year})",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(0.8f),
                        fontWeight = FontWeight.Light,
                        fontSize = 14.sp,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun MovieSwipeCardPreview() {
    val sampleMovie = Movie(
        actors = "Keanu Reeves, Laurence Fishburne, Carrie-Anne Moss",
        awards = "4 Oscar Adayı. 1 Oscar Kazandı. 37 ödül & 51 adaylık",
        boxOffice = "$463,517,383",
        country = "USA",
        dVD = "21 Sep 1999",
        director = "Lana Wachowski, Lilly Wachowski",
        genre = "Action, Sci-Fi",
        imdbID = "tt0133093",
        imdbRating = "8.7",
        imdbVotes = "1,800,000",
        language = "English",
        metascore = "73",
        plot = "A computer hacker learns from mysterious rebels about the true nature of his reality and his role in the war against its controllers.",
        poster = "https://image.tmdb.org/t/p/w500/f89U3ADr1oiB1s9GkdPOEpXUk5H.jpg",
        production = "Warner Bros.",
        rated = "R",
        ratings = listOf(Rating("Internet Movie Database", "8.7/10")),
        released = "31 Mar 1999",
        response = "True",
        runtime = "136 min",
        title = "The Matrix",
        type = "movie",
        website = "N/A",
        writer = "Lilly Wachowski, Lana Wachowski",
        year = "1999"
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MovieSwipeCard(movie = sampleMovie)

    }
}