package com.bekircaglar.wepick.domain.model

import com.bekircaglar.wepick.presentation.screens.innercategory.CategoryItem
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.jetbrains.compose.resources.StringResource
import wepick.composeapp.generated.resources.Res.string
import wepick.composeapp.generated.resources.category_bbq
import wepick.composeapp.generated.resources.category_breakfast
import wepick.composeapp.generated.resources.category_chinese
import wepick.composeapp.generated.resources.category_desserts
import wepick.composeapp.generated.resources.category_fastfood
import wepick.composeapp.generated.resources.category_indian
import wepick.composeapp.generated.resources.category_italian
import wepick.composeapp.generated.resources.category_japanese
import wepick.composeapp.generated.resources.category_mediterranean
import wepick.composeapp.generated.resources.category_mexican
import wepick.composeapp.generated.resources.category_pizza
import wepick.composeapp.generated.resources.category_salads
import wepick.composeapp.generated.resources.category_seafood
import wepick.composeapp.generated.resources.category_soups
import wepick.composeapp.generated.resources.category_vegetarian


@Serializable
enum class FoodCategory(
    @Transient override val titleRes: StringResource,
    @Transient override val id: String
) : CategoryItem {
    FASTFOOD(string.category_fastfood, "fastfood"),
    ITALIAN(string.category_italian, "italian"),
    CHINESE(string.category_chinese, "chinese"),
    MEXICAN(string.category_mexican, "mexican"),
    INDIAN(string.category_indian, "indian"),
    JAPANESE(string.category_japanese, "japanese"),
    MEDITERRANEAN(string.category_mediterranean, "mediterranean"),
    VEGETARIAN(string.category_vegetarian, "vegetarian"),
    SEAFOOD(string.category_seafood, "seafood"),
    DESSERTS(string.category_desserts, "desserts"),
    BBQ(string.category_bbq, "bbq"),
    SALADS(string.category_salads, "salads"),
    SOUPS(string.category_soups, "soups"),
    BREAKFAST(string.category_breakfast, "breakfast"),
    PIZZA(string.category_pizza, "pizza"),
}