package com.egg.jumpadventures.ui.main.gamescreen.overlay

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.egg.jumpadventures.ui.main.component.GradientOutlinedText
import com.egg.jumpadventures.ui.main.component.StartPrimaryButton

@Composable
fun IntroOverlay(
    onStart: () -> Unit,
) {
    val panelGrad = Brush.verticalGradient(listOf(Color(0xFFFFE6FE), Color(0xFFCC5EFF)))
    val cardShape = RoundedCornerShape(26.dp)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x99000000)),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .width(320.dp)
                .wrapContentHeight()
                .clip(cardShape)
                .background(panelGrad)
                .padding(vertical = 22.dp, horizontal = 18.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                GradientOutlinedText(
                    text = "Jump up!",
                    fontSize = 34.sp,
                    gradientColors = listOf(Color.White, Color.White)
                )
                Text(
                    text = "Bounce between soft platforms, grab sunrise coins, and keep the egg from falling.",
                    color = Color(0xFF7B4A2D),
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )
                StartPrimaryButton(
                    text = "Play",
                    onClick = onStart
                )
            }
        }
    }
}
