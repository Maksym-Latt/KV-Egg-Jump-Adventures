package com.egg.jumpadventures.ui.main.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egg.jumpadventures.audio.AudioSuite
import com.egg.jumpadventures.data.settings.AudioPrefsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repo: AudioPrefsRepository,
    private val audio: AudioSuite
) : ViewModel() {

    private val _ui = MutableStateFlow(SettingsUiState())
    val ui: StateFlow<SettingsUiState> = _ui

    init {
        val music = repo.fetchMusicLevel()
        val sound = repo.fetchEffectLevel()
        val vibration = repo.isHapticsActive()
        _ui.value = SettingsUiState(
            musicVolume = music,
            soundVolume = sound,
            vibrationEnabled = vibration
        )
        audio.adjustMusicLevel(music)
        audio.adjustEffectLevel(sound)
        audio.toggleHaptics(vibration)
    }

    fun setMusicVolume(value: Int) {
        val v = value.coerceIn(0, 100)
        _ui.value = _ui.value.copy(musicVolume = v)
        viewModelScope.launch { repo.storeMusicLevel(v) }
        audio.adjustMusicLevel(v)
    }

    fun setSoundVolume(value: Int) {
        val v = value.coerceIn(0, 100)
        _ui.value = _ui.value.copy(soundVolume = v)
        viewModelScope.launch { repo.storeEffectLevel(v) }
        audio.adjustEffectLevel(v)
    }

    fun toggleMusic(enabled: Boolean) {
        setMusicVolume(if (enabled) 70 else 0)
    }

    fun toggleSound(enabled: Boolean) {
        setSoundVolume(if (enabled) 80 else 0)
    }

    fun toggleVibration(enabled: Boolean) {
        _ui.value = _ui.value.copy(vibrationEnabled = enabled)
        viewModelScope.launch { repo.storeHapticsState(enabled) }
        audio.toggleHaptics(enabled)
    }
}
