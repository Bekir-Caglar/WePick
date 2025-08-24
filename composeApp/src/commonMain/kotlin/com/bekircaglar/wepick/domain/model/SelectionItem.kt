package com.bekircaglar.wepick.domain.model

data class SelectionItem(
    val id: String,
    val title: String,
    val description: String,
    val imageUrl: String? = null,
    val price: String? = null,
    val rating: Float? = null,
    val categoryId: String
)
