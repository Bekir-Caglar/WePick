package com.bekircaglar.wepick.domain.service

import com.bekircaglar.wepick.domain.model.Movie
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class OmdbApiService() : MovieApiService {

    val httpClient = HttpClient() {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }


    companion object {
        private const val BASE_URL = "https://www.omdbapi.com/"
    }


    override suspend fun getMovieDetails(imdbId: String): Movie {
        val response: Movie = httpClient.get(BASE_URL) {
            // Sorgu parametrelerini ekleme
            url {
                parameters.append("apikey", "12864280")
                parameters.append("i", imdbId) // 'i' IMDB ID için kullanılır
                parameters.append("plot", "short") // İsteğe bağlı: 'full' veya 'short'
            }
        }.body() // Yanıt gövdesini DetailedMovie veri sınıfına çözümleme

        if (response.response == "True") {
            return response
        } else {
            // API'den gelen hata durumunu göster (örneğin ID yanlışsa)
            println("OMDB API Hata: ${response.response}")
            return Movie()
        }

    }
}