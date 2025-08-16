package com.bekircaglar.wepick.data

import com.bekircaglar.wepick.data.datastore.createDataStore
import com.bekircaglar.wepick.data.manager.UserSessionManager
import com.bekircaglar.wepick.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

object UserSession {
    private val dataStore = createDataStore()
    private val userSessionManager = UserSessionManager(dataStore)

    val userSessionFlow: Flow<User> = userSessionManager.userSession

    suspend fun getCurrentUserSession(): User {
        return userSessionManager.userSession.first()
    }

    @OptIn(ExperimentalTime::class)
    suspend fun initializeUser(): User {
        val currentSession = getCurrentUserSession()
        if (currentSession.id.isNullOrEmpty()) {
            val newId = "${Clock.System.now().toEpochMilliseconds()}-${(0..9999).random()}"
            updateUserId(newId)
            return getCurrentUserSession()
        }
        return currentSession
    }

    suspend fun updateUserId(id: String?) {
        userSessionManager.updateUserId(id)
    }

    suspend fun updateUserNickname(nickname: String?) {
        userSessionManager.updateUserNickname(nickname)
    }

    suspend fun updateUserEmoji(emoji: String?) {
        userSessionManager.updateUserEmoji(emoji)
    }

    suspend fun updateUserSession(userSessionData: User) {
        userSessionManager.updateUserSession(userSessionData)
    }

    suspend fun updateUserSession(id: String?, nickname: String?, emoji: String?) {
        userSessionManager.updateUserSession(User(id, nickname, emoji))
    }

    suspend fun clearUserSession() {
        userSessionManager.clearUserSession()
    }

    // Backward compatibility için getter metodları
    suspend fun getId(): String? = getCurrentUserSession().id
    suspend fun getNickname(): String? = getCurrentUserSession().name
    suspend fun getEmoji(): String? = getCurrentUserSession().emoji
}

