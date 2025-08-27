package com.bekircaglar.wepick.domain.usecase.selection

import com.bekircaglar.wepick.domain.repository.SelectionRepository

class LikeSelectionItemUseCase(
    private val selectionRepository: SelectionRepository
) {
    suspend operator fun invoke(
        roomId: String,
        likedItemId: String
    ) = selectionRepository.likeSelectionItem(
        roomId = roomId,
        likedItemId = likedItemId
    )

    suspend fun dislikeSelectionItem(
        roomId: String,
        likedItemId: String
    ) = selectionRepository.dislikeSelectionItem(
        roomId = roomId,
        likedItemId = likedItemId
    )

}