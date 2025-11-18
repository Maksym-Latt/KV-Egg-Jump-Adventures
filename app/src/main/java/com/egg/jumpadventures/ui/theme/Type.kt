package com.egg.jumpadventures.ui.theme
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.egg.jumpadventures.R

val BalooFontFamily = FontFamily(
    Font(R.font.baloo_2_regular, weight = FontWeight.Normal),
    Font(R.font.baloo_2_medium, weight = FontWeight.Medium),
    Font(R.font.baloo_2_extra_bold, weight = FontWeight.ExtraBold),
)

private val HeadingText = TextStyle(
    fontFamily = BalooFontFamily,
    fontWeight = FontWeight.ExtraBold,
)

private val BodyText = TextStyle(
    fontFamily = BalooFontFamily,
    fontWeight = FontWeight.Normal,
)

val Typography = Typography(
    displayLarge = HeadingText.copy(fontSize = 48.sp, lineHeight = 54.sp),
    displayMedium = HeadingText.copy(fontSize = 40.sp, lineHeight = 46.sp),
    headlineLarge = HeadingText.copy(fontSize = 32.sp, lineHeight = 38.sp),
    headlineMedium = HeadingText.copy(fontSize = 28.sp, lineHeight = 34.sp),
    titleLarge = HeadingText.copy(fontSize = 24.sp, lineHeight = 30.sp),
    titleMedium = HeadingText.copy(fontSize = 20.sp, lineHeight = 26.sp),
    titleSmall = HeadingText.copy(fontSize = 18.sp, lineHeight = 24.sp),

    bodyLarge = BodyText.copy(fontSize = 16.sp, lineHeight = 22.sp),
    bodyMedium = BodyText.copy(fontSize = 14.sp, lineHeight = 20.sp),
    bodySmall = BodyText.copy(fontSize = 12.sp, lineHeight = 18.sp),

    labelLarge = HeadingText.copy(fontSize = 14.sp, lineHeight = 18.sp),
    labelMedium = HeadingText.copy(fontSize = 12.sp, lineHeight = 16.sp),
    labelSmall = HeadingText.copy(fontSize = 11.sp, lineHeight = 14.sp),
)