package com.bekircaglar.wepick.domain.usecase.room

class ObserveRoomMemersUseCase(
    private val roomRepository: com.bekircaglar.wepick.domain.repository.RoomRepository,
) {
    suspend operator fun invoke(roomId: String) =
        roomRepository.observeRoomMembersOnlineStatus(roomId)
}