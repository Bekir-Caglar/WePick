package com.bekircaglar.wepick.presentation.screens.categoryscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bekircaglar.wepick.domain.model.CategoryModel
import com.bekircaglar.wepick.domain.usecase.category.CreateRoomUseCase
import com.bekircaglar.wepick.domain.usecase.category.GetCategoryListUseCase
import com.bekircaglar.wepick.utils.QueryState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CategoryViewModel(
    private val getCategoriesUseCase: GetCategoryListUseCase,
    private val createRoomUseCase: CreateRoomUseCase
) : ViewModel() {

    private val _categories = MutableStateFlow<List<CategoryModel>>(emptyList())
    val categories = _categories.asStateFlow()

    private val _generatedRoomId = MutableStateFlow<QueryState<String>?>(null)
    val generatedRoomId = _generatedRoomId.asStateFlow()

    init {
        fetchCategories()
    }

    fun clearGeneratedRoomCode() {
        _generatedRoomId.update { null }
    }

    private fun fetchCategories() = viewModelScope.launch {
        getCategoriesUseCase().collect { response ->
            _categories.update {
                when (response) {
                    is QueryState.Loading -> {
                        emptyList()
                    }

                    is QueryState.Success -> {
                        response.data
                    }

                    is QueryState.Error -> {
                        emptyList()
                    }

                    is QueryState.Idle -> {
                        emptyList()
                    }
                }
            }
        }
    }

    fun createRoom(categoryId: String) = viewModelScope.launch {
        createRoomUseCase(categoryId).collect { response ->
            _generatedRoomId.update {
                response
            }
        }
    }


}