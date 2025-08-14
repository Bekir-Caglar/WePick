package com.bekircaglar.wepick.domain.usecase.category

import com.bekircaglar.wepick.domain.repository.CategoryRepository

class GetCategoryListUseCase(
    private val categoryRepository: CategoryRepository
) {

    suspend operator fun invoke() = categoryRepository.getCategories()
}