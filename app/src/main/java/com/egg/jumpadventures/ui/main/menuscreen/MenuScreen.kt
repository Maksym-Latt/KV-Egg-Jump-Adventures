package com.egg.jumpadventures.ui.main.menuscreen

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.egg.jumpadventures.R
import com.egg.jumpadventures.ui.main.component.SecondaryIconButton
import com.egg.jumpadventures.ui.main.component.StartPrimaryButton

@Composable
fun MenuScreen(
    state: MainViewModel.UiState,
    onStartGame: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenShop: () -> Unit,
    onOpenPrivacy: () -> Unit,
) {
    val transition = rememberInfiniteTransition(label = "chicken_float")
    val floatOffset = transition.animateFloat(
        initialValue = -8f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFFFFF4FF),
                        Color(0xFFFFE0D2),
                        Color(0xFFFFE7C6)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ShopBadge(onOpenShop = onOpenShop, coins = state.coins)
                SecondaryIconButton(
                    onClick = onOpenSettings,
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Open settings",
                        tint = Color.White,
                        modifier = Modifier.fillMaxSize(0.82f)
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.title_chicken),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 12.dp)
                        .scale(1.05f)
                        .alpha(0.95f)
                        .offset(y = floatOffset.value.dp),
                    contentScale = ContentScale.Fit
                )
                Image(
                    painter = painterResource(id = R.drawable.title_text),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(vertical = 8.dp),
                    contentScale = ContentScale.FillWidth
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                StartPrimaryButton(
                    text = "Play",
                    onClick = onStartGame,
                    modifier = Modifier
                        .fillMaxWidth(0.68f)
                        .height(64.dp)
                )

                if (state.lastHeight > 0) {
                    Spacer(modifier = Modifier.height(18.dp))
                    Text(
                        text = "Last jump: ${state.lastHeight} m",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color(0xFF7B4A2D),
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Tap the gear for settings or read privacy",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF8C674D))
                )
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp)
        ) {
            Text(
                text = "Privacy",
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(Color(0x33FFFFFF))
                    .padding(horizontal = 18.dp, vertical = 8.dp)
                    .shadow(0.dp),
                color = Color(0xFF7B4A2D),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .padding(4.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Color.Transparent)
                    .clickableWithRipple(onOpenPrivacy)
            )
        }
    }
}

@Composable
private fun ShopBadge(onOpenShop: () -> Unit, coins: Int) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(18.dp))
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFFFF1D9), Color(0xFFFFC796))
                )
            )
            .shadow(8.dp, RoundedCornerShape(18.dp), clip = false)
            .padding(horizontal = 14.dp, vertical = 12.dp)
            .clickableWithRipple(onOpenShop),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFFFE6B0)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.coin),
                contentDescription = null,
                modifier = Modifier.size(26.dp)
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(
                text = "New skin",
                color = Color(0xFF7B4A2D),
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
            Text(
                text = "$coins coins",
                color = Color(0xFF9A6A46),
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp
            )
        }
    }
}

private fun Modifier.clickableWithRipple(onClick: () -> Unit): Modifier =
    this.then(androidx.compose.foundation.clickable(onClick = onClick))
