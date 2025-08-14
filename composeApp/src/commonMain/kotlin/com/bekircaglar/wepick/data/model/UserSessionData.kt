package com.bekircaglar.wepick.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UserSessionData(
    val id: String? = null,
    val nickname: String? = null,
    val emoji: String? = null
)
