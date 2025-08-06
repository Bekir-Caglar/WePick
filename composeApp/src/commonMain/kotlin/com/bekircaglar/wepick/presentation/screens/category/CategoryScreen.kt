package com.bekircaglar.wepick.presentation.screens.category

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemGesturesPadding
import androidx.compose.foundation.lazy.grid.GridCells.Adaptive
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.bekircaglar.wepick.domain.model.CategoryModel
import com.bekircaglar.wepick.navigation.Screens
import com.bekircaglar.wepick.presentation.screens.category.components.CategoryItem
import com.bekircaglar.wepick.theme.WePickTheme
import org.jetbrains.compose.resources.painterResource
import wepick.composeapp.generated.resources.Res
import wepick.composeapp.generated.resources.logo


@Composable
fun CategoryScreen(navController: NavHostController) {

    val categoryList = listOf(
        CategoryModel(id = "1", name = "Food", emoji = "🍔", description = "Delicious food options"),
        CategoryModel(id = "2", name = "Travel", emoji = "✈️", description = "Explore the world"),
        CategoryModel(id = "3", name = "Shopping", emoji = "🛍️", description = "Find great deals"),
        CategoryModel(
            id = "4",
            name = "Entertainment",
            emoji = "🎬",
            description = "Movies, music, and more"
        ),
        CategoryModel(id = "5", name = "Health", emoji = "💪", description = "Stay fit and healthy"),
        CategoryModel(id = "6", name = "Education", emoji = "📚", description = "Learn new things"),
        CategoryModel(
            id = "7",
            name = "Technology",
            emoji = "💻",
            description = "Latest tech trends"
        ),
        CategoryModel(id = "8", name = "Sports", emoji = "⚽", description = "Get active and play"),
        CategoryModel(id = "9", name = "Fashion", emoji = "👗", description = "Trendy styles"),
        CategoryModel(
            id = "10",
            name = "Home",
            emoji = "🏠",
            description = "Home improvement and decor"
        ),
        CategoryModel(id = "11", name = "Finance", emoji = "💰", description = "Manage your money"),
        CategoryModel(
            id = "12",
            name = "Lifestyle",
            emoji = "🌿",
            description = "Living your best life"
        )
    )

    var selectedCategory by remember { mutableStateOf<CategoryModel?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WePickTheme.colors.background)
            .statusBarsPadding()
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.size(16.dp))

        Image(
            painter = painterResource(Res.drawable.logo),
            contentDescription = "App Logo",
            modifier = Modifier
                .size(72.dp)
                .clip(RoundedCornerShape(16.dp))
        )

        Text(
            text = "Karar vermek istediğiniz kategoriyi seçin ve başlayın!",
            style = MaterialTheme.typography.bodyLarge,
            color = WePickTheme.colors.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 24.dp, bottom = 8.dp)
                .padding(horizontal = 16.dp)
                .align(Alignment.CenterHorizontally)
        )

        LazyVerticalGrid(
            modifier = Modifier
                .padding(16.dp)
                .weight(1f),
            columns = Adaptive(minSize = 128.dp),
            horizontalArrangement = spacedBy(16.dp),
            verticalArrangement = spacedBy(16.dp)
        ) {
            items(categoryList) { item ->
                CategoryItem(
                    category = item,
                    isSelected = selectedCategory?.id == item.id,
                    onCategorySelected = { id ->
                        selectedCategory = categoryList.find { it.id == id }
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    navController.navigate(Screens.LAUNCH) {
                        popUpTo(Screens.CATEGORY) { inclusive = true }
                    }
                },
                enabled = navController.previousBackStackEntry != null,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.elevatedButtonElevation(
                    defaultElevation = 3.dp,
                    pressedElevation = 0.dp,
                    hoveredElevation = 0.dp,
                    focusedElevation = 0.dp
                ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = WePickTheme.colors.primaryVariant,
                    contentColor = Color.White,
                    disabledContentColor = Color.White.copy(alpha = 0.7f),
                    disabledContainerColor = WePickTheme.colors.primaryVariant.copy(
                        alpha = 0.2f
                    )
                )
            ) {
                Text(
                    text = "Geri dön",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                )
            }

            Button(
                onClick = { /* Odaya Katıl */ },
                enabled = selectedCategory != null,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.elevatedButtonElevation(
                    defaultElevation = 3.dp,
                    pressedElevation = 0.dp,
                    hoveredElevation = 0.dp,
                    focusedElevation = 0.dp
                ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = WePickTheme.colors.primary,
                    contentColor = Color.White,
                    disabledContentColor = Color.White.copy(alpha = 0.7f),
                    disabledContainerColor = WePickTheme.colors.primary.copy(alpha = 0.2f)
                )
            ) {
                Text(
                    text = "Devam et",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                )
            }

        }

        Spacer(modifier = Modifier.height(16.dp))

    }


}
