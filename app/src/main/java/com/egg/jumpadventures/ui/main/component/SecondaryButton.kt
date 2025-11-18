package com.egg.jumpadventures.ui.main.component

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun SecondaryBackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) = SecondaryIconButton(
    onClick = onClick,
    modifier = modifier
) {
    Icon(
        imageVector = Icons.Default.ArrowBack,
        contentDescription = null,
        tint = Color(0xFFF69533),            // мягкий оранжевый как шестерёнка
        modifier = Modifier.fillMaxSize(0.7f)
    )
}

@Composable
fun SecondaryIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconSize: Dp = 40.dp,
    contentPadding: PaddingValues = PaddingValues(8.dp),
    icon: @Composable () -> Unit
) {
    val outerShape = RoundedCornerShape(22.dp)
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()

    // лёгкий скейл при нажатии
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.96f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "secondary_button_scale"
    )


    val innerCircleColor = Color(0xFFFFF7EA)
    val innerBorderColor = Color(0xFFF29A35)

    Box(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(outerShape)
            .clickable(
                interactionSource = interaction,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        // внутренний круг, как светлая "плашка"
        Box(
            modifier = Modifier
                .size(iconSize * 1.6f)             // диаметр внутреннего круга
                .background(innerCircleColor, CircleShape)
                .border(2.dp, innerBorderColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            // сама иконка
            Box(
                modifier = Modifier
                    .size(iconSize)
                    .graphicsLayer { alpha = if (pressed) 0.7f else 1f },
                contentAlignment = Alignment.Center
            ) {
                icon()
            }
        }
    }
}

@Composable
fun SecondaryIconButton(
    @DrawableRes iconRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconSize: Dp = 40.dp,
    contentPadding: PaddingValues = PaddingValues(8.dp)
) {
    SecondaryIconButton(
        onClick = onClick,
        modifier = modifier,
        iconSize = iconSize,
        contentPadding = contentPadding
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = Color(0xFFF69533),           // оранжевая шестерёнка как на макете
            modifier = Modifier.fillMaxSize(0.7f)
        )
    }
}
