package com.bekircaglar.wepick.presentation.screens.createroom.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bekircaglar.wepick.theme.WePickTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import wepick.composeapp.generated.resources.Res
import wepick.composeapp.generated.resources.ic_qr
import wepick.composeapp.generated.resources.ic_secret_code
import wepick.composeapp.generated.resources.invite_message

@Composable
fun RoomCodeHeader(
    roomCode: String,
) {
    val clipboardManager = LocalClipboardManager.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp)
            .background(Color.Transparent, shape = RoundedCornerShape(16.dp))
            .border(
                color = WePickTheme.colors.onSurface.copy(0.3f),
                width = 1.dp,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Image(
            painter = painterResource(Res.drawable.ic_secret_code),
            contentDescription = "code",
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .size(24.dp)
                .align(Alignment.CenterVertically),
            colorFilter = ColorFilter.tint(WePickTheme.colors.onSurface.copy(0.8f))

        )

        Text(
            text = stringResource(Res.string.invite_message),
            fontWeight = Bold,
            fontSize = 15.sp,
            color = WePickTheme.colors.onBackground.copy(0.8f),
            modifier = Modifier
                .fillMaxWidth()
                .weight(15f)
        )

        Spacer(modifier = Modifier.weight(1f))

        TextButton(
            onClick = {
                clipboardManager.setText(AnnotatedString(roomCode))
            },
            modifier = Modifier
                .padding(end = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = roomCode,
                    fontWeight = Bold,
                    fontSize = 16.sp,
                )
            }
        }
    }
}