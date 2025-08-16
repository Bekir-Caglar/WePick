package com.bekircaglar.wepick

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.bekircaglar.wepick.data.repository.FirebaseStatusRepository
import com.bekircaglar.wepick.domain.model.User
import com.bekircaglar.wepick.utils.UserStatus
import com.bekircaglar.wepick.utils.UserStatusManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class AndroidUserStatusManager(
    private val repository: FirebaseStatusRepository
) : UserStatusManager, DefaultLifecycleObserver {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var heartbeatJob: Job? = null
    private var currentUser: User? = null
    private var isInForeground = true

    private val heartbeatInterval = 30_000L // 30 seconds
    private val awayThreshold = 300_000L // 5 minutes

    override suspend fun initialize(user: User) {
        currentUser = user

        // Register lifecycle observer
        ProcessLifecycleOwner.Companion.get().lifecycle.addObserver(this)

        // Setup disconnection handler
        repository.setupDisconnectionHandler(user)

        // Set initial online status
        setStatus(UserStatus.ONLINE)
        startHeartbeat()
    }

    override suspend fun setStatus(status: UserStatus) {
        currentUser?.let { user ->
            val presence = user.copy(
                status = status,
                lastSeen = Clock.System.now().toEpochMilliseconds(),
                isActive = status == UserStatus.ONLINE
            )
            user.id?.let { repository.updateUserStatus(it, presence) }
        }
    }

    override suspend fun startHeartbeat() {
        heartbeatJob?.cancel()
        heartbeatJob = scope.launch {
            while (isActive && isInForeground) {
                currentUser?.let { user ->
                    val presence = user.copy(
                        status = UserStatus.ONLINE,
                        lastSeen = Clock.System.now().toEpochMilliseconds(),
                        isActive = true
                    )
                    user.id?.let { repository.updateUserStatus(it, presence) }
                }
                delay(heartbeatInterval)
            }
        }
    }

    override suspend fun stopHeartbeat() {
        heartbeatJob?.cancel()
        heartbeatJob = null
    }

    override suspend fun cleanup() {
        stopHeartbeat()
        currentUser?.let { user ->
            user.id?.let { repository.removeUserStatus(it) }
        }
        ProcessLifecycleOwner.Companion.get().lifecycle.removeObserver(this)
        scope.cancel()
    }

    override fun observeUserStatus(userId: String, onStatusChanged: (User?) -> Unit) {
        scope.launch {
            repository.observeUserStatus(userId).collect { presence ->
                onStatusChanged(presence)
            }
        }
    }

    override fun observeAllUsers(onUsersChanged: (Map<String, User>) -> Unit) {
        scope.launch {
            repository.observeAllUsers().collect { users ->
                onUsersChanged(users)
            }
        }
    }

    // Lifecycle callbacks
    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        isInForeground = true
        scope.launch {
            setStatus(UserStatus.ONLINE)
            startHeartbeat()
        }
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        isInForeground = false
        scope.launch {
            stopHeartbeat()
            setStatus(UserStatus.AWAY)
        }
    }
}