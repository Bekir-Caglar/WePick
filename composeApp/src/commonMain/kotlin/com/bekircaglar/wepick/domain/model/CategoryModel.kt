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
            id = "1",
            categoryType = CategoryType.FOOD,
            emoji = "\uD83C\uDF54",
            description = "Delicious food and recipes"
        ),
        CategoryModel(
            id = "2",
            categoryType = CategoryType.TRAVEL,
            emoji = "\uD83D\uDEEB",
            description = "Explore new places and cultures"
        ),
        CategoryModel(
            id = "3",
            categoryType = CategoryType.SHOPPING,
            emoji = "\uD83D\uDED2",
            description = "Latest trends and deals"
        ),
        CategoryModel(
            id = "4",
            categoryType = CategoryType.ENTERTAINMENT,
            emoji = "\uD83C\uDFAC",
            description = "Movies, TV shows, and more"
        ),
        CategoryModel(
            id = "5",
            categoryType = CategoryType.SPORTS,
            emoji = "\u26BD",
            description = "All about sports and fitness"
        ),
        CategoryModel(
            id = "6",
            categoryType = CategoryType.TECHNOLOGY,
            emoji = "\uD83D\uDCBB",
            description = "Gadgets, software, and innovations"
        ),
        CategoryModel(
            id = "7",
            categoryType = CategoryType.HEALTH,
            emoji = "\uD83E\uDD37",
            description = "Wellness and healthy living"
        ),
        CategoryModel(
            id = "8",
            categoryType = CategoryType.EDUCATION,
            emoji = "\uD83C\uDF93",
            description = "Learning resources and tips"
        ),
        CategoryModel(
            id = "9",
            categoryType = CategoryType.ART,
            emoji = "\uD83C\uDFA8",
            description = "Creative arts and inspiration"
        ),
        CategoryModel(
            id = "10",
            categoryType = CategoryType.MUSIC,
            emoji = "\uD83C\uDFB5",
            description = "Music genres and artists"
        )
    )
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