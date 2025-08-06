package com.bekircaglar.wepick.domain.model

data class CategoryModel(
    val id: String,
    val name: String,
    val emoji: String,
    val description: String? = null,
) {
}