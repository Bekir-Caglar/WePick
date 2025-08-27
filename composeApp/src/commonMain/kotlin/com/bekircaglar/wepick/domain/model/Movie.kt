package com.bekircaglar.wepick.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Movie(
    @SerialName("Actors")
    val actors: String = "",
    @SerialName("Awards")
    val awards: String = "",
    @SerialName("BoxOffice")
    val boxOffice: String = "",
    @SerialName("Country")
    val country: String = "",
    @SerialName("DVD")
    val dVD: String = "",
    @SerialName("Director")
    val director: String = "",
    @SerialName("Genre")
    val genre: String = "",
    @SerialName("imdbID")
    val imdbID: String = "",
    @SerialName("imdbRating")
    val imdbRating: String = "",
    @SerialName("imdbVotes")
    val imdbVotes: String = "",
    @SerialName("Language")
    val language: String = "",
    @SerialName("Metascore")
    val metascore: String = "",
    @SerialName("Plot")
    val plot: String = "",
    @SerialName("Poster")
    val poster: String = "",
    @SerialName("Production")
    val production: String = "",
    @SerialName("Rated")
    val rated: String = "",
    @SerialName("Ratings")
    val ratings: List<Rating> = listOf(),
    @SerialName("Released")
    val released: String = "",
    @SerialName("Response")
    val response: String = "",
    @SerialName("Runtime")
    val runtime: String = "",
    @SerialName("Title")
    val title: String = "",
    @SerialName("Type")
    val type: String = "",
    @SerialName("Website")
    val website: String = "",
    @SerialName("Writer")
    val writer: String = "",
    @SerialName("Year")
    val year: String = ""
)

object sampleMovie {
    val sampleMovies = listOf(
        Movie(
            actors = "Leonardo DiCaprio, Joseph Gordon-Levitt, Ellen Page",
            awards = "Oscar Adayı: En İyi Film, En İyi Senaryo",
            boxOffice = "$829,895,144",
            country = "USA, UK",
            dVD = "07 Dec 2010",
            director = "Christopher Nolan",
            genre = "Action, Adventure, Sci-Fi",
            imdbID = "tt1375666",
            imdbRating = "8.8",
            imdbVotes = "2,200,000",
            language = "English, Japanese, French",
            metascore = "74",
            plot = "A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea.",
            poster = "https://image.tmdb.org/t/p/w500/edv5CZvWj09upOsy2Y6IwDhK8bt.jpg",
            production = "Warner Bros.",
            rated = "PG-13",
            ratings = listOf(Rating("Internet Movie Database", "8.8/10")),
            released = "16 Jul 2010",
            response = "True",
            runtime = "148 min",
            title = "Inception",
            type = "movie",
            website = "N/A",
            writer = "Christopher Nolan",
            year = "2010"
        ),
        Movie(
            actors = "Keanu Reeves, Laurence Fishburne, Carrie-Anne Moss",
            awards = "4 Oscar Kazandı",
            boxOffice = "$463,517,383",
            country = "USA",
            dVD = "21 Sep 1999",
            director = "Lana Wachowski, Lilly Wachowski",
            genre = "Action, Sci-Fi",
            imdbID = "tt0133093",
            imdbRating = "8.7",
            imdbVotes = "1,900,000",
            language = "English",
            metascore = "73",
            plot = "A computer hacker learns about the true nature of his reality and his role in the war against its controllers.",
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
        ),
        Movie(
            actors = "Tom Hanks, Robin Wright, Gary Sinise",
            awards = "6 Oscar Kazandı",
            boxOffice = "$678,226,465",
            country = "USA",
            dVD = "28 Aug 2001",
            director = "Robert Zemeckis",
            genre = "Drama, Romance",
            imdbID = "tt0109830",
            imdbRating = "8.8",
            imdbVotes = "2,000,000",
            language = "English",
            metascore = "82",
            plot = "The presidencies of Kennedy and Johnson, the Vietnam War, and more through the eyes of an Alabama man.",
            poster = "https://image.tmdb.org/t/p/w500/saHP97rTPS5eLmrLQEcANmKrsFl.jpg",
            production = "Paramount Pictures",
            rated = "PG-13",
            ratings = listOf(Rating("Internet Movie Database", "8.8/10")),
            released = "06 Jul 1994",
            response = "True",
            runtime = "142 min",
            title = "Forrest Gump",
            type = "movie",
            website = "N/A",
            writer = "Winston Groom, Eric Roth",
            year = "1994"
        ),
        Movie(
            actors = "Christian Bale, Heath Ledger, Aaron Eckhart",
            awards = "2 Oscar Kazandı",
            boxOffice = "$1,005,973,645",
            country = "USA, UK",
            dVD = "09 Dec 2008",
            director = "Christopher Nolan",
            genre = "Action, Crime, Drama",
            imdbID = "tt0468569",
            imdbRating = "9.0",
            imdbVotes = "2,600,000",
            language = "English, Mandarin",
            metascore = "84",
            plot = "When the menace known as the Joker emerges, Batman must accept one of the greatest psychological and physical tests.",
            poster = "https://image.tmdb.org/t/p/w500/qJ2tW6WMUDux911r6m7haRef0WH.jpg",
            production = "Warner Bros.",
            rated = "PG-13",
            ratings = listOf(Rating("Internet Movie Database", "9.0/10")),
            released = "18 Jul 2008",
            response = "True",
            runtime = "152 min",
            title = "The Dark Knight",
            type = "movie",
            website = "N/A",
            writer = "Jonathan Nolan, Christopher Nolan",
            year = "2008"
        ),
        Movie(
            actors = "Marlon Brando, Al Pacino, James Caan",
            awards = "3 Oscar Kazandı",
            boxOffice = "$246,120,974",
            country = "USA",
            dVD = "09 Oct 2001",
            director = "Francis Ford Coppola",
            genre = "Crime, Drama",
            imdbID = "tt0068646",
            imdbRating = "9.2",
            imdbVotes = "1,800,000",
            language = "English, Italian",
            metascore = "100",
            plot = "The aging patriarch of an organized crime dynasty transfers control to his reluctant son.",
            poster = "https://image.tmdb.org/t/p/w500/3bhkrj58Vtu7enYsRolD1fZdja1.jpg",
            production = "Paramount Pictures",
            rated = "R",
            ratings = listOf(Rating("Internet Movie Database", "9.2/10")),
            released = "24 Mar 1972",
            response = "True",
            runtime = "175 min",
            title = "The Godfather",
            type = "movie",
            website = "N/A",
            writer = "Mario Puzo, Francis Ford Coppola",
            year = "1972"
        )
    )

}

@Serializable
data class Rating(
    @SerialName("Source")
    val source: String = "",
    @SerialName("Value")
    val value: String = ""
)