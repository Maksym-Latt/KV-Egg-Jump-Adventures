package com.egg.jumpadventures.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import com.egg.jumpadventures.data.settings.AudioPrefsRepository

@Singleton
class DefaultAudioSuite @Inject constructor(
    @ApplicationContext private val context: Context,
    prefs: AudioPrefsRepository
) : AudioSuite {

    private enum class TrackGroup { MENU_THEME, PLAY_THEME }
    private enum class FxCue { VICTORY, DEFEAT, COLLECT, LEAP, SHORTAGE }

    private val fxPool: SoundPool = SoundPool.Builder()
        .setMaxStreams(6)
        .setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
        )
        .build()

    private val cueAliasMap = mapOf(
        FxCue.VICTORY to "sfx_win",
        FxCue.DEFEAT to "sfx_lose",
        FxCue.COLLECT to "sfx_pickup",
        FxCue.LEAP to "sfx_jump",
        FxCue.SHORTAGE to "sfx_nomoney",
    )

    private val cueResMap = cueAliasMap.mapValues { resolveRaw(it.value) }
    private val loadedHandles = mutableMapOf<FxCue, Int>()
    private val preparedCues = mutableSetOf<Int>()
    private val queuedCues = mutableSetOf<Int>()

    private val menuTrackRes = resolveRaw("music_menu")
    private val playTrackRes = resolveRaw("music_game")

    private val activePlayers = mutableMapOf<TrackGroup, MediaPlayer>()
    private var activeGroup: TrackGroup? = null

    private var musicLevel: Float = prefs.fetchMusicLevel().toVolume()
    private var fxLevel: Float = prefs.fetchEffectLevel().toVolume()
    private var hapticsEnabled: Boolean = prefs.isHapticsActive()
    private var pendingResume: Boolean = false

    private val hapticEngine: Vibrator? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val mgr = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
        mgr?.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    }

    init {
        fxPool.setOnLoadCompleteListener { pool, sampleId, status ->
            if (status == 0) {
                preparedCues += sampleId
                if (queuedCues.remove(sampleId)) {
                    pool.play(sampleId, fxLevel, fxLevel, 1, 0, 1f)
                }
            } else {
                queuedCues.remove(sampleId)
            }
        }
    }

    override fun launchMenuTrack() {
        pendingResume = false
        playMusic(TrackGroup.MENU_THEME)
    }

    override fun launchPlayTrack() {
        pendingResume = false
        playMusic(TrackGroup.PLAY_THEME)
    }

    override fun haltTracks() {
        activeGroup?.let { channel ->
            activePlayers[channel]?.let { player ->
                if (player.isPlaying) player.pause()
                player.seekTo(0)
            }
        }
        activeGroup = null
        pendingResume = false
    }

    override fun suspendTrack() {
        activeGroup?.let { channel ->
            activePlayers[channel]?.let { player ->
                if (player.isPlaying) {
                    player.pause()
                    pendingResume = true
                }
            }
        }
    }

    override fun continueTrack() {
        if (!pendingResume) return
        pendingResume = false
        activeGroup?.let { channel ->
            val player = activePlayers[channel]
            if (player != null && !player.isPlaying) {
                try {
                    player.start()
                } catch (_: IllegalStateException) {
                    activePlayers.remove(channel)
                    activeGroup = null
                }
            } else if (player == null) {
                activeGroup = null
            }
        }
    }

    override fun adjustMusicLevel(percent: Int) {
        musicLevel = percent.toVolume()
        activePlayers.values.forEach { player ->
            player.setVolume(musicLevel, musicLevel)
        }
    }

    override fun adjustEffectLevel(percent: Int) {
        fxLevel = percent.toVolume()
    }

    override fun toggleHaptics(enabled: Boolean) {
        hapticsEnabled = enabled
    }

    override fun cueDefeat() {
        playEffect(FxCue.DEFEAT)
        vibrate(160L)
    }

    override fun cueCollect() {
        playEffect(FxCue.COLLECT)
        vibrate(25L)
    }

    override fun cueLeap() {
        playEffect(FxCue.LEAP)
    }

    override fun cueInsufficient() {
        playEffect(FxCue.SHORTAGE)
        vibrate(90L)
    }

    override fun cueVictory() {
        playEffect(FxCue.VICTORY)
        vibrate(200L)
    }


    private fun playMusic(channel: TrackGroup) {
        if (activeGroup == channel && activePlayers[channel]?.isPlaying == true) {
            return
        }

        activeGroup?.let { active ->
            activePlayers[active]?.let { player ->
                if (player.isPlaying) player.pause()
                player.seekTo(0)
            }
        }

        val player = ensurePlayer(channel)
        activeGroup = if (player != null) channel else null
        player?.let {
            it.setVolume(musicLevel, musicLevel)
            it.isLooping = true
            try {
                it.start()
            } catch (_: IllegalStateException) {
                activePlayers.remove(channel)
            }
        }
    }

    private fun ensurePlayer(channel: TrackGroup): MediaPlayer? {
        activePlayers[channel]?.let { existing ->
            return existing
        }

        val resId = when (channel) {
            TrackGroup.MENU_THEME -> menuTrackRes
            TrackGroup.PLAY_THEME -> playTrackRes
        }

        if (resId == 0) return null

        return runCatching { MediaPlayer.create(context, resId) }
            .getOrNull()
            ?.also { player ->
                player.isLooping = true
                player.setVolume(musicLevel, musicLevel)
                activePlayers[channel] = player
            }
    }

    private fun playEffect(effect: FxCue) {
        if (fxLevel <= 0f) return

        val resId = cueResMap[effect] ?: 0
        if (resId == 0) return

        val sampleId = loadedHandles[effect]
        if (sampleId != null) {
            if (preparedCues.contains(sampleId)) {
                fxPool.play(sampleId, fxLevel, fxLevel, 1, 0, 1f)
            } else {
                queuedCues += sampleId
            }
            return
        }

        val loadId = fxPool.load(context, resId, 1)
        if (loadId != 0) {
            loadedHandles[effect] = loadId
            queuedCues += loadId
        }
    }

    private fun resolveRaw(name: String): Int {
        if (name.isBlank()) return 0
        return context.resources.getIdentifier(name, "raw", context.packageName)
    }

    private fun Int.toVolume(): Float = (this.coerceIn(0, 100) / 100f).coerceIn(0f, 1f)

    private fun vibrate(durationMs: Long) {
        if (!hapticsEnabled) return
        val vib = hapticEngine ?: return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vib.vibrate(VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vib.vibrate(durationMs)
        }
    }
}
