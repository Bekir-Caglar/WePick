package com.bekircaglar.wepick.domain.usecase.join

class JoinRoomUseCase(
    private val joinRepository: com.bekircaglar.wepick.domain.repository.JoinRepository,
) {
    suspend operator fun invoke(
        roomCode: String,
        userId: String,
    ) = joinRepository.joinRoom(roomCode, userId)
}