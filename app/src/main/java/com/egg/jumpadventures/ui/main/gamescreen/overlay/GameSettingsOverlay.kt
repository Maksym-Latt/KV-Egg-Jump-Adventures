package com.egg.jumpadventures.ui.main.gamescreen.overlay

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.egg.jumpadventures.ui.main.component.GradientOutlinedText
import com.egg.jumpadventures.ui.main.component.OrangePrimaryButton
import com.egg.jumpadventures.ui.main.component.SecondaryBackButton
import com.egg.jumpadventures.ui.main.component.SecondaryIconButton
import com.egg.jumpadventures.ui.main.component.StartPrimaryButton

@Composable
fun GameSettingsOverlay(
    onResume: () -> Unit,
    onRetry: () -> Unit,
    onHome: () -> Unit,
) {
    val cardShape = RoundedCornerShape(26.dp)
    val panelGrad = Brush.verticalGradient(listOf(Color(0xFFFFE6FE), Color(0xFFCC5EFF)))

    Box(
        Modifier
            .fillMaxSize()
            .background(Color(0x99000000))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onResume() }
    ) {
        SecondaryBackButton(
            onClick = onResume,
            modifier = Modifier
                .padding(start = 20.dp, top = 24.dp)
                .size(60.dp)
        )

        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .clip(cardShape)
                .background(panelGrad)
                .padding(vertical = 20.dp, horizontal = 18.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {}
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                GradientOutlinedText(
                    text = "Pause",
                    fontSize = 36.sp,
                    gradientColors = listOf(Color.White, Color.White)
                )

                OrangePrimaryButton(
                    text = "Resume",
                    onClick = onResume,
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
                StartPrimaryButton(
                    text = "Try Again",
                    onClick = onRetry,
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
                SecondaryIconButton(
                    onClick = onHome,
                    modifier = Modifier.size(56.dp)
                ) {
                    Text(text = "Menu", color = Color.White, fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}
