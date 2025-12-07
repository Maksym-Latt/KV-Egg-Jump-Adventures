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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun VectorRetreatNode(
    trigger: () -> Unit,
    modifier: Modifier = Modifier
) = OrbitalGlyphButton(
    onActivate = trigger,
    modifier = modifier
) {
    Icon(
        imageVector = Icons.Default.ArrowBack,
        contentDescription = null,
        tint = Color(0xFFF69533),
        modifier = Modifier.fillMaxSize(0.7f)
    )
}

@Composable
fun OrbitalGlyphButton(
    onActivate: () -> Unit,
    modifier: Modifier = Modifier,
    glyphScale: Dp = 40.dp,
    framePadding: PaddingValues = PaddingValues(8.dp),
    glyph: @Composable () -> Unit
) {
    val hullShape = RoundedCornerShape(22.dp)
    val signal = remember { MutableInteractionSource() }
    val engaged by signal.collectIsPressedAsState()


    val phaseShift by animateFloatAsState(
        targetValue = if (engaged) 0.96f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "secondary_button_scale"
    )


    val coreTone = Color(0xFFFFF7EA)
    val rimTone = Color(0xFFF29A35)

    Box(
        modifier = modifier
            .graphicsLayer {
                scaleX = phaseShift
                scaleY = phaseShift
            }
            .clip(hullShape)
            .clickable(
                interactionSource = signal,
                indication = null,
                onClick = onActivate
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(glyphScale * 1.6f)
                .background(coreTone, CircleShape)
                .border(2.dp, rimTone, CircleShape),
            contentAlignment = Alignment.Center
        ) {

            Box(
                modifier = Modifier
                    .size(glyphScale)
                    .graphicsLayer { alpha = if (engaged) 0.7f else 1f },
                contentAlignment = Alignment.Center
            ) {
                glyph()
            }
        }
    }
}
