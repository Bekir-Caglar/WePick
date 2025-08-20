package com.bekircaglar.wepick.domain.model

import kotlinx.serialization.Serializable
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Serializable
data class RoomModel @OptIn(ExperimentalTime::class) constructor(
    val id: String? = "",
    val name: String? = "",
    val roomCategory: String? = "",
    val ownerId: String? = "",
    val members: List<String> = emptyList(),
    val readyMembers: List<String> = emptyList(),
    val roomCode: String? = "",
    val createdAt: Long = Clock.System.now().toEpochMilliseconds()
) {
}