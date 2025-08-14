package com.bekircaglar.wepick.domain.usecase.room

class ExitRoomUseCase(
    private val roomRepository: com.bekircaglar.wepick.domain.repository.RoomRepository
) {
    suspend operator fun invoke(roomId: String, userId: String) =
        roomRepository.exitRoom(roomId, userId = userId)
}