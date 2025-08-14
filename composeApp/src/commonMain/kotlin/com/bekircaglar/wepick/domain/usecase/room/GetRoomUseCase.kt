package com.bekircaglar.wepick.domain.usecase.room

class GetRoomUseCase(
    private val roomRepository: com.bekircaglar.wepick.domain.repository.RoomRepository
) {
    suspend operator fun invoke(roomCode: String) = roomRepository.getRoom(roomCode)
}