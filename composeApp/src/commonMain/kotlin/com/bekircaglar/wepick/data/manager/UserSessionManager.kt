package com.bekircaglar.wepick.data.manager

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.bekircaglar.wepick.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserSessionManager(private val dataStore: DataStore<Preferences>) {

    companion object {
        private val USER_ID_KEY = stringPreferencesKey("user_id")
        private val USER_NICKNAME_KEY = stringPreferencesKey("user_nickname")
        private val USER_EMOJI_KEY = stringPreferencesKey("user_emoji")
    }

    val userSession: Flow<User> = dataStore.data.map { preferences ->
        User(
            id = preferences[USER_ID_KEY],
            name = preferences[USER_NICKNAME_KEY],
            emoji = preferences[USER_EMOJI_KEY]
        )
    }

    suspend fun updateUserId(id: String?) {
        dataStore.edit { preferences ->
            if (id != null) {
                preferences[USER_ID_KEY] = id
            } else {
                preferences.remove(USER_ID_KEY)
            }
        }
    }

    suspend fun updateUserNickname(nickname: String?) {
        dataStore.edit { preferences ->
            if (nickname != null) {
                preferences[USER_NICKNAME_KEY] = nickname
            } else {
                preferences.remove(USER_NICKNAME_KEY)
            }
        }
    }

    suspend fun updateUserEmoji(emoji: String?) {
        dataStore.edit { preferences ->
            if (emoji != null) {
                preferences[USER_EMOJI_KEY] = emoji
            } else {
                preferences.remove(USER_EMOJI_KEY)
            }
        }
    }

    suspend fun updateUserSession(userSessionData: User) {
        dataStore.edit { preferences ->
            userSessionData.id?.let { preferences[USER_ID_KEY] = it } ?: preferences.remove(USER_ID_KEY)
            userSessionData.name?.let { preferences[USER_NICKNAME_KEY] = it } ?: preferences.remove(USER_NICKNAME_KEY)
            userSessionData.emoji?.let { preferences[USER_EMOJI_KEY] = it } ?: preferences.remove(USER_EMOJI_KEY)
        }
    }

    suspend fun clearUserSession() {
        dataStore.edit { preferences ->
            preferences.remove(USER_ID_KEY)
            preferences.remove(USER_NICKNAME_KEY)
            preferences.remove(USER_EMOJI_KEY)
        }
    }
}
