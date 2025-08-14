package com.bekircaglar.wepick.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String? = "",
    val name: String? = "",
    val emoji: String? = "",
)