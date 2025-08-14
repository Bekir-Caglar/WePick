package com.bekircaglar.wepick.presentation.screens.categoryscreen.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bekircaglar.wepick.domain.model.CategoryModel
import com.bekircaglar.wepick.theme.WePickTheme


@Composable

fun CategoryItem(
    category: CategoryModel,
    isSelected: Boolean = false,
    onCategorySelected: (String) -> Unit
) {
    val borderModifier = if (isSelected) {
        Modifier.border(2.dp, WePickTheme.colors.primary, CardDefaults.shape)
    } else {
        Modifier
    }

    Card(
        modifier = Modifier
            .aspectRatio(4f / 3f)
            .height(100.dp)
            .then(borderModifier),
        onClick = { category.id?.let { onCategorySelected(it) } },
        colors = CardDefaults.cardColors(
            containerColor = WePickTheme.colors.primaryVariant.copy(
                0.4f
            )
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = category.emoji ?: "❓",
                fontSize = 40.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = category.categoryType?.value ?: "Unknown Category",
                style = MaterialTheme.typography.titleMedium,
                color = WePickTheme.colors.onBackground,
            )
            category.description?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = WePickTheme.colors.onBackground.copy(alpha = 0.7f),
                )
            }
        }
    }
}