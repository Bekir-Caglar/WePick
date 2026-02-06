package com.bekircaglar.wepick.domain.service

import com.bekircaglar.wepick.domain.model.Movie
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.math.roundToInt

// TMDB Response Models
@Serializable
data class TmdbDiscoverResponse(
    val results: List<TmdbMovieResult>
)

@Serializable
data class TmdbMovieResult(
    val id: Int,
    val title: String,
    val overview: String,
    @SerialName("poster_path")
    val posterPath: String?,
    @SerialName("release_date")
    val releaseDate: String?,
    @SerialName("vote_average")
    val voteAverage: Double?
)

@Serializable
data class TmdbMovieDetail(
    val imdb_id: String?,
    val id: Int,
    val title: String,
    val overview: String,
    @SerialName("poster_path")
    val posterPath: String?,
    @SerialName("release_date")
    val releaseDate: String?,
    @SerialName("vote_average")
    val voteAverage: Double?,
    val runtime: Int?,
    val genres: List<TmdbGenre>?,
    val credits: TmdbCredits?,
    @SerialName("watch/providers")
    val watchProviders: TmdbWatchProviders?
)

@Serializable
data class TmdbGenre(
    val id: Int,
    val name: String
)

@Serializable
data class TmdbCredits(
    val cast: List<TmdbCast>?,
    val crew: List<TmdbCrew>?
)

@Serializable
data class TmdbCast(
    val name: String
)

@Serializable
data class TmdbCrew(
    val name: String,
    val job: String
)


class TmdbApiService : MovieApiService {

    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
                coerceInputValues = true
            })
        }
    }

    companion object {
        private const val BASE_URL = "https://api.themoviedb.org/3"
        // TODO: Move API KEY to secure storage or BuildConfig
        private const val API_KEY = "ae4bd1b6fce2a5648671bfc171d15ba4" // Using one from the user's previous successful script run
    }

    // Map WePick category IDs (likely uppercase or lowercase strings) to TMDB Genre IDs
    private val genreMapping = mapOf(
        "ACTION" to 28,
        "ADVENTURE" to 12,
        "ANIMATION" to 16,
        "COMEDY" to 35,
        "CRIME" to 80,
        "DOCUMENTARY" to 99,
        "DRAMA" to 18,
        "FAMILY" to 10751,
        "FANTASY" to 14,
        "HISTORY" to 36,
        "HORROR" to 27,
        "MUSIC" to 10402,
        "MYSTERY" to 9648,
        "ROMANCE" to 10749,
        "SCI_FI" to 878,
        "THRILLER" to 53,
        "WAR" to 10752,
        "WESTERN" to 37,
        // Lowercase mappings for robustness
        "action" to 28,
        "adventure" to 12,
        "animation" to 16,
        "comedy" to 35,
        "crime" to 80,
        "documentary" to 99,
        "drama" to 18,
        "family" to 10751,
        "fantasy" to 14,
        "history" to 36,
        "horror" to 27,
        "music" to 10402,
        "mystery" to 9648,
        "romance" to 10749,
        "sci_fi" to 878,
        "thriller" to 53,
        "war" to 10752,
        "western" to 37
    )

    suspend fun discoverMovies(subCategories: List<String>, page: Int = 1): List<Movie> {
        // Convert subCategories to TMDB Genre IDs
        // Use pipe (|) for OR logic (movies matching ANY of the genres). Comma (,) is for AND logic.
        val genreIds = subCategories.mapNotNull { genreMapping[it] }.joinToString("|")

        // Fetch 10 pages to get ~200 movies (TMDB returns 20 per page)
        val pagesToFetch = 10
        val startPage = (page - 1) * pagesToFetch + 1

        try {
            return kotlinx.coroutines.coroutineScope {
                // 1. Fetch multiple pages in parallel
                val initialResponses = (startPage until startPage + pagesToFetch).map { currentPage ->
                    async {
                        try {
                            httpClient.get("$BASE_URL/discover/movie") {
                                parameter("api_key", API_KEY)
                                parameter("language", "tr-TR") // Turkish language preference
                                if (genreIds.isNotEmpty()) {
                                    parameter("with_genres", genreIds)
                                }
                                parameter("page", currentPage)
                                parameter("sort_by", "popularity.desc")
                            }.body<TmdbDiscoverResponse>()
                        } catch (e: Exception) {
                            null
                        }
                    }
                }.awaitAll().filterNotNull().flatMap { it.results }

                // 2. Fetch details for all collected movies in parallel
                initialResponses.map { result ->
                    async { fetchMovieDetails(result.id) }
                }.awaitAll().shuffled()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return emptyList()
        }
    }

    private suspend fun fetchMovieDetails(tmdbId: Int): Movie {
        return try {
            val detail: TmdbMovieDetail = httpClient.get("$BASE_URL/movie/$tmdbId") {
                parameter("api_key", API_KEY)
                parameter("language", "tr-TR")
                parameter("append_to_response", "credits,external_ids,watch/providers")
            }.body()

            Movie(
                title = detail.title,
                imdbID = detail.imdb_id ?: detail.id.toString(), // Fallback to TMDB ID if IMDB ID missing
                plot = detail.overview,
                poster = "https://image.tmdb.org/t/p/w500${detail.posterPath}",
                year = detail.releaseDate?.take(4) ?: "",
                imdbRating = detail.voteAverage?.let { ((it * 10.0).roundToInt() / 10.0).toString() } ?: "",
                actors = detail.credits?.cast?.take(4)?.joinToString(", ") { it.name } ?: "",
                director = detail.credits?.crew?.find { it.job == "Director" }?.name ?: "",
                genre = detail.genres?.joinToString(", ") { it.name } ?: "",
                runtime = detail.runtime?.let { "$it dk" } ?: "",
                response = "True",
                platforms = detail.watchProviders?.results?.get("TR")?.flatrate?.mapNotNull { it.provider_name } ?: emptyList()
            )
        } catch (e: Exception) {
            Movie()
        }
    }

    override suspend fun getMovieDetails(imdbId: String): Movie {
        // This was for OMDb, maybe not needed primarily anymore, but good to have.
        // If we need to fetch by IMDB ID via TMDB:
        // /find/{external_id}?external_source=imdb_id
        return Movie() // Placeholder or implement find
    }
}

@Serializable
data class TmdbWatchProviders(
    val results: Map<String, TmdbProviderCountry>? = null
)

@Serializable
data class TmdbProviderCountry(
    val link: String? = null,
    val flatrate: List<TmdbProviderItem>? = null,
    val rent: List<TmdbProviderItem>? = null,
    val buy: List<TmdbProviderItem>? = null
)

@Serializable
data class TmdbProviderItem(
    val provider_id: Int? = null,
    val provider_name: String? = null,
    val logo_path: String? = null
)
