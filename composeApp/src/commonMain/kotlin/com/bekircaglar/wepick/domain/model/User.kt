package com.bekircaglar.wepick.domain.model

import com.bekircaglar.wepick.utils.UserStatus
import kotlinx.serialization.Serializable
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Serializable
data class User @OptIn(ExperimentalTime::class) constructor(
    val id: String? = "",
    val name: String? = "",
    val emoji: String? = "",
    val status: UserStatus? = UserStatus.ONLINE,
    val lastSeen: Long = Clock.System.now().toEpochMilliseconds(),
    val isActive: Boolean = true
)