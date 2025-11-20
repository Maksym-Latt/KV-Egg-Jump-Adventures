package com.egg.jumpadventures.ui.main.menuscreen.model

import androidx.annotation.DrawableRes
import com.egg.jumpadventures.R

enum class EggSkin(
    val title: String,
    val price: Int,
    @DrawableRes val shopPreview: Int,
    @DrawableRes val playerSprite: Int,
) {
    Classic("Snowy Egg", 0, R.drawable.egg_1, R.drawable.player_1),
    Gold("Gold Egg", 250, R.drawable.egg_2, R.drawable.player_2),
    Rainbow("Rainbow Egg", 850, R.drawable.egg_3, R.drawable.player_3),
    Cracked("Cracked Egg", 1050, R.drawable.egg_4, R.drawable.player_4);
}
