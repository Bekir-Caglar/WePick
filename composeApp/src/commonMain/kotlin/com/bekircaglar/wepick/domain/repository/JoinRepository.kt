package com.bekircaglar.wepick.domain.repository

import com.bekircaglar.wepick.utils.QueryState
import kotlinx.coroutines.flow.Flow

interface JoinRepository {

    suspend fun joinRoom(
        roomCode: String,
        userId: String,
    ): Flow<QueryState<Boolean>>
}