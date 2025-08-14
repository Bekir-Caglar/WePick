package com.bekircaglar.wepick.presentation.screens.categoryscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells.Adaptive
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.bekircaglar.wepick.domain.model.CategoryModel
import com.bekircaglar.wepick.navigation.RoomCode
import com.bekircaglar.wepick.presentation.screens.categoryscreen.components.CategoryItem
import com.bekircaglar.wepick.theme.WePickTheme
import com.bekircaglar.wepick.utils.data
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import wepick.composeapp.generated.resources.Res
import wepick.composeapp.generated.resources.ic_arrow_left
import wepick.composeapp.generated.resources.ic_menu
import wepick.composeapp.generated.resources.logo


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(navController: NavHostController) {

    val viewModel: CategoryViewModel = koinViewModel()
    val categoryList by viewModel.categories.collectAsStateWithLifecycle()
    var selectedCategory by remember { mutableStateOf<CategoryModel?>(null) }
    val generatedRoomCode by viewModel.generatedRoomId.collectAsStateWithLifecycle()

    LaunchedEffect(generatedRoomCode) {
        if (generatedRoomCode?.data != null ) {
            navController.navigate(RoomCode(generatedRoomCode?.data!!))
            viewModel.clearGeneratedRoomCode()
        }
    }

    CategoryUI(
        categoryList = categoryList,
        selectedCategory = selectedCategory,
        onCategorySelected = { id ->
            selectedCategory = categoryList.find { it.id == id }
        },
        onBack = { navController.popBackStack() },
        onContinue = {
            selectedCategory?.id?.let {
                viewModel.createRoom(it)
            }
        },
        canContinue = selectedCategory != null
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryUI(
    categoryList: List<CategoryModel>,
    selectedCategory: CategoryModel?,
    onCategorySelected: (String) -> Unit,
    onBack: () -> Unit,
    onContinue: () -> Unit,
    canContinue: Boolean
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = WePickTheme.colors.surface.copy(0.2f),
                    titleContentColor = WePickTheme.colors.primary,
                    navigationIconContentColor = WePickTheme.colors.primary
                ),
                title = {
                    Image(
                        painter = painterResource(Res.drawable.logo),
                        contentDescription = "WePick Logo",
                        modifier = Modifier
                            .size(32.dp),
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_arrow_left),
                            contentDescription = "menu",
                            modifier = Modifier.size(20.dp),
                            tint = WePickTheme.colors.onBackground,
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_menu),
                            contentDescription = "menu",
                            modifier = Modifier.size(20.dp),
                            tint = WePickTheme.colors.onBackground,
                        )
                    }

                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(WePickTheme.colors.background)
                .padding(it)
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Karar vermek istediğiniz kategoriyi seçin ve başlayın!",
                style = MaterialTheme.typography.bodyLarge,
                color = WePickTheme.colors.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 8.dp)
                    .padding(horizontal = 16.dp)
                    .align(Alignment.CenterHorizontally)
            )
            LazyVerticalGrid(
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f),
                columns = Adaptive(minSize = 128.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(categoryList) { item ->
                    CategoryItem(
                        category = item,
                        isSelected = selectedCategory?.id == item.id,
                        onCategorySelected = onCategorySelected
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onContinue,
                enabled = canContinue,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
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

    }
}
