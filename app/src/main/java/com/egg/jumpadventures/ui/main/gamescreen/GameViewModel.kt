package com.egg.jumpadventures.ui.main.gamescreen

import androidx.lifecycle.ViewModel
import com.egg.jumpadventures.ui.main.menuscreen.model.EggSkin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

private const val PLATFORM_COUNT = 6
private const val COIN_COUNT = 8

class GameViewModel : ViewModel() {

    private val random = Random(System.currentTimeMillis())

    private val _state = MutableStateFlow(
        GameUiState(
            platforms = seedPlatforms(),
            coinsOnField = seedCoins(),
        )
    )
    val state: StateFlow<GameUiState> = _state

    fun setSkin(skin: EggSkin) {
        _state.update { it.copy(selectedSkin = skin) }
    }

    fun showIntroOnEnter() {
        _state.update {
            it.copy(
                showIntro = true,
                running = false,
                isPaused = false,
                isGameOver = false,
                height = 0,
                coins = 0,
                platforms = seedPlatforms(),
                coinsOnField = seedCoins()
            )
        }
    }

    fun startRun() {
        _state.update {
            it.copy(
                running = true,
                isPaused = false,
                isGameOver = false,
                showIntro = false,
                height = 0,
                coins = 0,
                platforms = seedPlatforms(),
                coinsOnField = seedCoins()
            )
        }
    }

    fun pause() {
        _state.update { state ->
            if (!state.running || state.isGameOver) state else state.copy(isPaused = true)
        }
    }

    fun resume() {
        _state.update { state ->
            if (state.isGameOver) state else state.copy(isPaused = false, running = true)
        }
    }

    fun stopAndShowGameOver() {
        _state.update { it.copy(running = false, isPaused = false, isGameOver = true) }
    }

    fun retry() {
        showIntroOnEnter()
    }

    fun tick() {
        _state.update { state ->
            if (!state.running || state.isPaused || state.isGameOver) return@update state

            var collectedCoins = state.coins
            val movedCoins = state.coinsOnField.map { coin ->
                val newY = coin.y + 0.08f
                when {
                    newY in 0.45f..0.55f -> {
                        collectedCoins += 1
                        coin.copy(x = randomX(), y = respawnY())
                    }

                    newY > 1.1f -> coin.copy(x = randomX(), y = respawnY())
                    else -> coin.copy(y = newY)
                }
            }

            val movedPlatforms = state.platforms.map { platform ->
                val newY = platform.y + 0.12f
                if (newY > 1.1f) platform.copy(x = randomX(), y = respawnY()) else platform.copy(y = newY)
            }

            val newHeight = state.height + 3
            val reachedLimit = newHeight >= 180

            state.copy(
                height = newHeight,
                coins = collectedCoins,
                platforms = movedPlatforms,
                coinsOnField = movedCoins,
                running = state.running && !reachedLimit,
                isGameOver = state.isGameOver || reachedLimit
            )
        }
    }

    fun currentResult(): GameResult = _state.value.let { GameResult(height = it.height, coins = it.coins) }

    fun collectCoin(id: Int) {
        _state.update { state ->
            val coin = state.coinsOnField.firstOrNull { it.id == id } ?: return@update state
            state.copy(
                coins = state.coins + 1,
                coinsOnField = state.coinsOnField.map {
                    if (it.id == id) it.copy(x = randomX(), y = respawnY()) else it
                }
            )
        }
    }

    private fun seedPlatforms(): List<PlatformState> = List(PLATFORM_COUNT) { index ->
        PlatformState(id = index, x = randomX(), y = random.nextFloat())
    }

    private fun seedCoins(): List<CoinState> = List(COIN_COUNT) { index ->
        CoinState(id = index, x = randomX(), y = random.nextFloat())
    }

    private fun randomX(): Float = random.nextFloat().coerceIn(0.1f, 0.9f)

    private fun respawnY(): Float = -random.nextFloat() * 0.6f
}


data class GameUiState(
    val running: Boolean = false,
    val isPaused: Boolean = false,
    val showIntro: Boolean = true,
    val isGameOver: Boolean = false,
    val height: Int = 0,
    val coins: Int = 0,
    val selectedSkin: EggSkin = EggSkin.Classic,
    val platforms: List<PlatformState> = emptyList(),
    val coinsOnField: List<CoinState> = emptyList(),
)

data class PlatformState(
    val id: Int,
    val x: Float,
    val y: Float,
)

data class CoinState(
    val id: Int,
    val x: Float,
    val y: Float,
)

data class GameResult(
    val height: Int,
    val coins: Int,
)
