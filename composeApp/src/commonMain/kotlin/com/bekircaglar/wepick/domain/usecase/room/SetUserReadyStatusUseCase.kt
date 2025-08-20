package com.bekircaglar.wepick.domain.usecase.room

import com.bekircaglar.wepick.domain.repository.RoomRepository

class SetUserReadyStatusUseCase(
    private val roomRepository: RoomRepository,
) {
    suspend operator fun invoke(roomId: String, userId: String, isReady: Boolean) =
        roomRepository.setUserReadyStatus(roomId, userId, isReady)
}