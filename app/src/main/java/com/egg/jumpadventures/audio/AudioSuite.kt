package com.egg.jumpadventures.audio

interface AudioSuite {
    fun launchMenuTrack()
    fun launchPlayTrack()
    fun haltTracks()
    fun suspendTrack()
    fun continueTrack()

    fun adjustMusicLevel(percent: Int)
    fun adjustEffectLevel(percent: Int)
    fun toggleHaptics(enabled: Boolean)

    fun cueDefeat()
    fun cueCollect()
    fun cueLeap()
    fun cueInsufficient()
    fun cueVictory()
}
