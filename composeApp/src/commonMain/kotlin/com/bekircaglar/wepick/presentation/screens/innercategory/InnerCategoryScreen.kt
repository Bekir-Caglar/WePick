package com.bekircaglar.wepick.presentation.screens.innercategory

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.bekircaglar.wepick.domain.model.CategoryType
import com.bekircaglar.wepick.domain.model.FoodCategory
import com.bekircaglar.wepick.domain.model.MovieCategory
import com.bekircaglar.wepick.navigation.RoomCode
import com.bekircaglar.wepick.presentation.screens.categoryscreen.CategoryViewModel
import com.bekircaglar.wepick.theme.WePickTheme
import com.bekircaglar.wepick.utils.data
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import wepick.composeapp.generated.resources.Res
import wepick.composeapp.generated.resources.continue_text
import wepick.composeapp.generated.resources.ic_arrow_left
import wepick.composeapp.generated.resources.ic_info
import wepick.composeapp.generated.resources.inner_category_info
import wepick.composeapp.generated.resources.logo
import wepick.composeapp.generated.resources.no_category
import wepick.composeapp.generated.resources.select_your_favorite_categories

interface CategoryItem {
    val titleRes: StringResource
    val id: String
}

interface CategoryProvider<T : CategoryItem> {
    fun getCategories(): List<T>
    fun getCategoryType(): CategoryType
}

class MovieCategoryProvider : CategoryProvider<MovieCategory> {
    override fun getCategories(): List<MovieCategory> = MovieCategory.entries
    override fun getCategoryType(): CategoryType = CategoryType.MOVIE
}

class FoodCategoryProvider : CategoryProvider<FoodCategory> {
    override fun getCategories(): List<FoodCategory> = FoodCategory.entries
    override fun getCategoryType(): CategoryType = CategoryType.FOOD
}

object CategoryProviderFactory {
    fun <T : CategoryItem> getProvider(categoryType: CategoryType): CategoryProvider<T>? {
        return when (categoryType) {
            CategoryType.MOVIE -> MovieCategoryProvider() as? CategoryProvider<T>
            CategoryType.FOOD -> FoodCategoryProvider() as? CategoryProvider<T>
            else -> null
        }
    }
}

