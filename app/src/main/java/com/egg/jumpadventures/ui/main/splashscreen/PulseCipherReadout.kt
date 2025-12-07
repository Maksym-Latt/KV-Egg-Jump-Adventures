package com.egg.jumpadventures.ui.main.splashscreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.egg.jumpadventures.ui.main.component.ChromaticStrokeLabel
import kotlinx.coroutines.delay

@Composable
fun PulseCipherReadout(
    modifier: Modifier = Modifier,
    seed: String = "LOADING",
    tickInterval: Long = 300L
) {
    val shiftPattern = intArrayOf(1, 2, 3, 0)
    var idx by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(tickInterval)
            idx = (idx + 1) % shiftPattern.size
        }
    }
    val pulse = shiftPattern[idx]

    val rendered = buildString {
        append(seed)
        if (pulse > 0) append(" ")
        repeat(pulse) { append(".") }
    }

    ChromaticStrokeLabel(
        caption = rendered,
        modifier = modifier,
        textScale = 44.sp,
        outlineThickness = 10f
    )
}