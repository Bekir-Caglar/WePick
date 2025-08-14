package com.bekircaglar.wepick.domain.repository

import com.bekircaglar.wepick.domain.model.User
import com.bekircaglar.wepick.utils.QueryState
import kotlinx.coroutines.flow.Flow

interface LaunchRepository {
    suspend fun setUser(user: User): Flow<QueryState<Unit>>

    suspend fun getUsersByIdList(idList: List<String>): Flow<QueryState<List<User>>>

}