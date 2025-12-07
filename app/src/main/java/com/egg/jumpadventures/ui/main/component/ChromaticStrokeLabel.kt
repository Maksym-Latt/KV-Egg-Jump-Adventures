package com.egg.jumpadventures.ui.main.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.collections.map
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import android.graphics.Typeface
import androidx.core.content.res.ResourcesCompat
import com.egg.jumpadventures.R

@Composable
fun ChromaticStrokeLabel(
    caption: String,
    modifier: Modifier = Modifier,
    textScale: TextUnit = 44.sp,
    outlineThickness: Float = 5f,
    colorRange: List<Color> = listOf(Color(0xFFF49C47), Color(0xFFF49C47))
) {
    val ctx = LocalContext.current
    val density = LocalDensity.current
    val pxSize = with(density) { textScale.toPx() }

    val fontFace = remember {
        ResourcesCompat.getFont(ctx, R.font.baloo_2_extra_bold)
            ?: Typeface.DEFAULT_BOLD
    }

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height((textScale.value * 1.3).dp)
    ) {
        val paintG = android.graphics.Paint().apply {
            isAntiAlias = true
            textSize = pxSize
            this.typeface = fontFace
        }

        val textWidth = paintG.measureText(caption)
        val x = (size.width - textWidth) / 2f
        val fm = paintG.fontMetrics
        val y = size.height / 2f - (fm.ascent + fm.descent) / 2f

        paintG.style = android.graphics.Paint.Style.STROKE
        paintG.strokeWidth = outlineThickness
        paintG.color = 0xFFD86A22.toInt()
        paintG.strokeJoin = android.graphics.Paint.Join.ROUND
        drawContext.canvas.nativeCanvas.drawText(caption, x, y, paintG)

        paintG.style = android.graphics.Paint.Style.FILL
        paintG.shader = android.graphics.LinearGradient(
            0f, y + fm.ascent, 0f, y + fm.descent,
            colorRange.map { it.toArgb() }.toIntArray(),
            null,
            android.graphics.Shader.TileMode.CLAMP
        )
        drawContext.canvas.nativeCanvas.drawText(caption, x, y, paintG)
    }
}