package com.bekircaglar.wepick.presentation.screens.selectionscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bekircaglar.wepick.domain.model.CategoryType
import com.bekircaglar.wepick.domain.model.Movie
import com.bekircaglar.wepick.domain.model.RoomModel
import com.bekircaglar.wepick.domain.model.categoryList
import com.bekircaglar.wepick.domain.usecase.room.GetRoomUseCase
import com.bekircaglar.wepick.domain.usecase.selection.ClearRoomUseCase
import com.bekircaglar.wepick.domain.usecase.selection.GetMovieListUseCase
import com.bekircaglar.wepick.domain.usecase.selection.LikeSelectionItemUseCase
import com.bekircaglar.wepick.domain.usecase.selection.ObserveMatchUseCase
import com.bekircaglar.wepick.utils.QueryState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

class SelectionViewModel(
    private val getRoomUseCase: GetRoomUseCase,
    private val getMovieListUseCase: GetMovieListUseCase,
    private val likeSelectionItemUseCase: LikeSelectionItemUseCase,
    private val observeMatchUseCase: ObserveMatchUseCase,
    private val clearRoomUseCase: ClearRoomUseCase
) : ViewModel() {

    companion object {
        private const val PAGE_SIZE = 10
    }

    // Room data
    private val _roomData = MutableStateFlow<RoomModel?>(null)
    val roomData = _roomData.asStateFlow()

    // Selection items
    private val _selectionItemList = MutableStateFlow<List<SelectionItem>>(emptyList())
    val selectionItemList = _selectionItemList.asStateFlow()

    // Loading states
    private val _isInitialLoading = MutableStateFlow(false)
    val isInitialLoading = _isInitialLoading.asStateFlow()

    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore = _isLoadingMore.asStateFlow()

    // Pagination states
    private val _currentPage = MutableStateFlow(0)
    val currentPage = _currentPage.asStateFlow()

    private val _hasMorePages = MutableStateFlow(true)
    val hasMorePages = _hasMorePages.asStateFlow()

    private val _totalPages = MutableStateFlow(0)
    val totalPages = _totalPages.asStateFlow()

    // Error state
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    private val _matchFound = MutableStateFlow<String?>(null)
    val matchFound = _matchFound.asStateFlow()

    private val _selectedItem = MutableStateFlow<SelectionItem?>(null)
    val selectedItem = _selectedItem.asStateFlow()

    private var currentCategoryType: CategoryType? = null
    private var currentSubCategories: List<String> = emptyList()


    fun likeSelectionItem(roomId: String, likedItemId: String) = viewModelScope.launch {
        likeSelectionItemUseCase(
            roomId = roomId,
            likedItemId = likedItemId
        ).collect { queryState ->
            when (queryState) {
                is QueryState.Loading -> {
                    // Handle loading state if needed
                }

                is QueryState.Success -> {
                    // Handle success state if needed
                }

                is QueryState.Error -> {
                    _errorMessage.value = queryState.message
                }

                else -> {}
            }
        }
    }

    private fun getSelectedItem() {
        _matchFound.value?.let { matchId ->
            _selectionItemList.value.firstOrNull { item ->
                item is SelectionItem.MovieItem && item.movie?.imdbID == matchId
            }?.let { selectedItem ->
                _selectedItem.value = selectedItem
            }
        }
    }

    fun resetRoom() = viewModelScope.launch {
        _roomData.value?.id?.let { roomId ->
            clearRoomUseCase(roomId = roomId).collect { queryState ->
                when (queryState) {
                    is QueryState.Loading -> {
                        // Handle loading state if needed
                    }

                    is QueryState.Success -> {
                        _matchFound.value = null
                        _selectedItem.value = null
                    }

                    is QueryState.Error -> {
                        _errorMessage.value = queryState.message
                    }

                    else -> {}
                }
            }
        }
    }

    fun observeMatches(roomId: String) = viewModelScope.launch {
        observeMatchUseCase(roomId = roomId).collect { queryState ->
            when (queryState) {
                is QueryState.Loading -> {
                    // Handle loading state if needed
                }

                is QueryState.Success -> {
                    _matchFound.value = queryState.data
                    getSelectedItem()
                }

                is QueryState.Error -> {
                    _errorMessage.value = queryState.message
                }

                else -> {}
            }
        }
    }

    fun dislikeSelectionItem(roomId: String, likedItemId: String) = viewModelScope.launch {
        likeSelectionItemUseCase.dislikeSelectionItem(
            roomId = roomId,
            likedItemId = likedItemId
        ).collect { queryState ->
            when (queryState) {
                is QueryState.Loading -> {
                    // Handle loading state if needed
                }

                is QueryState.Success -> {
                    // Handle success state if needed
                }

                is QueryState.Error -> {
                    _errorMessage.value = queryState.message
                }

                else -> {}
            }
        }
    }

    fun getRoom(roomCode: String) = viewModelScope.launch {
        getRoomUseCase(roomCode = roomCode).collect {
            when (it) {
                is QueryState.Loading -> {
                    _isInitialLoading.value = true
                }

                is QueryState.Success -> {
                    _isInitialLoading.value = false
                    _roomData.value = it.data
                    categoryList.categories.first { category ->
                        category.id == it.data.roomCategory
                    }.categoryType?.let { categoryType ->
                        currentCategoryType = categoryType
                        currentSubCategories = it.data.subCategories
                        getInitialSelectionItems(
                            categoryType = categoryType,
                            subCategoriesList = it.data.subCategories
                        )
                    }
                    it.data.id?.let { roomId ->
                        observeMatches(roomId = roomId)
                    }
                }

                is QueryState.Error -> {
                    _isInitialLoading.value = false
                    _errorMessage.value = it.message
                }

                else -> {}
            }
        }
    }

    private fun getInitialSelectionItems(
        categoryType: CategoryType,
        subCategoriesList: List<String>
    ) =
        viewModelScope.launch {
            // Reset pagination states
            _currentPage.value = 0
            _hasMorePages.value = true
            _selectionItemList.value = emptyList()
            _errorMessage.value = null

            when (categoryType) {
                CategoryType.FOOD -> {}
                CategoryType.TRAVEL -> {}
                CategoryType.SHOPPING -> {}
                CategoryType.SPORTS -> {}
                CategoryType.TECHNOLOGY -> {}
                CategoryType.HEALTH -> {}
                CategoryType.EDUCATION -> {}
                CategoryType.ART -> {}
                CategoryType.MUSIC -> {}
                CategoryType.MOVIE -> getInitialMovies(subCategoriesList)
            }
        }

    private fun getInitialMovies(subCategoriesList: List<String>) = viewModelScope.launch {
        _roomData.value?.id?.let { roomId ->
            // Get total pages first
            getTotalPagesCount(subCategoriesList)

            getMovieListUseCase.getInitialMovies(
                subCategoriesList = subCategoriesList,
                roomId = roomId
            ).collect { queryState ->
                when (queryState) {
                    is QueryState.Loading -> {
                        _isInitialLoading.value = true
                    }

                    is QueryState.Success -> {
                        _isInitialLoading.value = false
                        _selectionItemList.value = queryState.data.map { movie ->
                            SelectionItem.MovieItem(movie = movie)
                        }
                        _currentPage.value = 0
                        _hasMorePages.value = _totalPages.value > 1
                    }

                    is QueryState.Error -> {
                        _isInitialLoading.value = false
                        _errorMessage.value = queryState.message
                        _selectionItemList.value = emptyList()
                    }

                    else -> {}
                }
            }
        }
    }

    fun loadMoreMovies() = viewModelScope.launch {
        if (!_hasMorePages.value || _isLoadingMore.value) return@launch

        _roomData.value?.id?.let { roomId ->
            getMovieListUseCase.loadMoreMovies(
                subCategoriesList = currentSubCategories,
                roomId = roomId,
                currentPage = _currentPage.value
            ).collect { queryState ->
                when (queryState) {
                    is QueryState.Loading -> {
                        _isLoadingMore.value = true
                    }

                    is QueryState.Success -> {
                        _isLoadingMore.value = false
                        val newMovies = queryState.data.map { movie ->
                            SelectionItem.MovieItem(movie = movie)
                        }

                        // Add new movies to existing list
                        _selectionItemList.update { currentList ->
                            currentList + newMovies
                        }

                        _currentPage.value = _currentPage.value + 1
                        _hasMorePages.value = _currentPage.value < _totalPages.value - 1
                    }

                    is QueryState.Error -> {
                        _isLoadingMore.value = false
                        _errorMessage.value = queryState.message
                    }

                    else -> {}
                }
            }
        }
    }

    private suspend fun getTotalPagesCount(subCategoriesList: List<String>) {
        try {
            val totalPages = getMovieListUseCase.getTotalPages(
                subCategoriesList = subCategoriesList,
                pageSize = PAGE_SIZE
            )
            _totalPages.value = totalPages
        } catch (e: Exception) {
            _totalPages.value = 1
        }
    }

    fun refreshMovies() = viewModelScope.launch {
        currentCategoryType?.let { categoryType ->
            getInitialSelectionItems(
                categoryType = categoryType,
                subCategoriesList = currentSubCategories
            )
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }

    // UI Helper methods
    fun shouldShowLoadMoreButton(): Boolean {
        return _hasMorePages.value &&
                !_isInitialLoading.value &&
                !_isLoadingMore.value &&
                _selectionItemList.value.isNotEmpty()
    }

    fun getCurrentMoviesCount(): Int {
        return _selectionItemList.value.size
    }

}

@Serializable
sealed class SelectionItem {
    data class MovieItem(val movie: Movie?) : SelectionItem()
}