package com.bekircaglar.wepick.presentation.screens.joinroom

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.bekircaglar.wepick.data.manager.UserSession
import com.bekircaglar.wepick.navigation.RoomCode
import com.bekircaglar.wepick.navigation.Screens
import com.bekircaglar.wepick.theme.WePickTheme
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import qrscanner.CameraLens
import qrscanner.OverlayShape
import qrscanner.QrScanner
import wepick.composeapp.generated.resources.Res
import wepick.composeapp.generated.resources.camera_permission_denied
import wepick.composeapp.generated.resources.ic_arrow_left
import wepick.composeapp.generated.resources.ic_info
import wepick.composeapp.generated.resources.ic_qr
import wepick.composeapp.generated.resources.join_message
import wepick.composeapp.generated.resources.join_room
import wepick.composeapp.generated.resources.join_room_code_info
import wepick.composeapp.generated.resources.join_room_via_qr
import wepick.composeapp.generated.resources.logo

@Composable
fun JoinRoomScreen(navController: NavHostController) {
    val viewModel: JoinViewModel = koinViewModel()
    val isUserJoined by viewModel.isUserJoined.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    var joinedRoomCode by remember { mutableStateOf("") }

    LaunchedEffect(isUserJoined) {
        if (isUserJoined == true && joinedRoomCode.isNotEmpty()) {
            navController.navigate(RoomCode(joinedRoomCode))
        }
    }

    ContentUI(
        onBackPressed = {
            navController.popBackStack(Screens.LAUNCH, inclusive = false)
        },
        onJoinRoom = { roomCode ->
            joinedRoomCode = roomCode
            scope.launch {
                val userId = UserSession.getId()
                userId?.let {
                    viewModel.joinRoom(
                        roomCode = roomCode,
                        userId = it
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ContentUI(
    onBackPressed: () -> Unit = { },
    onJoinRoom: (String) -> Unit = { }
) {
    var codeValues by remember { mutableStateOf(List(5) { "" }) }
    val focusRequesters = remember { List(5) { FocusRequester() } }
    var isButtonEnabled by remember { mutableStateOf(false) }
    var qrScanClicked by remember { mutableStateOf(false) }

    LaunchedEffect(codeValues) {
        isButtonEnabled = codeValues.all { it.isNotEmpty() }
    }

    if (qrScanClicked) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent),
                    title = {},
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                qrScanClicked = false
                            },
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.ic_arrow_left),
                                contentDescription = "Exit Room",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp),
                            )
                        }
                    }
                )
            },
            modifier = Modifier
                .fillMaxSize()
                .zIndex(2f)
        ){

        }

        QrScanner(
            flashlightOn = false,
            cameraLens = CameraLens.Back,
            openImagePicker = false,
            onCompletion = { roomLink ->
                qrScanClicked = false
                if (roomLink.isNotEmpty()) {
                    val code = roomLink.takeLast(5)
                    if (code.length == 5) {
                        codeValues = code.toList().map { it.toString() }
                        onJoinRoom(code)
                    }
                }
            },
            imagePickerHandler = {},
            onFailure = { error ->
                // Handle error
            },
            overlayShape = OverlayShape.Square,
            overlayColor = Color.Black.copy(0.7F),
            overlayBorderColor = Color.White,
            permissionDeniedView = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(Res.string.camera_permission_denied),
                        color = Color.White,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .zIndex(1f),
        )
    }


    Scaffold(
        containerColor = WePickTheme.colors.background,
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = WePickTheme.colors.surface.copy(0.2f),
                    titleContentColor = WePickTheme.colors.primary,
                    navigationIconContentColor = WePickTheme.colors.primary
                ),
                title = { },
                navigationIcon = {
                    IconButton(
                        onClick = onBackPressed,
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_arrow_left),
                            contentDescription = "Exit Room",
                            tint = WePickTheme.colors.onBackground,
                            modifier = Modifier.size(20.dp),
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
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Image(
                painter = painterResource(Res.drawable.logo),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(16.dp))
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(Res.string.join_message),
                style = MaterialTheme.typography.bodyLarge,
                color = WePickTheme.colors.onBackground,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            Row(
                modifier = Modifier.padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(5) { index ->
                    CodeInputField(
                        index = index,
                        value = codeValues[index],
                        codeValues = codeValues,
                        onValueChange = { i, newValue ->
                            val newList = codeValues.toMutableList()
                            newList[i] = newValue
                            codeValues = newList
                        },
                        focusRequesters = focusRequesters,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

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
                    text = stringResource(Res.string.join_room_code_info),
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val finalCode = codeValues.joinToString("")
                    onJoinRoom(finalCode)

                },
                enabled = isButtonEnabled,
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
                    text = stringResource(Res.string.join_room),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = {
                    qrScanClicked = true
                }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_qr),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(Res.string.join_room_via_qr),
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun CodeInputField(
    index: Int,
    value: String,
    codeValues: List<String>,
    onValueChange: (Int, String) -> Unit,
    focusRequesters: List<FocusRequester>,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            val filteredValue = newValue.filter { it.isLetterOrDigit() }.uppercase()
            val codeToUse = if (filteredValue.length > 5) filteredValue.take(5) else filteredValue
            if (codeToUse.length == 5) {
                codeToUse.forEachIndexed { i, c ->
                    if (i < codeValues.size) {
                        onValueChange(i, c.toString())
                    }
                }
                focusRequesters.last().requestFocus()
            } else {
                val singleChar = codeToUse.take(1)
                if (singleChar != value) {
                    onValueChange(index, singleChar)
                    if (singleChar.isNotEmpty() && index < focusRequesters.lastIndex) {
                        focusRequesters[index + 1].requestFocus()
                    }
                }
            }
        },
        modifier = modifier
            .aspectRatio(1f)
            .focusRequester(focusRequesters[index])
            .onKeyEvent { keyEvent: KeyEvent ->
                if (keyEvent.key == Key.Backspace) {
                    if (value.isNotEmpty()) {
                        onValueChange(index, "")
                        true
                    } else if (index > 0) {
                        focusRequesters[index - 1].requestFocus()
                        onValueChange(index - 1, "")
                        true
                    } else false
                } else false
            },
        textStyle = TextStyle(
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        ),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = WePickTheme.colors.primary,
            unfocusedBorderColor = WePickTheme.colors.primaryVariant.copy(alpha = 0.5f),
            cursorColor = WePickTheme.colors.primary,
            focusedTextColor = WePickTheme.colors.onBackground,
            unfocusedTextColor = WePickTheme.colors.onBackground.copy(alpha = 0.7f)
        ),
        shape = RoundedCornerShape(12.dp),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next,
            capitalization = KeyboardCapitalization.Characters
        )
    )
}

@Preview
@Composable
private fun JoinRoomScreenPreview() {
    ContentUI()
}
