package com.bekircaglar.wepick.domain.repository

import com.bekircaglar.wepick.data.repository.DEFAULT_PAGE_SIZE
import com.bekircaglar.wepick.domain.model.Movie
import com.bekircaglar.wepick.utils.QueryState
import kotlinx.coroutines.flow.Flow

interface SelectionRepository {

    suspend fun getMovieList(
        subCategoriesList: List<String>,
        roomId: String,
        page: Int = 0,
        pageSize: Int = DEFAULT_PAGE_SIZE
    ): Flow<QueryState<List<Movie>>>

    suspend fun getInitialMovieList(
        subCategoriesList: List<String>,
        roomId: String
    ): Flow<QueryState<List<Movie>>>

    suspend fun loadMoreMovies(
        subCategoriesList: List<String>,
        roomId: String,
        currentPage: Int
    ): Flow<QueryState<List<Movie>>>

    suspend fun getTotalPages(
        subCategoriesList: List<String>,
        pageSize: Int = DEFAULT_PAGE_SIZE
    ): Int

    suspend fun likeSelectionItem(
        roomId: String,
        likedItemId: String
    ): Flow<QueryState<Unit>>

    suspend fun dislikeSelectionItem(
        roomId: String,
        likedItemId: String
    ): Flow<QueryState<Unit>>

    suspend fun observeMatches(
        roomId: String
    ): Flow<QueryState<String>>


}