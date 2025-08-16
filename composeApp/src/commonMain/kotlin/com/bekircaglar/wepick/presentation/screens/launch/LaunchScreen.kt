package com.bekircaglar.wepick.presentation.screens.launch

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.bekircaglar.wepick.data.UserSession
import com.bekircaglar.wepick.domain.model.User
import com.bekircaglar.wepick.navigation.Screens
import com.bekircaglar.wepick.theme.WePickTheme
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import wepick.composeapp.generated.resources.Res
import wepick.composeapp.generated.resources.header
import wepick.composeapp.generated.resources.ic_rotate_right
import wepick.composeapp.generated.resources.logo
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
fun LaunchScreen(navController: NavHostController) {
    val viewModel: LaunchViewModel = koinViewModel()
    val scope = rememberCoroutineScope()

    // UserSession flow'unu collect ediyoruz
    val userSessionState by UserSession.userSessionFlow.collectAsState(
        initial = User()
    )

    val animalEmojis = listOf(
        "🐱",
        "🐶",
        "🦊",
        "🐻",
        "🐼",
        "🐸",
        "🦁",
        "🐵",
        "🐰",
        "🐨",
        "🐯",
        "🦒",
        "🐘",
        "🦏",
        "🐺",
        "🐮",
        "🐷",
        "🐭",
        "🐹",
        "🐒",
        "🦝",
        "🦘",
        "🐧",
        "🦅",
        "🐦",
        "🦆",
        "🦉",
        "🐟",
        "🐠",
        "🐡",
        "🦈",
        "🐙",
        "🦀",
        "🦞",
        "🐛",
        "🦋",
        "🐝",
        "🐞",
        "🦗",
        "🐌"
    )

    // State'leri başlangıçta boş bırakıp sonra UserSession'dan dolduruyoruz
    var nickname by rememberSaveable { mutableStateOf("") }
    var currentEmoji by rememberSaveable { mutableStateOf("") }
    var isInitialized by remember { mutableStateOf(false) }

    // UserSession'dan veri yükleme - sadece bir kez
    LaunchedEffect(Unit) {
        // UserSession'ı initialize et (ID garantili olarak oluştur)
        val initializedSession = UserSession.initializeUser()

        // Nickname varsa yükle
        initializedSession.name?.let { savedNickname ->
            if (savedNickname.isNotEmpty() && nickname.isEmpty()) {
                nickname = savedNickname
            }
        }

        // Emoji varsa yükle, yoksa random seç ve kaydet
        if (!initializedSession.emoji.isNullOrEmpty()) {
            currentEmoji = initializedSession.emoji!!
        } else if (currentEmoji.isEmpty()) {
            currentEmoji = animalEmojis.random()
            UserSession.updateUserEmoji(currentEmoji)
        }

        isInitialized = true
    }

    // Nickname değiştiğinde UserSession'ı güncelle
    LaunchedEffect(nickname) {
        if (isInitialized && nickname.isNotEmpty()) {
            scope.launch {
                UserSession.updateUserNickname(nickname)
            }
        }
    }

    // Emoji değiştiğinde UserSession'ı güncelle
    LaunchedEffect(currentEmoji) {
        if (isInitialized && currentEmoji.isNotEmpty()) {
            scope.launch {
                UserSession.updateUserEmoji(currentEmoji)
            }
        }
    }

    Scaffold(
        contentColor = WePickTheme.colors.onBackground,
        containerColor = WePickTheme.colors.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(Res.drawable.header),
                    contentDescription = "Header Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .background(
                            brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    WePickTheme.colors.background
                                ),
                                startY = 0f,
                                endY = 700f
                            )
                        )
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = WePickTheme.colors.background)
                    .padding(horizontal = 24.dp)
                    .offset(y = (-80).dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Ne seçeceğinizi tartışmayı bırakın! WePick ile birlikte en iyi seçimi kolayca yapın!",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = WePickTheme.colors.onBackground,
                        textAlign = TextAlign.Center,
                        lineHeight = 24.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(12.dp)
                                .size(96.dp)
                                .clip(CircleShape)
                                .background(WePickTheme.colors.primaryVariant.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = currentEmoji ?: "?",
                                fontSize = 48.sp,
                                modifier = Modifier
                            )
                        }

                        IconButton(
                            onClick = {
                                currentEmoji = animalEmojis.random()
                            },

                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = WePickTheme.colors.primary,
                                contentColor = WePickTheme.colors.onPrimary
                            ),
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .clip(CircleShape)
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.ic_rotate_right),
                                contentDescription = "Refresh Profile",
                                tint = WePickTheme.colors.onPrimary,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = nickname ?: "",
                        onValueChange = { nickname = it },
                        label = { Text("Takma adın") },
                        placeholder = {
                            Text(
                                text = "Takma adınızı girin...",
                                color = WePickTheme.colors.onBackground.copy(0.5f)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = WePickTheme.colors.onBackground,
                            unfocusedTextColor = WePickTheme.colors.onBackground.copy(alpha = 0.7f),
                            focusedContainerColor = WePickTheme.colors.background,
                            focusedBorderColor = WePickTheme.colors.primary,
                            unfocusedBorderColor = WePickTheme.colors.onBackground.copy(alpha = 0.3f),
                            focusedLabelColor = WePickTheme.colors.primary,
                            unfocusedLabelColor = WePickTheme.colors.onBackground.copy(alpha = 0.7f),
                            cursorColor = WePickTheme.colors.primary
                        ),
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Words
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Button(
                            onClick = {
                                navController.navigate(route = Screens.JOIN_ROOM)
                                viewModel.setUser()
                            },
                            enabled = nickname.isNotBlank(),
                            modifier = Modifier
                                .fillMaxWidth()
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
                                text = "Odaya katıl",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                navController.navigate(route = Screens.CATEGORY)
                                viewModel.setUser()
                            },
                            enabled = nickname.isNotBlank(),
                            modifier = Modifier
                                .fillMaxWidth()
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
                                text = "Oda oluştur",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                            )
                        }
                    }
                }
            }
        }
    }
}
