package com.egg.jumpadventures.ui.main.menuscreen

import androidx.lifecycle.ViewModel
import com.egg.jumpadventures.ui.main.gamescreen.GameResult
import com.egg.jumpadventures.ui.main.menuscreen.model.EggSkin
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    enum class Screen { Menu, Game }

    data class UiState(
        val screen: Screen = Screen.Menu,
        val lastHeight: Int = 0,
        val coins: Int = 0,
        val selectedSkin: EggSkin = EggSkin.Classic,
        val ownedSkins: Set<EggSkin> = setOf(EggSkin.Classic)
    )

    private val _ui = MutableStateFlow(UiState())
    val ui: StateFlow<UiState> = _ui.asStateFlow()

    val skins: List<EggSkin> = EggSkin.entries

    fun startGame() {
        _ui.update { it.copy(screen = Screen.Game) }
    }

    fun backToMenu(result: GameResult) {
        _ui.update {
            it.copy(
                screen = Screen.Menu,
                lastHeight = result.height,
                coins = (it.coins + result.coins).coerceAtLeast(0)
            )
        }
    }

    fun backToMenu() {
        _ui.update { it.copy(screen = Screen.Menu) }
    }

    fun selectSkin(skin: EggSkin) {
        _ui.update { state ->
            if (state.ownedSkins.contains(skin)) state.copy(selectedSkin = skin) else state
        }
    }

    fun buySkin(skin: EggSkin) {
        _ui.update { state ->
            if (state.ownedSkins.contains(skin)) return@update state
            if (state.coins < skin.price) return@update state
            state.copy(
                coins = state.coins - skin.price,
                ownedSkins = state.ownedSkins + skin,
                selectedSkin = skin
            )
        }
    }
}
