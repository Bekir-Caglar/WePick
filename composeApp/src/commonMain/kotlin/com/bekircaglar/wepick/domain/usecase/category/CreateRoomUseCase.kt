package com.bekircaglar.wepick.domain.usecase.category

import com.bekircaglar.wepick.data.repository.CategoryRepositoryImp
import com.bekircaglar.wepick.domain.repository.CategoryRepository

class CreateRoomUseCase(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(categoryId: String) = categoryRepository.createRoom(categoryId)
}