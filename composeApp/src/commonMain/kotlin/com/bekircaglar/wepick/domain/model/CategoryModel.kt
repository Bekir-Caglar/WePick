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

object categoryList {
    val categories = listOf(
        CategoryModel(
            id = "4",
            categoryType = CategoryType.MOVIE,
            emoji = "\uD83C\uDFAC",
            description = "Movies, TV shows, and more"
        )
    )
}

@Serializable
enum class CategoryType(val value: String) {
    FOOD("Food"),
    TRAVEL("Travel"),
    SHOPPING("Shopping"),
    MOVIE("Movie"),
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