@file:OptIn(ExperimentalTime::class)

package com.bekircaglar.wepick

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
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSOperationQueue
import platform.UIKit.UIApplicationDidBecomeActiveNotification
import platform.UIKit.UIApplicationDidEnterBackgroundNotification
import platform.UIKit.UIApplicationWillTerminateNotification
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class IOSUserStatusManager(
    private val repository: FirebaseStatusRepository
) : UserStatusManager {

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var heartbeatJob: Job? = null
    private var currentUser: User? = null
    private var isInForeground = true

    private val heartbeatInterval = 30_000L // 30 seconds

    override suspend fun initialize(user: User) {
        currentUser = user

        // Setup app lifecycle observers
        setupLifecycleObservers()

        // Setup disconnection handler
        repository.setupDisconnectionHandler(user)

        // Set initial online status
        setStatus(UserStatus.ONLINE)
        startHeartbeat()
    }

    private fun setupLifecycleObservers() {
        // App becomes active
        NSNotificationCenter.Companion.defaultCenter.addObserverForName(
            name = UIApplicationDidBecomeActiveNotification,
            `object` = null,
            queue = NSOperationQueue.Companion.mainQueue
        ) { _ ->
            scope.launch {
                isInForeground = true
                setStatus(UserStatus.ONLINE)
                startHeartbeat()
            }
        }

        // App goes to background
        NSNotificationCenter.Companion.defaultCenter.addObserverForName(
            name = UIApplicationDidEnterBackgroundNotification,
            `object` = null,
            queue = NSOperationQueue.Companion.mainQueue
        ) { _ ->
            scope.launch {
                isInForeground = false
                stopHeartbeat()
                setStatus(UserStatus.AWAY)
            }
        }

        // App will terminate
        NSNotificationCenter.Companion.defaultCenter.addObserverForName(
            name = UIApplicationWillTerminateNotification,
            `object` = null,
            queue = NSOperationQueue.Companion.mainQueue
        ) { _ ->
            scope.launch {
                setStatus(UserStatus.OFFLINE)
            }
        }
    }

    override suspend fun setStatus(status: UserStatus) {
        currentUser?.let { user ->
            val presence = user.copy(
                status = status,
                lastSeen = Clock.System.now().toEpochMilliseconds(),
                isActive = isInForeground
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

        // Remove notification observers
        NSNotificationCenter.Companion.defaultCenter.removeObserver(this)
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
}