package com.egg.jumpadventures.ui.main.component

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
import com.egg.jumpadventures.ui.theme.BalooFontFamily

@Composable
fun LaunchActionButton(
    label: String = "START",
    onPress: () -> Unit,
    modifier: Modifier = Modifier
) = PrimaryCoreActionButton(
    label = label,
    onPress = onPress,
    modifier = modifier,
    style = ActionStyle.StartLime
)

@Composable
fun AmberActionButton(
    label: String,
    onPress: () -> Unit,
    modifier: Modifier = Modifier
) = PrimaryCoreActionButton(
    label = label,
    onPress = onPress,
    modifier = modifier,
    style = ActionStyle.OrangeAmber
)

public enum class ActionStyle {
    StartLime,
    OrangeAmber,
    AmberCompact,
    PeachReturn
}
@Composable
public fun PrimaryCoreActionButton(
    label: String,
    onPress: () -> Unit,
    modifier: Modifier = Modifier,
    style: ActionStyle
) {
    val interAction = remember { MutableInteractionSource() }
    val pressed by interAction.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.97f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "primary_button_scale"
    )

    val config: PillParams = when (style) {
        ActionStyle.StartLime -> PillParams(
            fontFamily = BalooFontFamily,
            fontWeight = FontWeight.ExtraBold,
            textMagnitude = 16.sp,
            ascentFlow = 20.sp,
            paddingAxis = 22.dp,
            minimumBound = 40.dp,
            curvature = 20.dp,
            coatIdle = Brush.horizontalGradient(
                listOf(
                    Color(0xFF9CF56A),
                    Color(0xFF2EBB55)
                )
            ),
            coatActive = Brush.horizontalGradient(
                listOf(
                    Color(0xFF7CC34F),
                    Color(0xFF228B3E)
                )
            ),
            rimShade = Color(0xFF1F7B38),
            pigment = Color.White
        )

        ActionStyle.OrangeAmber -> PillParams(
            fontFamily = BalooFontFamily,
            fontWeight = FontWeight.ExtraBold,
            textMagnitude = 22.sp,
            ascentFlow = 26.sp,
            paddingAxis = 40.dp,
            minimumBound = 56.dp,
            curvature = 22.dp,
            coatIdle = Brush.horizontalGradient(
                listOf(
                    Color(0xFFFFE0B8),
                    Color(0xFFFFB15C)
                )
            ),
            coatActive = Brush.horizontalGradient(
                listOf(
                    Color(0xFFFCC47E),
                    Color(0xFFE48C33)
                )
            ),
            rimShade = Color(0xFFF0933B),
            pigment = Color.White
        )

        ActionStyle.AmberCompact -> PillParams(
            fontFamily = BalooFontFamily,
            fontWeight = FontWeight.ExtraBold,
            textMagnitude = 14.sp,
            ascentFlow = 18.sp,
            paddingAxis = 20.dp,
            minimumBound = 32.dp,
            curvature = 18.dp,
            coatIdle = Brush.horizontalGradient(
                listOf(
                    Color(0xFFFFE0B8),
                    Color(0xFFFFB15C)
                )
            ),
            coatActive = Brush.horizontalGradient(
                listOf(
                    Color(0xFFFCC47E),
                    Color(0xFFE48C33)
                )
            ),
            rimShade = Color(0xFFF0933B),
            pigment = Color.White
        )

        ActionStyle.PeachReturn -> PillParams(
            fontFamily = BalooFontFamily,
            fontWeight = FontWeight.ExtraBold,
            textMagnitude = 22.sp,
            ascentFlow = 26.sp,
            paddingAxis = 40.dp,
            minimumBound = 60.dp,
            curvature = 26.dp,
            coatIdle = Brush.horizontalGradient(
                listOf(
                    Color(0xFFFFE7D4),
                    Color(0xFFFFB68B)
                )
            ),
            coatActive = Brush.horizontalGradient(
                listOf(
                    Color(0xFFF8D1B3),
                    Color(0xFFE39268)
                )
            ),
            rimShade = Color(0xFFE08E5C),
            pigment = Color(0xFF5A3520)
        )
    }

    val bgBrush = if (pressed) config.coatActive else config.coatIdle

    val pillShape = RoundedCornerShape(config.curvature)

    Box(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .shadow(
                elevation = 6.dp,
                shape = pillShape,
                clip = false,
                ambientColor = Color(0x33000000),
                spotColor = Color(0x33000000)
            )
            .clip(pillShape)
            .background(bgBrush)
            .border(width = 1.dp, color = config.rimShade, shape = pillShape)
            .defaultMinSize(minHeight = config.minimumBound)
            .clickable(
                interactionSource = interAction,
                indication = null,
                onClick = onPress
            )
            .padding(
                horizontal = config.paddingAxis,
                vertical = 10.dp
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label.uppercase(),
            color = config.pigment,
            fontFamily = config.fontFamily,
            fontWeight = config.fontWeight,
            fontSize = config.textMagnitude,
            lineHeight = config.ascentFlow,
            letterSpacing = 1.sp
        )
    }
}

@Stable
private data class PillParams(
    val fontFamily: FontFamily,
    val fontWeight: FontWeight,
    val textMagnitude: TextUnit,
    val ascentFlow: TextUnit,
    val paddingAxis: Dp,
    val minimumBound: Dp,
    val curvature: Dp,
    val coatIdle: Brush,
    val coatActive: Brush,
    val rimShade: Color,
    val pigment: Color
)