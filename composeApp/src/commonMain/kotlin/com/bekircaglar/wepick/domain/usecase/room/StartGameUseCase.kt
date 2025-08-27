package com.bekircaglar.wepick.domain.usecase.room

import com.bekircaglar.wepick.domain.repository.RoomRepository

class StartGameUseCase(
    private val roomRepository: RoomRepository
) {
    suspend operator fun invoke(roomId: String) = roomRepository.startGame(roomId)

}