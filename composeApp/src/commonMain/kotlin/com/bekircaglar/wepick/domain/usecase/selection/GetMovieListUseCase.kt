package com.bekircaglar.wepick.domain.usecase.selection

import com.bekircaglar.wepick.domain.model.Movie
import com.bekircaglar.wepick.domain.repository.SelectionRepository
import com.bekircaglar.wepick.utils.QueryState
import kotlinx.coroutines.flow.Flow

class GetMovieListUseCase(
    private val selectionRepository: SelectionRepository
) {

    suspend operator fun invoke(
        subCategoriesList: List<String>,
        roomId: String,
        page: Int = 0,
        pageSize: Int = 20
    ): Flow<QueryState<List<Movie>>> =
        selectionRepository.getMovieList(
            subCategoriesList = subCategoriesList,
            roomId = roomId,
            page = page,
            pageSize = pageSize
        )

    suspend fun getInitialMovies(
        subCategoriesList: List<String>,
        roomId: String
    ): Flow<QueryState<List<Movie>>> =
        selectionRepository.getInitialMovieList(
            subCategoriesList = subCategoriesList,
            roomId = roomId
        )

    suspend fun loadMoreMovies(
        subCategoriesList: List<String>,
        roomId: String,
        currentPage: Int
    ): Flow<QueryState<List<Movie>>> =
        selectionRepository.loadMoreMovies(
            subCategoriesList = subCategoriesList,
            roomId = roomId,
            currentPage = currentPage
        )

    suspend fun getTotalPages(
        subCategoriesList: List<String>,
        pageSize: Int = 20
    ): Int =
        selectionRepository.getTotalPages(
            subCategoriesList = subCategoriesList,
            pageSize = pageSize
        )
}