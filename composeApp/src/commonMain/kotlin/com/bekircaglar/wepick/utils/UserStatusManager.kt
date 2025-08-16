package com.bekircaglar.wepick.utils

import com.bekircaglar.wepick.domain.model.User

interface UserStatusManager {
    suspend fun initialize(user: User)
    suspend fun setStatus(status: UserStatus)
    suspend fun startHeartbeat()
    suspend fun stopHeartbeat()
    suspend fun cleanup()
    fun observeUserStatus(userId: String, onStatusChanged: (User?) -> Unit)
    fun observeAllUsers(onUsersChanged: (Map<String, User>) -> Unit)
}