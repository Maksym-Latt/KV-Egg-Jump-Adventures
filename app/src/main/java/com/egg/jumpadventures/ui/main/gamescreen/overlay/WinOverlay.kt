package com.egg.jumpadventures.ui.main.gamescreen.overlay

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.egg.jumpadventures.R
import com.egg.jumpadventures.ui.main.component.GradientOutlinedText
import com.egg.jumpadventures.ui.main.component.OrangePrimaryButton
import com.egg.jumpadventures.ui.main.component.PrimaryButton
import com.egg.jumpadventures.ui.main.component.PrimaryVariant
import com.egg.jumpadventures.ui.main.gamescreen.GameResult

@Composable
fun WinOverlay(
    result: GameResult,
    onNextLevel: () -> Unit,
    onHome: () -> Unit,
) {
    ResultOverlayContainer {
        ResultTitle(text = "YOU WIN!", background = Color(0xFFFFDCC9))

        Image(
            painter = painterResource(id = R.drawable.title_chicken),
            contentDescription = null,
            modifier = Modifier
                .size(140.dp)
                .padding(vertical = 4.dp),
            contentScale = ContentScale.Fit
        )

        ResultStat(
            label = "+${result.coins}",
            iconRes = R.drawable.coin,
            background = Color(0xFFFFE8D4),
            contentColor = Color(0xFFF28E3A)
        )

        OrangePrimaryButton(
            text = "TRY AGAIN",
            onClick = onNextLevel,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        PrimaryButton(
            text = "MENU",
            onClick = onHome,
            modifier = Modifier.fillMaxWidth(),
            variant = PrimaryVariant.PeachBack
        )
    }
}

@Composable
fun GameOverOverlay(
    result: GameResult,
    targetCoins: Int,
    onRetry: () -> Unit,
    onHome: () -> Unit,
) {
    ResultOverlayContainer {
        ResultTitle(text = "YOU FELL!", background = Color(0xFFFFD6CF))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ResultStat(
                label = "${result.coins}",
                iconRes = R.drawable.coin,
                background = Color(0xFFFFEBDC),
                contentColor = Color(0xFFF28E3A)
            )

            ResultStat(
                label = "${result.height} m",
                iconRes = R.drawable.platform,
                background = Color(0xFFFFEBDC),
                contentColor = Color(0xFF7B4A2D)
            )

            GoalReminder(targetCoins = targetCoins)
        }

        OrangePrimaryButton(
            text = "TRY AGAIN",
            onClick = onRetry,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        )

        PrimaryButton(
            text = "MENU",
            onClick = onHome,
            modifier = Modifier.fillMaxWidth(),
            variant = PrimaryVariant.PeachBack
        )
    }
}

@Composable
private fun ResultOverlayContainer(content: @Composable ColumnScope.() -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 26.dp, vertical = 44.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(36.dp))
                .background(Color.White.copy(alpha = 0.68f))
                .padding(horizontal = 24.dp, vertical = 22.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            content = content
        )
    }
}

@Composable
private fun ResultTitle(text: String, background: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(background)
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        GradientOutlinedText(
            text = text,
            fontSize = 28.sp,
            gradientColors = listOf(Color(0xFFFFB15C), Color(0xFFFFB15C))
        )
    }
}

@Composable
private fun ResultStat(
    label: String,
    iconRes: Int,
    background: Color,
    contentColor: Color,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(background)
            .padding(horizontal = 18.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier.size(30.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = label,
            color = contentColor,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@Composable
private fun GoalReminder(targetCoins: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Brush.horizontalGradient(listOf(Color(0xFFFFF2E6), Color(0xFFFFE0C8))))
            .padding(horizontal = 16.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Goal: $targetCoins coins",
            color = Color(0xFF7B4A2D),
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
    }
}
