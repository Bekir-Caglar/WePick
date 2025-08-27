package com.bekircaglar.wepick.domain.repository

import com.bekircaglar.wepick.domain.model.RoomModel
import com.bekircaglar.wepick.utils.QueryState
import kotlinx.coroutines.flow.Flow

interface RoomRepository {

    suspend fun getRoom(roomCode: String): Flow<QueryState<RoomModel>>
    suspend fun exitRoom(roomId: String, userId: String): Flow<QueryState<Boolean>>
    suspend fun observeRoomMembersOnlineStatus(roomId: String): Flow<QueryState<Unit>>
    suspend fun checkUserInRoom(roomCode: String, userId: String): Flow<QueryState<Boolean>>
    suspend fun setUserReadyStatus(roomId: String, userId: String, isReady: Boolean): Flow<QueryState<Unit>>
    suspend fun startGame(roomId: String): Flow<QueryState<Unit>>

}