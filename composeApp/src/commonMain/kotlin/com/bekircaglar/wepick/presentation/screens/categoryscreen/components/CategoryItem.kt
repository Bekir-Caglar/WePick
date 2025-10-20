package com.bekircaglar.wepick.presentation.screens.categoryscreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.bekircaglar.wepick.domain.model.CategoryModel
import com.bekircaglar.wepick.theme.WePickTheme

@Composable
fun CategoryItem(
    category: CategoryModel,
    isSelected: Boolean = false,
    onCategorySelected: (String) -> Unit,
    showComingSoon: Boolean = false
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
        onClick = {
            if (!showComingSoon)
                category.id?.let { onCategorySelected(it) }
        },
        colors = CardDefaults.cardColors(
            containerColor = WePickTheme.colors.primaryVariant.copy(0.4f)
        )
    ) {
        Box() {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))
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
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 2,
                        color = WePickTheme.colors.onBackground.copy(alpha = 0.7f),
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }

            }
            if (showComingSoon) {
                Column(
                    modifier = Modifier
                        .zIndex(1f)
                        .background(Color.Black.copy(alpha = 0.4f))
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(36.dp)
                            .background(WePickTheme.colors.primary.copy(alpha = 0.2f))

                    ) {
                        Text(
                            text = "Coming Soon",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                }
            }
        }

    }
}

@Composable
private fun ComingSoonRibbon(
    modifier: Modifier = Modifier,
    text: String = "COMING SOON"
) {
    // Köşe alanı: şeridin döndürülmüş halde sığması için kare bir kutu
    Box(
        modifier = modifier.size(90.dp)
    ) {
        // Diyagonal şerit
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .height(24.dp)
                .rotate(45f)
                .background(WePickTheme.colors.primary)
        ) {
            Text(
                text = text,
                color = WePickTheme.colors.onPrimary,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.align(Alignment.Center),
                maxLines = 1
            )
        }
    }
}