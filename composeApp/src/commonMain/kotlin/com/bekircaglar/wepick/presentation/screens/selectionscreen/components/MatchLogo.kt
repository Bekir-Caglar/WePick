package com.bekircaglar.wepick.presentation.screens.selectionscreen.components// Compose kodu: import eklemeden, doğrudan kullanılabilir şekilde
// Jetpack Compose için gerekli importlar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.graphicsLayer
import com.bekircaglar.wepick.theme.WePickTheme
import io.ktor.client.request.invoke
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.ui.tooling.preview.Preview
import wepick.composeapp.generated.resources.Montserrat_Bold
import wepick.composeapp.generated.resources.Res

@Composable
fun MatchLogo() {
    Box(
        modifier = Modifier
            .background(Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(Color.Transparent)
        ) {
            Text(
                text = "PERFECT",
                fontFamily = FontFamily(Font(Res.font.Montserrat_Bold)),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                letterSpacing = 2.sp,
                modifier = Modifier
                    .alpha(0.85f)
            )
            Text(
                text = "PAIR!",
                fontFamily = FontFamily(Font(Res.font.Montserrat_Bold)),
                fontSize = 100.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 4.sp,
                modifier = Modifier,
                style = TextStyle(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF238AFF),
                            Color(0xFF96C3FE),
                            Color(0xFFC5DDFC),
                        )
                    ),
                    shadow = Shadow(
                        color = Color(0x4D238AFF), // rgba(35, 138, 255, 0.3)
                        offset = Offset(0f, 16f),
                        blurRadius = 30f
                    ),
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            )
        }
    }
}

@Preview
@Composable
fun MatchLogoPreview() {
    WePickTheme() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(WePickTheme.colors.background),
            contentAlignment = Alignment.Center
        ){
            MatchLogo()
        }
    }
}