@Composable
fun InnerCategoryScreen(navHostController: NavHostController, categoryId: String) {
    val viewModel: CategoryViewModel = koinViewModel()
    val generatedRoomCode by viewModel.generatedRoomId.collectAsStateWithLifecycle()
    val categoryList by viewModel.categories.collectAsStateWithLifecycle()
    var selectedCategories by rememberSaveable { mutableStateOf(listOf<String>()) }

    val category = categoryList.find { it.id == categoryId }

    val categoryItems = remember(category?.categoryType) {
        when (category?.categoryType) {
            CategoryType.MOVIE -> {
                val provider =
                    CategoryProviderFactory.getProvider<MovieCategory>(CategoryType.MOVIE)
                provider?.getCategories() ?: emptyList()
            }

            CategoryType.FOOD -> {
                val provider = CategoryProviderFactory.getProvider<FoodCategory>(CategoryType.FOOD)
                provider?.getCategories() ?: emptyList()
            }

            else -> emptyList<CategoryItem>()
        }
    }

    LaunchedEffect(generatedRoomCode) {
        if (generatedRoomCode?.data != null) {
            navHostController.navigate(RoomCode(generatedRoomCode?.data!!))
            viewModel.clearGeneratedRoomCode()
        }
    }

    ContentUI(
        categoryItems = categoryItems,
        selectedCategories = selectedCategories,
        onCategorySelected = { categoryItem ->
            selectedCategories = if (selectedCategories.contains(categoryItem.id)) {
                selectedCategories - categoryItem.id
            } else {
                if (selectedCategories.size < 5) { // Updated max to 5
                    selectedCategories + categoryItem.id
                } else {
                    selectedCategories
                }
            }
        },
        onBackClick = {
            navHostController.popBackStack()
        },
        onContinueClick = {
            viewModel.createRoom(categoryId, selectedCategories)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ContentUI(
    categoryItems: List<CategoryItem>,
    selectedCategories: List<String>,
    onCategorySelected: (CategoryItem) -> Unit,
    onBackClick: () -> Unit = {},
    onContinueClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Scaffold(
        containerColor = WePickTheme.colors.background,
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
                        modifier = Modifier.size(32.dp),
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_arrow_left),
                            contentDescription = "menu",
                            modifier = Modifier.size(20.dp),
                            tint = WePickTheme.colors.onBackground,
                        )
                    }
                },
            )
        },
        bottomBar = {
            var pressed by remember { mutableStateOf(false) }
            val scale by animateFloatAsState(
                targetValue = if (pressed) 0.95f else 1f,
                label = "buttonScale"
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = WePickTheme.colors.background)
                    .navigationBarsPadding()
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Button(
                    onClick = onContinueClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                        }
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
                    ),
                    enabled = selectedCategories.isNotEmpty(), // Min 1 selected
                ) {
                    Text(text = stringResource(Res.string.continue_text), fontSize = 16.sp)
                }
            }
        }
    ) { padding ->
        if (categoryItems.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(color = WePickTheme.colors.background),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = stringResource(Res.string.select_your_favorite_categories),
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    color = WePickTheme.colors.onBackground,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_info),
                        contentDescription = "Info",
                        tint = WePickTheme.colors.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(Res.string.inner_category_info),
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                FlowRow(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(
                        16.dp,
                        Alignment.CenterHorizontally
                    ),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categoryItems.forEach { categoryItem ->
                        CategoryChip(
                            categoryItem = categoryItem,
                            isSelected = selectedCategories.contains(categoryItem.id),
                            onClick = { onCategorySelected(categoryItem) }
                        )
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(WePickTheme.colors.background),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(Res.string.no_category),
                    color = WePickTheme.colors.onBackground
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryChip(
    categoryItem: CategoryItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.95f else 1f,
        label = "chipScale"
    )

    Card(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        border = if (isSelected) null else BorderStroke(
            1.dp,
            WePickTheme.colors.onBackground.copy(alpha = 0.12f)
        ),
        shape = MaterialTheme.shapes.medium,
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) WePickTheme.colors.primary else WePickTheme.colors.primaryVariant.copy(
                0.4f
            ),
            contentColor = if (isSelected) Color.White else WePickTheme.colors.onBackground
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        interactionSource = remember {
            object : MutableInteractionSource {
                private val delegate = MutableInteractionSource()
                override val interactions = delegate.interactions
                override suspend fun emit(interaction: Interaction) {
                    if (interaction is PressInteraction.Press) pressed = true
                    if (interaction is PressInteraction.Release || interaction is PressInteraction.Cancel) pressed =
                        false
                    delegate.emit(interaction)
                }

                override fun tryEmit(interaction: Interaction) = delegate.tryEmit(interaction)
            }
        }
    ) {
        Text(
            text = stringResource(categoryItem.titleRes),
            fontSize = 16.sp,
            modifier = Modifier.padding(horizontal = 24.dp).padding(vertical = 8.dp)
        )
    }
}

@Preview()
@Composable
fun CategoryChipsSectionPreview() {
    var selectedCategories by remember { mutableStateOf(listOf<String>()) }
    val categoryItems = MovieCategory.entries

    WePickTheme(darkTheme = true) {
        ContentUI(
            categoryItems = categoryItems,
            selectedCategories = selectedCategories,
            onCategorySelected = { categoryItem ->
                selectedCategories = if (selectedCategories.contains(categoryItem.id)) {
                    selectedCategories - categoryItem.id
                } else {
                    selectedCategories + categoryItem.id
                }
            }
        )
    }
}