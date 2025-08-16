package com.bekircaglar.wepick.domain.usecase.room

import com.bekircaglar.wepick.domain.repository.RoomRepository

class CheckUserInRoomUseCase(
    private val roomRepository: RoomRepository
) {
    suspend operator fun invoke(roomCode: String, userId: String) =
        roomRepository.checkUserInRoom(roomCode = roomCode, userId = userId)

}