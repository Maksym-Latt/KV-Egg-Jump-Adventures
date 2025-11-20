package com.egg.jumpadventures.data.progress

import com.egg.jumpadventures.ui.main.menuscreen.model.EggSkin

data class PlayerProgress(
    val coins: Int = 0,
    val level: Int = 1,
    val bestHeight: Int = 0,
    val selectedSkin: EggSkin = EggSkin.Classic,
    val ownedSkins: Set<EggSkin> = setOf(EggSkin.Classic)
)
