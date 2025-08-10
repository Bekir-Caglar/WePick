package com.bekircaglar.wepick.presentation.screens.createroom

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells.Adaptive
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.bekircaglar.wepick.navigation.Screens
import com.bekircaglar.wepick.presentation.screens.createroom.components.InviteFriendsBottomSheet
import com.bekircaglar.wepick.presentation.screens.createroom.components.RoomCodeHeader
import com.bekircaglar.wepick.theme.WePickTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import wepick.composeapp.generated.resources.Res
import wepick.composeapp.generated.resources.ic_arrow_left
import wepick.composeapp.generated.resources.ic_exit
import wepick.composeapp.generated.resources.ic_menu
import wepick.composeapp.generated.resources.ic_plus
import wepick.composeapp.generated.resources.logo
import wepick.composeapp.generated.resources.qr_code


@Composable
fun CreateRoomScreen(navController: NavHostController) {

    ContentUi(
        onBackClick = {
            navController.popBackStack()
        },
        onStartClicked = {
            navController.navigate(Screens.SELECTION)
        }
    )
}

data class User(
    val id: String = "",
    val name: String = "",
    val emoji: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ContentUi(
    onBackClick: () -> Unit,
    onStartClicked: () -> Unit = { /* No-op */ }
) {
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
    var listOfUsers by rememberSaveable() {
        mutableStateOf(
            listOf(
                User(id = "0", name = "inviteButon", emoji = ""),
                User(id = "1", name = "MırMır", emoji = animalEmojis[0]),
                User(id = "2", name = "Karabaş", emoji = animalEmojis[1]),
                User(id = "3", name = "Tuki", emoji = animalEmojis[2]),
                User(id = "4", name = "Bobo", emoji = animalEmojis[3]),
            )
        )
    }
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }

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
                        modifier = Modifier
                            .size(32.dp),
                    )
                },
                navigationIcon = {
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
                },
                actions = {
                    IconButton(
                        onClick = onBackClick
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_exit),
                            contentDescription = "Exit Room",
                            modifier = Modifier.size(20.dp),
                            tint = WePickTheme.colors.onBackground,
                        )
                    }


                }
            )
        }
    ) {

        if (showBottomSheet) {
            InviteFriendsBottomSheet(
                roomCode = "WLYCY",
                shareLink = "https://yourapp.com/join/WLYCY",
                onDismiss = { showBottomSheet = false },
                qrCodePainter = painterResource(resource = Res.drawable.qr_code) // QR kod resminizi buraya koyun
            )
        }

        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .background(WePickTheme.colors.background)
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            RoomCodeHeader()

            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .padding(horizontal = 32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                columns = Adaptive(minSize = 120.dp)
            ) {
                items(listOfUsers) { user ->
                    val isInviteButton = user.name == "inviteButon"
                    Card(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .height(100.dp)
                            .width(100.dp),
                        onClick = {
                            if (isInviteButton) {
                                showBottomSheet = true
                                /*listOfUsers = listOfUsers + User(
                                    id = (listOfUsers.size + 1).toString(),
                                    name = "User ${listOfUsers.size + 1}",
                                    emoji = animalEmojis[(listOfUsers.size + 1) % animalEmojis.size]
                                )*/
                            } else {
                            }
                        },
                        colors = CardDefaults.cardColors(
                            containerColor = if (isInviteButton) WePickTheme.colors.primary else WePickTheme.colors.primaryVariant.copy(
                                0.4f
                            )
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        if (isInviteButton)
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                Box(
                                    modifier = Modifier
                                        .padding(12.dp)
                                        .size(80.dp)
                                        .clip(CircleShape)
                                        .background(WePickTheme.colors.primaryVariant.copy(alpha = 0.4f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = painterResource(Res.drawable.ic_plus),
                                        contentDescription = "Invite User",
                                        modifier = Modifier
                                            .size(30.dp)
                                            .clip(CircleShape),
                                        colorFilter = ColorFilter.tint(WePickTheme.colors.onPrimary)

                                    )
                                }

                                Text(
                                    text = "Davet Et",
                                    fontSize = 18.sp,
                                    color = Color.White,
                                )
                            }
                        else
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                Box(
                                    contentAlignment = Alignment.Center
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .padding(12.dp)
                                            .size(80.dp)
                                            .clip(CircleShape)
                                            .background(WePickTheme.colors.primaryVariant.copy(alpha = 0.4f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = user.emoji,
                                            fontSize = 36.sp,
                                            modifier = Modifier
                                        )
                                    }
                                }
                                Text(
                                    text = user.name,
                                    fontSize = 18.sp,
                                    color = WePickTheme.colors.onBackground,
                                )
                            }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))


            Button(
                onClick = onStartClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(horizontal = 32.dp),
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
                    text = "Oylamayı Başlat",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                )
            }

        }
    }

}


@Preview()
@Composable
fun LightCreateRoomScreenPreview() {
    ContentUi(
        onBackClick = {}
    )
}

@Preview()
@Composable
fun DarkCreateRoomScreenPreview() {
    WePickTheme(darkTheme = true) {
        ContentUi(
            onBackClick = {}
        )
    }
}