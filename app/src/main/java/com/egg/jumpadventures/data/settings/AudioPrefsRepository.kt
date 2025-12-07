package com.egg.jumpadventures.data.settings

interface AudioPrefsRepository {
    fun fetchMusicLevel(): Int
    fun fetchEffectLevel(): Int
    fun isHapticsActive(): Boolean

    fun storeMusicLevel(value: Int)
    fun storeEffectLevel(value: Int)
    fun storeHapticsState(enabled: Boolean)
}