package com.bekircaglar.wepick.presentation.screens.createroom.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bekircaglar.wepick.theme.WePickTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import qrgenerator.qrkitpainter.PatternType
import qrgenerator.qrkitpainter.QrBallType
import qrgenerator.qrkitpainter.QrFrameType
import qrgenerator.qrkitpainter.QrKitBrush
import qrgenerator.qrkitpainter.QrKitColors
import qrgenerator.qrkitpainter.QrKitLogo
import qrgenerator.qrkitpainter.QrKitShapes
import qrgenerator.qrkitpainter.QrPixelType
import qrgenerator.qrkitpainter.customBrush
import qrgenerator.qrkitpainter.getSelectedFrameShape
import qrgenerator.qrkitpainter.getSelectedPattern
import qrgenerator.qrkitpainter.getSelectedPixel
import qrgenerator.qrkitpainter.getSelectedQrBall
import qrgenerator.qrkitpainter.rememberQrKitPainter
import wepick.composeapp.generated.resources.Res
import wepick.composeapp.generated.resources.ic_copy
import wepick.composeapp.generated.resources.ic_link
import wepick.composeapp.generated.resources.ic_qr
import wepick.composeapp.generated.resources.ic_x
import wepick.composeapp.generated.resources.logo
import wepick.composeapp.generated.resources.qr_code

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InviteFriendsBottomSheet(
    roomCode: String,
    onDismiss: () -> Unit,
) {
    val shareLink = "we-pick://join?code=$roomCode"
    val clipboardManager = LocalClipboardManager.current
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = WePickTheme.colors.background,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        modifier = Modifier.fillMaxWidth(),
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Arkadaşlarını Davet Et",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = WePickTheme.colors.onSurface,
                )

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(20.dp)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_x),
                        contentDescription = "Kapat",
                        tint = WePickTheme.colors.onSurface.copy(alpha = 0.8f),
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            Box(
                modifier = Modifier
                    .background(
                        WePickTheme.colors.primaryVariant.copy(alpha = 0.4f),
                        RoundedCornerShape(16.dp)
                    )
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {

                val painter = rememberQrKitPainter(data = shareLink)

                Image(
                    painter = painter,
                    contentDescription = "QR Kodu",
                    modifier = Modifier
                        .size(200.dp)
                        .background(Color.White, shape = RoundedCornerShape(12.dp))
                        .padding(8.dp),
                    colorFilter = ColorFilter.tint(Color.Black),
                    alignment = Alignment.Center
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Oda kodunu arkadaşına gönder veya onlara yukarıdaki QR kodu tarat.",
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = roomCode,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = WePickTheme.colors.primary,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(48.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ActionButton(
                    icon = painterResource(Res.drawable.ic_copy),
                    text = "Kodu Kopyala",
                    onClick = {
                        clipboardManager.setText(AnnotatedString(roomCode))
                    }
                )

                // Copy Link Butonu
                ActionButton(
                    icon = painterResource(Res.drawable.ic_link),
                    text = "Linki Kopyala",
                    onClick = {
                        clipboardManager.setText(AnnotatedString(shareLink))
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun ActionButton(
    icon: Painter,
    text: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(120.dp)
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier
                .size(56.dp)
                .background(
                    WePickTheme.colors.primaryVariant.copy(alpha = 0.4f),
                    RoundedCornerShape(28.dp)
                )
        ) {
            Icon(
                painter = icon,
                contentDescription = text,
                tint = WePickTheme.colors.primary,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = text,
            fontSize = 12.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}