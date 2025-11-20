package com.egg.jumpadventures.data.progress

import com.egg.jumpadventures.ui.main.menuscreen.model.EggSkin
import kotlinx.coroutines.flow.StateFlow

interface PlayerProgressRepository {
    val progress: StateFlow<PlayerProgress>

    fun recordFinishedRun(coinsEarned: Int, height: Int, level: Int)
    fun saveLevel(level: Int)
    fun selectSkin(skin: EggSkin)
    fun buySkin(skin: EggSkin): Boolean
}
