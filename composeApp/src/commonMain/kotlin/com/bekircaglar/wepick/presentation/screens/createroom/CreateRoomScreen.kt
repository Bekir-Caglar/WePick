package com.bekircaglar.wepick.presentation.screens.createroom

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.bekircaglar.wepick.Platform
import com.bekircaglar.wepick.data.manager.UserSession
import com.bekircaglar.wepick.di.AppModule
import com.bekircaglar.wepick.domain.model.RoomModel
import com.bekircaglar.wepick.domain.model.User
import com.bekircaglar.wepick.getPlatform
import com.bekircaglar.wepick.navigation.Screens
import com.bekircaglar.wepick.presentation.screens.createroom.components.InviteFriendsBottomSheet
import com.bekircaglar.wepick.presentation.screens.createroom.components.RoomCodeHeader
import com.bekircaglar.wepick.theme.WePickTheme
import com.bekircaglar.wepick.utils.HandleBackPress
import com.bekircaglar.wepick.utils.QueryState
import com.bekircaglar.wepick.utils.data
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.context.startKoin
import wepick.composeapp.generated.resources.Res
import wepick.composeapp.generated.resources.ic_exit
import wepick.composeapp.generated.resources.ic_king
import wepick.composeapp.generated.resources.ic_menu
import wepick.composeapp.generated.resources.ic_plus
import wepick.composeapp.generated.resources.logo
import wepick.composeapp.generated.resources.qr_code


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CreateRoomScreen(navController: NavHostController, roomCode: String) {
    val viewModel: CreateRoomViewModel = koinViewModel()
    val room by viewModel.room.collectAsStateWithLifecycle()
    val isUserExit by viewModel.isUserExit.collectAsStateWithLifecycle()
    val listOfUsers by viewModel.roomUsers.collectAsStateWithLifecycle()
    val allUsersReady by viewModel.allUsersReady.collectAsStateWithLifecycle()
    val platform = getPlatform()
    var isOwner by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(room) {
        isOwner = UserSession.getId() == room?.data?.ownerId
    }

    LaunchedEffect(isUserExit) {
        if (isUserExit) {
            scope.launch {
                val userId = UserSession.getId()
                userId?.let {
                    navController.navigate(Screens.LAUNCH) {
                        popUpTo(0)
                        launchSingleTop = true
                    }
                }
            }
        }
    }

    LaunchedEffect(roomCode) {
        if (room == null) {
            viewModel.getRoom(roomCode = roomCode)
            UserSession.getId()?.let {
                viewModel.joinRoom(
                    roomCode = roomCode,
                    userId = it,
                )
            }
        }
    }

    if (platform.name == "Android") {
        HandleBackPress {
            scope.launch {
                val userId = UserSession.getId()
                userId?.let {
                    room?.data?.id?.let { roomId ->
                        viewModel.exitRoom(
                            roomId = roomId,
                            userId = it
                        )
                    }
                }
            }
        }
    }

    ContentUi(
        roomCode = roomCode,
        onBackClick = {
            scope.launch {
                val userId = UserSession.getId()
                userId?.let {
                    room?.data?.id?.let { roomId ->
                        viewModel.exitRoom(
                            roomId = roomId,
                            userId = it
                        )
                    }
                }
            }
        },
        onStartClicked = {
            navController.navigate(Screens.SELECTION)
        },
        setUsersReadyStatus = { isReady ->
            viewModel.setUsersReadyStatus(
                isReady = isReady
            )
        },
        room = room,
        isOwner = isOwner,
        allUsersReady = allUsersReady,
        listOfUsers = listOfUsers,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ContentUi(
    listOfUsers: List<User>,
    room: QueryState<RoomModel>?,
    roomCode: String,
    isOwner: Boolean = false,
    allUsersReady: Boolean = false,
    setUsersReadyStatus: (Boolean) -> Unit = { },
    onBackClick: () -> Unit,
    onStartClicked: () -> Unit = { }
) {

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
    ) { padding ->

        var isReady by remember { mutableStateOf(false) }

        if (showBottomSheet) {
            InviteFriendsBottomSheet(
                roomCode = roomCode,
                onDismiss = { showBottomSheet = false },
            )
        }

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(WePickTheme.colors.background)
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            RoomCodeHeader(
                roomCode = roomCode,
            )

            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .padding(horizontal = 32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                columns = Adaptive(minSize = 120.dp)
            ) {
                items(
                    listOf(
                        listOfUsers.firstOrNull { it.name == "inviteButon" } ?: User(
                            id = "invite",
                            name = "inviteButon",
                            emoji = null
                        )
                    ) + listOfUsers.filter { it.name != "inviteButon" }
                ) { user ->
                    val isInviteButton = user.name == "inviteButon"
                    val isOwnerUser = user.id == room?.data?.ownerId
                    val isReady = room?.data?.readyMembers?.contains(user.id) == true

                    val backgroundColor = when {
                        isInviteButton -> WePickTheme.colors.primary
                        else -> WePickTheme.colors.primaryVariant.copy(0.4f)
                    }
                    val readyColor = when {
                        isReady -> Color(0xFF4CAF50).copy(alpha = 0.2f) // yeşil hafif saydam
                        else -> Color(0xFFF44336).copy(alpha = 0.2f)
                    }

                    Card(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .height(100.dp)
                            .width(100.dp),
                        onClick = {
                            if (isInviteButton) {
                                showBottomSheet = true
                            }
                        },
                        colors = CardDefaults.cardColors(
                            containerColor = backgroundColor
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        if (isInviteButton) {
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
                                        modifier = Modifier.size(30.dp),
                                        colorFilter = ColorFilter.tint(WePickTheme.colors.onPrimary)
                                    )
                                }
                                Text(
                                    text = "Davet Et",
                                    fontSize = 18.sp,
                                    color = Color.White,
                                )
                            }
                        } else {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    if (isOwnerUser) {
                                        Text(
                                            text = "👑",
                                            fontSize = 24.sp,
                                            modifier = Modifier
                                                .align(Alignment.TopEnd)
                                                .graphicsLayer(rotationZ = 35f)
                                                .offset(
                                                    x = (-7).dp,
                                                    y = (12).dp
                                                ).zIndex(2f),
                                        )
                                    }
                                    Box(
                                        modifier = Modifier
                                            .padding(12.dp)
                                            .size(80.dp)
                                            .clip(CircleShape)
                                            .background(WePickTheme.colors.primaryVariant.copy(alpha = 0.4f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = user.emoji ?: "",
                                            fontSize = 36.sp,
                                        )
                                    }
                                }
                                Text(
                                    text = user.name ?: "",
                                    fontSize = 16.sp,
                                    color = WePickTheme.colors.onBackground,
                                    maxLines = 1,
                                    textAlign = TextAlign.Center,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier
                                        .background(
                                            if (!isOwnerUser) readyColor else Color.Transparent,
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .padding(horizontal = 8.dp)

                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))


            Button(
                onClick = {
                    if (isOwner) {
                        onStartClicked()
                    } else {
                        isReady = !isReady
                        setUsersReadyStatus(isReady)
                    }
                },
                enabled = if (isOwner) allUsersReady else true,
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
                    text = if (isOwner) "Oylamayı Başlat" else if (isReady) "Hazır Değil" else "Hazır",
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
        room = null,
        roomCode = "AHTWS",
        onBackClick = {},
        listOfUsers = emptyList()
    )
}

@Preview()
@Composable
fun DarkCreateRoomScreenPreview() {
    WePickTheme(darkTheme = true) {
        ContentUi(
            room = null,
            roomCode = "AHTWS",
            onBackClick = {},
            listOfUsers = emptyList()
        )
    }
}