package com.bekircaglar.wepick.domain.service

import com.bekircaglar.wepick.domain.model.Movie

interface MovieApiService {
    suspend fun getMovieDetails(imdbId: String): Movie
}