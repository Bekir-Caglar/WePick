package com.bekircaglar.wepick.presentation.screens.categoryscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bekircaglar.wepick.domain.model.CategoryModel
import com.bekircaglar.wepick.domain.model.categoryList
import com.bekircaglar.wepick.domain.usecase.category.CreateRoomUseCase
import com.bekircaglar.wepick.utils.QueryState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CategoryViewModel(
    private val createRoomUseCase: CreateRoomUseCase
) : ViewModel() {

    private val _categories = MutableStateFlow<List<CategoryModel>>(categoryList.categories)
    val categories = _categories.asStateFlow()

    private val _generatedRoomId = MutableStateFlow<QueryState<String>?>(null)
    val generatedRoomId = _generatedRoomId.asStateFlow()
    fun clearGeneratedRoomCode() {
        _generatedRoomId.update { null }
    }

    fun createRoom(categoryId: String, selectedSubCategories: List<String>) = viewModelScope.launch {
        val subCategories = selectedSubCategories.map { it.uppercase() }
        createRoomUseCase(categoryId,subCategories).collect { response ->
                _generatedRoomId.update {
                    response
                }
            }
        }


}