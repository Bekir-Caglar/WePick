package com.bekircaglar.wepick.domain.usecase.selection

import com.bekircaglar.wepick.domain.repository.SelectionRepository

class ObserveMatchUseCase(
    private val selectionRepository: SelectionRepository
) {
    suspend operator fun invoke(
        roomId: String
    ) = selectionRepository.observeMatches(
        roomId = roomId
    )
}