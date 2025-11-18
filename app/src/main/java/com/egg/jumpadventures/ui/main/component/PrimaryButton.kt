package com.egg.jumpadventures.ui.main.component

import android.R.attr.scaleX
import android.R.attr.scaleY
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ---------- Internal ----------
@Composable
fun StartPrimaryButton(
    text: String = "START",
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) = PrimaryButton(
    text = text,
    onClick = onClick,
    modifier = modifier,
    variant = PrimaryVariant.StartGreen
)

@Composable
fun OrangePrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) = PrimaryButton(
    text = text,
    onClick = onClick,
    modifier = modifier,
    variant = PrimaryVariant.Orange
)

// ---------- Варианты внешнего вида ----------

public enum class PrimaryVariant {
    StartGreen,     // зелёная "SELECTED"
    Orange,         // большая PLAY
    OrangeSmall,    // маленькая BUY
    PeachBack       // широкая BACK
}

// ---------- Основная кнопка ----------

@Composable
public fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: PrimaryVariant
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()

    // маленький scale при нажатии
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.97f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "primary_button_scale"
    )

    // ---------- Параметры под каждый вариант ----------

    val params: ButtonParams = when (variant) {
        // зелёная капсула (SELECTED)
        PrimaryVariant.StartGreen -> ButtonParams(
            family = FontFamily.SansSerif,
            weight = FontWeight.ExtraBold,
            size = 16.sp,
            line = 20.sp,
            padH = 22.dp,
            minHeight = 40.dp,
            cornerRadius = 20.dp,
            bgIdle = Brush.horizontalGradient(
                listOf(
                    Color(0xFF9CF56A),
                    Color(0xFF2EBB55)
                )
            ),
            bgPressed = Brush.horizontalGradient(
                listOf(
                    Color(0xFF7CC34F),
                    Color(0xFF228B3E)
                )
            ),
            borderColor = Color(0xFF1F7B38),
            textColor = Color.White
        )

        // большая PLAY
        PrimaryVariant.Orange -> ButtonParams(
            family = FontFamily.SansSerif,
            weight = FontWeight.ExtraBold,
            size = 22.sp,
            line = 26.sp,
            padH = 40.dp,
            minHeight = 56.dp,
            cornerRadius = 22.dp,
            bgIdle = Brush.horizontalGradient(
                listOf(
                    Color(0xFFFFE0B8),
                    Color(0xFFFFB15C)
                )
            ),
            bgPressed = Brush.horizontalGradient(
                listOf(
                    Color(0xFFFCC47E),
                    Color(0xFFE48C33)
                )
            ),
            borderColor = Color(0xFFF0933B),
            textColor = Color.White
        )

        // маленькая BUY
        PrimaryVariant.OrangeSmall -> ButtonParams(
            family = FontFamily.SansSerif,
            weight = FontWeight.ExtraBold,
            size = 14.sp,
            line = 18.sp,
            padH = 20.dp,
            minHeight = 32.dp,
            cornerRadius = 18.dp,
            bgIdle = Brush.horizontalGradient(
                listOf(
                    Color(0xFFFFE0B8),
                    Color(0xFFFFB15C)
                )
            ),
            bgPressed = Brush.horizontalGradient(
                listOf(
                    Color(0xFFFCC47E),
                    Color(0xFFE48C33)
                )
            ),
            borderColor = Color(0xFFF0933B),
            textColor = Color.White
        )

        // широкая BACK (персиковая с коричневым текстом)
        PrimaryVariant.PeachBack -> ButtonParams(
            family = FontFamily.SansSerif,
            weight = FontWeight.ExtraBold,
            size = 22.sp,
            line = 26.sp,
            padH = 40.dp,
            minHeight = 60.dp,
            cornerRadius = 26.dp,
            bgIdle = Brush.horizontalGradient(
                listOf(
                    Color(0xFFFFE7D4),
                    Color(0xFFFFB68B)
                )
            ),
            bgPressed = Brush.horizontalGradient(
                listOf(
                    Color(0xFFF8D1B3),
                    Color(0xFFE39268)
                )
            ),
            borderColor = Color(0xFFE08E5C),
            textColor = Color(0xFF5A3520) // коричневый
        )
    }

    val backgroundBrush = if (pressed) params.bgPressed else params.bgIdle

    val shape = RoundedCornerShape(params.cornerRadius)

    Box(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .shadow(
                elevation = 6.dp,
                shape = shape,
                clip = false,
                ambientColor = Color(0x33000000),
                spotColor = Color(0x33000000)
            )
            .clip(shape)
            .background(backgroundBrush)
            .border(width = 1.dp, color = params.borderColor, shape = shape)
            .defaultMinSize(minHeight = params.minHeight)
            .clickable(
                interactionSource = interaction,
                indication = null,
                onClick = onClick
            )
            .padding(
                horizontal = params.padH,
                vertical = 10.dp
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text.uppercase(),
            color = params.textColor,
            fontFamily = params.family,
            fontWeight = params.weight,
            fontSize = params.size,
            lineHeight = params.line,
            letterSpacing = 1.sp
        )
    }
}

/** Параметры кнопки (типографика + размеры + цвета) */
@Stable
private data class ButtonParams(
    val family: FontFamily,
    val weight: FontWeight,
    val size: TextUnit,
    val line: TextUnit,
    val padH: Dp,
    val minHeight: Dp,
    val cornerRadius: Dp,
    val bgIdle: Brush,
    val bgPressed: Brush,
    val borderColor: Color,
    val textColor: Color
)