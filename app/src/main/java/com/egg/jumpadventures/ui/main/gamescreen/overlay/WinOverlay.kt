package com.egg.jumpadventures.ui.main.gamescreen.overlay

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.egg.jumpadventures.ui.main.component.GradientOutlinedText
import com.egg.jumpadventures.ui.main.component.OrangePrimaryButton
import com.egg.jumpadventures.ui.main.component.StartPrimaryButton
import com.egg.jumpadventures.ui.main.gamescreen.GameResult

@Composable
fun GameOverOverlay(
    result: GameResult,
    onRetry: () -> Unit,
    onHome: () -> Unit,
) {
    val shape = RoundedCornerShape(26.dp)
    val grad = Brush.verticalGradient(listOf(Color(0xFFFFE6FE), Color(0xFFCC5EFF)))

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 80.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .clip(shape)
                .background(grad)
                .padding(vertical = 24.dp, horizontal = 20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                GradientOutlinedText(
                    text = "You Fell!",
                    fontSize = 34.sp,
                    gradientColors = listOf(Color.White, Color.White)
                )

                Text(
                    text = "Coins: ${result.coins}    Height: ${result.height}m",
                    color = Color(0xFF7B4A2D),
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold
                )

                StartPrimaryButton(
                    text = "Try Again",
                    onClick = onRetry,
                    modifier = Modifier.fillMaxWidth(0.9f)
                )
                OrangePrimaryButton(
                    text = "Menu",
                    onClick = onHome,
                    modifier = Modifier.fillMaxWidth(0.9f)
                )
            }
        }
    }
}
