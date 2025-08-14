package com.bekircaglar.wepick.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class CategoryModel(
    val id: String? = null,
    val categoryType: CategoryType? = null,
    val emoji: String? = null,
    val description: String? = null,
) {
}

@Serializable
enum class CategoryType(val value: String) {
    FOOD("Food"),
    TRAVEL("Travel"),
    SHOPPING("Shopping"),
    ENTERTAINMENT("Entertainment"),
    SPORTS("Sports"),
    TECHNOLOGY("Technology"),
    HEALTH("Health"),
    EDUCATION("Education"),
    ART("Art"),
    MUSIC("Music");

    companion object {
        fun fromValue(value: String): CategoryType? {
            return entries.find { it.value == value }
        }
    }
}