package com.bekircaglar.wepick.data.repository

import com.bekircaglar.wepick.domain.model.User
import com.bekircaglar.wepick.utils.UserStatus
import dev.gitlive.firebase.database.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class FirebaseStatusRepository(
    private val database: DatabaseReference
) {
    private val presenceRef = database.child("presence")
    
    suspend fun updateUserStatus(userId: String, presence: User) {
        try {
            presenceRef.child(userId).setValue(presence)
        } catch (e: Exception) {
            println("Error updating user status: ${e.message}")
        }
    }
    
    suspend fun removeUserStatus(userId: String) {
        try {
            presenceRef.child(userId).removeValue()
        } catch (e: Exception) {
            println("Error removing user status: ${e.message}")
        }
    }
    
    fun observeUserStatus(userId: String): Flow<User?> = flow {
        presenceRef.child(userId).valueEvents.collect { snapshot ->
            try {
                val presence = snapshot.value<User>()
                emit(presence)
            } catch (e: Exception) {
                emit(null)
            }
        }
    }
    
    fun observeAllUsers(): Flow<Map<String, User>> = flow {
        presenceRef.valueEvents.collect { snapshot ->
            try {
                val users = mutableMapOf<String, User>()
                snapshot.children.forEach { child ->
                    child.key?.let { userId ->
                        child.value<User>()?.let { presence ->
                            users[userId] = presence
                        }
                    }
                }
                emit(users)
            } catch (e: Exception) {
                emit(emptyMap())
            }
        }
    }
    
    @OptIn(ExperimentalTime::class)
    suspend fun setupDisconnectionHandler(user: User) {
        try {
            val offlinePresence = user.copy(
                status = UserStatus.OFFLINE,
                lastSeen = Clock.System.now().toEpochMilliseconds(),
                isActive = false
            )
            user.id?.let { presenceRef.child(it) }?.onDisconnect()?.setValue(offlinePresence)
        } catch (e: Exception) {
            println("Error setting up disconnection handler: ${e.message}")
        }
    }
}