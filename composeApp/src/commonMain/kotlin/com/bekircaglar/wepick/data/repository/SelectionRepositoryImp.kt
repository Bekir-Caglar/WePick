package com.bekircaglar.wepick.data.repository

import com.bekircaglar.wepick.data.manager.UserSession
import com.bekircaglar.wepick.domain.model.Movie
import com.bekircaglar.wepick.domain.model.MovieDB
import com.bekircaglar.wepick.domain.model.RoomModel
import com.bekircaglar.wepick.domain.repository.SelectionRepository
import com.bekircaglar.wepick.domain.service.OmdbApiService
import com.bekircaglar.wepick.utils.QueryState
import dev.gitlive.firebase.database.DatabaseReference
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow

const val DEFAULT_PAGE_SIZE = 10
const val INITIAL_LOAD_SIZE = 10

class SelectionRepositoryImp(
    private val databaseReference: DatabaseReference,
    private val omdbApiService: OmdbApiService
) : SelectionRepository {

    private var allMoviesCache: List<MovieDB>? = null

    private suspend fun getAllMovies(): List<MovieDB> {
        if (allMoviesCache == null) {
            val movieRef = databaseReference.child("movies").valueEvents.first()
            allMoviesCache = movieRef.children.map { value ->
                value.value<MovieDB>()
            }
        }
        return allMoviesCache?.shuffled() ?: emptyList()
    }

    override suspend fun getMovieList(
        subCategoriesList: List<String>,
        roomId: String,
        page: Int,
        pageSize: Int
    ): Flow<QueryState<List<Movie>>> = flow {
        emit(QueryState.Loading)
        try {
            val moviesDB = mutableListOf<MovieDB>()
            val allMovies = getAllMovies()

            subCategoriesList.forEach { subCategory ->
                val filteredMovies = allMovies.filter { movie ->
                    movie.categories.contains(subCategory)
                }
                moviesDB.addAll(filteredMovies)
            }

            val distinctMovies = moviesDB.distinctBy { it.imdbId }
            val startIndex = page * pageSize
            val endIndex = minOf(startIndex + pageSize, distinctMovies.size)

            if (startIndex >= distinctMovies.size) {
                emit(QueryState.Success(emptyList()))
                return@flow
            }

            val paginatedMovies = distinctMovies.subList(startIndex, endIndex)

            val movies = fetchMoviesInParallel(paginatedMovies)

            emit(QueryState.Success(movies.shuffled()))
        } catch (e: Exception) {
            emit(QueryState.Error(e.message ?: "Bilinmeyen hata"))
        }
    }

    override suspend fun getInitialMovieList(
        subCategoriesList: List<String>,
        roomId: String
    ): Flow<QueryState<List<Movie>>> = flow {
        emit(QueryState.Loading)
        try {
            val moviesDB = mutableListOf<MovieDB>()
            val allMovies = getAllMovies()

            subCategoriesList.forEach { subCategory ->
                val filteredMovies = allMovies.filter { movie ->
                    movie.categories.contains(subCategory)
                }
                moviesDB.addAll(filteredMovies)
            }

            val initialMovies = moviesDB.distinctBy { it.imdbId }.take(INITIAL_LOAD_SIZE)

            val movies = fetchMoviesInParallel(initialMovies)

            emit(QueryState.Success(movies.shuffled()))
        } catch (e: Exception) {
            emit(QueryState.Error(e.message ?: "Bilinmeyen hata"))
        }
    }

    private suspend fun fetchMoviesInParallel(movieDBList: List<MovieDB>): List<Movie> =
        coroutineScope {
            val deferredMovies = movieDBList.map { movieDB ->
                async {
                    try {
                        omdbApiService.getMovieDetails(movieDB.imdbId)
                    } catch (e: Exception) {
                        null
                    }
                }
            }

            val results = deferredMovies.awaitAll()

            return@coroutineScope results.filterNotNull().distinctBy { it.imdbID }
        }


    override suspend fun observeMatches(roomId: String): Flow<QueryState<String>> = flow {
        emit(QueryState.Loading)
        try {
            val roomRef = databaseReference.child("rooms").child(roomId)
            val members =
                roomRef.child("members").valueEvents.first().children.map { it.value<String>() }
            if (members.size < 2) {
                emit(QueryState.Error(""))
                return@flow
            }
            val user1 = members[0]
            val user2 = members[1]
            val likesRef1 = databaseReference.child("likes").child(roomId).child(user1)
            val likesRef2 = databaseReference.child("likes").child(roomId).child(user2)

            // likes altında ilgili roomId'de değişiklikleri dinle
            databaseReference.child("likes").child(roomId).valueEvents.collect { _ ->
                val user1Likes =
                    likesRef1.valueEvents.firstOrNull()?.children?.map { it.value<String>() }
                        ?: emptyList()
                val user2Likes =
                    likesRef2.valueEvents.firstOrNull()?.children?.map { it.value<String>() }
                        ?: emptyList()
                val matchedId = user1Likes.intersect(user2Likes.toSet()).firstOrNull()
                if (matchedId != null) {
                    emit(QueryState.Success(matchedId))
                    return@collect
                }
            }
        } catch (e: Exception) {
            emit(QueryState.Error(e.message ?: "Bilinmeyen hata"))
        }
    }

    override suspend fun loadMoreMovies(
        subCategoriesList: List<String>,
        roomId: String,
        currentPage: Int
    ): Flow<QueryState<List<Movie>>> {
        return getMovieList(subCategoriesList, roomId, currentPage + 1)
    }

    override suspend fun getTotalPages(
        subCategoriesList: List<String>,
        pageSize: Int
    ): Int {
        return try {
            val allMovies = getAllMovies()

            val moviesDB = mutableListOf<MovieDB>()
            subCategoriesList.forEach { subCategory ->
                val filteredMovies = allMovies.filter { movie ->
                    movie.categories.contains(subCategory)
                }
                moviesDB.addAll(filteredMovies)
            }

            val totalMovies = moviesDB.distinctBy { it.imdbId }.size
            (totalMovies + pageSize - 1) / pageSize // Ceiling division
        } catch (e: Exception) {
            0
        }
    }

    override suspend fun likeSelectionItem(
        roomId: String,
        likedItemId: String
    ): Flow<QueryState<Unit>> = flow {
        emit(QueryState.Loading)
        try {
            val userId = UserSession.getId() ?: throw Exception("Kullanıcı bulunamadı")
            val roomRef = databaseReference.child("rooms").child(roomId)
            val members =
                roomRef.child("members").valueEvents.first().children.map { it.value<String>() }
            if (!members.contains(userId)) {
                throw Exception("Kullanıcı bu odanın üyesi değil")
            }

            val likesRef = databaseReference.child("likes").child(roomId).child(userId)
            val userLikesSnapshot = likesRef.valueEvents.firstOrNull()
            val userLikes =
                userLikesSnapshot?.children?.map { value -> value.value<String>() }?.toMutableList()
                    ?: mutableListOf()
            if (!userLikes.contains(likedItemId)) {
                likesRef.setValue(userLikes + likedItemId)
            }
            emit(QueryState.Success(Unit))
        } catch (e: Exception) {
            emit(QueryState.Error(e.message ?: "Bilinmeyen hata"))
        }
    }

    override suspend fun dislikeSelectionItem(
        roomId: String,
        dislikedItemId: String
    ): Flow<QueryState<Unit>> = flow {
        emit(QueryState.Loading)
        try {
            val userId = UserSession.getId() ?: throw Exception("Kullanıcı bulunamadı")
            val roomRef = databaseReference.child("rooms").child(roomId)
            val members =
                roomRef.child("members").valueEvents.first().children.map { it.value<String>() }
            if (!members.contains(userId)) {
                throw Exception("Kullanıcı bu odanın üyesi değil")
            }

            val likesRef = databaseReference.child("likes").child(roomId).child(userId)
            val userLikesSnapshot = likesRef.valueEvents.firstOrNull()
            val userLikes =
                userLikesSnapshot?.children?.map { value -> value.value<String>() }?.toMutableList()
                    ?: mutableListOf()
            if (userLikes.contains(dislikedItemId)) {
                userLikes.remove(dislikedItemId)
                likesRef.setValue(userLikes)
            }
            emit(QueryState.Success(Unit))
        } catch (e: Exception) {
            emit(QueryState.Error(e.message ?: "Bilinmeyen hata"))
        }
    }


}