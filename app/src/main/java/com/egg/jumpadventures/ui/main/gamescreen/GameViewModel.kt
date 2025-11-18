package com.egg.jumpadventures.ui.main.gamescreen

import androidx.lifecycle.ViewModel
import com.egg.jumpadventures.ui.main.menuscreen.model.EggSkin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.abs
import kotlin.random.Random

private const val PLATFORM_COUNT = 8
private const val COIN_COUNT = 6
private const val GRAVITY = 0.0014f
private const val JUMP_FORCE = -0.028f
private const val CAMERA_ANCHOR = 0.35f
private const val PLAYER_FALL_LIMIT = 1.05f
private const val HEIGHT_PER_UNIT = 240
private const val PLATFORM_HIT_X = 0.18f
private const val PLATFORM_HIT_Y = 0.06f
private const val MIN_PLATFORM_VERTICAL_GAP = 0.08f
private const val MAX_PLATFORM_VERTICAL_GAP = 0.18f
private const val MIN_PLATFORM_HORIZONTAL_GAP = 0.22f
private const val PLATFORM_RESPAWN_ATTEMPTS = 8
private const val BASE_TARGET = 100
private const val TARGET_PER_LEVEL = 50

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
                hasWon = false,
                height = 0,
                coins = 0,
                platforms = seedPlatforms(),
                coinsOnField = seedCoins(),
                playerX = 0.5f,
                playerY = 0.8f,
                verticalVelocity = JUMP_FORCE,
                targetCoins = targetForLevel(it.level)
            )
        }
    }

    fun startRun() {
        _state.update {
            it.copy(
                running = true,
                isPaused = false,
                isGameOver = false,
                hasWon = false,
                showIntro = false,
                height = 0,
                coins = 0,
                platforms = seedPlatforms(),
                coinsOnField = seedCoins(),
                playerX = 0.5f,
                playerY = 0.8f,
                verticalVelocity = JUMP_FORCE,
                targetCoins = targetForLevel(it.level)
            )
        }
    }

    fun pause() {
        _state.update { state ->
            if (!state.running || state.isGameOver || state.hasWon) state else state.copy(isPaused = true)
        }
    }

    fun resume() {
        _state.update { state ->
            if (state.isGameOver || state.hasWon) state else state.copy(isPaused = false, running = true)
        }
    }

    fun stopAndShowGameOver() {
        _state.update { it.copy(running = false, isPaused = false, isGameOver = true, hasWon = false) }
    }

    fun retry() {
        showIntroOnEnter()
    }

    fun advanceToNextLevel() {
        _state.update { current ->
            val nextLevel = current.level + 1
            current.copy(
                level = nextLevel,
                targetCoins = targetForLevel(nextLevel),
                showIntro = true,
                running = false,
                isPaused = false,
                isGameOver = false,
                hasWon = false,
                height = 0,
                coins = 0,
                platforms = seedPlatforms(),
                coinsOnField = seedCoins(),
                playerX = 0.5f,
                playerY = 0.8f,
                verticalVelocity = JUMP_FORCE
            )
        }
    }

    fun movePlayer(delta: Float) {
        _state.update { state ->
            if (!state.running || state.isPaused || state.isGameOver) return@update state
            val moved = wrapPosition(state.playerX + delta)
            state.copy(playerX = moved)
        }
    }

    fun tick() {
        _state.update { state ->
            if (!state.running || state.isPaused || state.isGameOver || state.hasWon) return@update state

            var playerX = wrapPosition(state.playerX)
            var playerY = state.playerY + state.verticalVelocity
            var velocityY = state.verticalVelocity + GRAVITY
            var platforms = state.platforms
            var coinsOnField = state.coinsOnField
            var coins = state.coins
            var height = state.height
            var running = state.running
            var isGameOver = state.isGameOver
            var hasWon = state.hasWon

            if (velocityY > 0f) {
                val landingPlatform = platforms.firstOrNull { platform ->
                    val closeX = wrapDelta(playerX, platform.x) < PLATFORM_HIT_X
                    val closeY = playerY in (platform.y - PLATFORM_HIT_Y)..platform.y
                    closeX && closeY
                }
                if (landingPlatform != null) {
                    velocityY = JUMP_FORCE
                    playerY = landingPlatform.y - 0.08f
                }
            }

            coinsOnField = coinsOnField.map { coin ->
                val dx = wrapDelta(playerX, coin.x)
                val dy = abs(playerY - coin.y)
                if (dx < 0.12f && dy < 0.12f) {
                    coins += 1
                    coin.copy(x = randomX(), y = respawnPlatformY())
                } else {
                    coin
                }
            }

            if (playerY < CAMERA_ANCHOR) {
                val shift = CAMERA_ANCHOR - playerY
                playerY = CAMERA_ANCHOR
                platforms = platforms.map { it.copy(y = it.y + shift) }
                coinsOnField = coinsOnField.map { it.copy(y = it.y + shift) }
                height += (shift * HEIGHT_PER_UNIT).toInt()
            }

            val updatedPlatforms = platforms.toMutableList()
            for (index in updatedPlatforms.indices) {
                if (updatedPlatforms[index].y > 1.05f) {
                    val withoutCurrent = updatedPlatforms.filterIndexed { i, _ -> i != index }
                    updatedPlatforms[index] = respawnPlatform(withoutCurrent, updatedPlatforms[index].id)
                }
            }
            platforms = updatedPlatforms

            coinsOnField = coinsOnField.map { coin ->
                if (coin.y > 1.05f) coin.copy(x = randomX(), y = respawnPlatformY()) else coin
            }

            if (coins >= state.targetCoins) {
                running = false
                hasWon = true
            }

            if (playerY > PLAYER_FALL_LIMIT && !hasWon) {
                running = false
                isGameOver = true
            }

            state.copy(
                height = height,
                coins = coins,
                platforms = platforms,
                coinsOnField = coinsOnField,
                playerX = playerX,
                playerY = playerY,
                verticalVelocity = velocityY,
                running = running,
                isGameOver = isGameOver,
                hasWon = hasWon
            )
        }
    }

    fun currentResult(): GameResult = _state.value.let {
        GameResult(
            height = it.height,
            coins = it.coins,
            level = it.level,
            targetCoins = it.targetCoins,
            hasWon = it.hasWon
        )
    }

    private fun wrapPosition(value: Float): Float {
        var wrapped = value
        while (wrapped < 0f) wrapped += 1f
        while (wrapped > 1f) wrapped -= 1f
        return wrapped
    }

    private fun wrapDelta(a: Float, b: Float): Float {
        val diff = abs(a - b)
        return minOf(diff, 1f - diff)
    }

    private fun seedPlatforms(): List<PlatformState> {
        val platforms = mutableListOf<PlatformState>()
        var currentY = 0.86f

        repeat(PLATFORM_COUNT) { index ->
            platforms += createPlatform(
                id = index,
                targetY = currentY,
                existing = platforms
            )
            currentY -= randomPlatformGap()
        }

        return platforms
    }

    private fun seedCoins(): List<CoinState> = List(COIN_COUNT) { index ->
        CoinState(id = index, x = randomX(), y = random.nextFloat())
    }

    private fun randomPlatformGap(): Float =
        MIN_PLATFORM_VERTICAL_GAP + random.nextFloat() * (MAX_PLATFORM_VERTICAL_GAP - MIN_PLATFORM_VERTICAL_GAP)

    private fun randomX(): Float = random.nextFloat().coerceIn(0.05f, 0.95f)

    private fun createPlatform(id: Int, targetY: Float, existing: List<PlatformState>): PlatformState {
        val clampedY = targetY.coerceAtMost(0.95f)
        val x = findNonOverlappingX(existing, clampedY)
        return PlatformState(id = id, x = x, y = clampedY)
    }

    private fun respawnPlatform(platforms: List<PlatformState>, platformId: Int): PlatformState {
        val highestY = platforms.minOfOrNull { it.y } ?: 0.86f
        val newY = highestY - randomPlatformGap()
        return createPlatform(id = platformId, targetY = newY, existing = platforms)
    }

    private fun findNonOverlappingX(existing: List<PlatformState>, y: Float): Float {
        repeat(PLATFORM_RESPAWN_ATTEMPTS) {
            val candidate = randomX()
            val overlaps = existing.any { platform ->
                val closeX = wrapDelta(candidate, platform.x) < MIN_PLATFORM_HORIZONTAL_GAP
                val closeY = abs(y - platform.y) < MIN_PLATFORM_VERTICAL_GAP
                closeX && closeY
            }
            if (!overlaps) return candidate
        }
        return randomX()
    }

    private fun respawnPlatformY(): Float = -random.nextFloat() * 0.25f
}


data class GameUiState(
    val running: Boolean = false,
    val isPaused: Boolean = false,
    val showIntro: Boolean = true,
    val isGameOver: Boolean = false,
    val hasWon: Boolean = false,
    val height: Int = 0,
    val coins: Int = 0,
    val level: Int = 1,
    val targetCoins: Int = targetForLevel(level),
    val selectedSkin: EggSkin = EggSkin.Classic,
    val platforms: List<PlatformState> = emptyList(),
    val coinsOnField: List<CoinState> = emptyList(),
    val playerX: Float = 0.5f,
    val playerY: Float = 0.8f,
    val verticalVelocity: Float = JUMP_FORCE,
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
    val level: Int,
    val targetCoins: Int,
    val hasWon: Boolean,
)

private fun targetForLevel(level: Int): Int = BASE_TARGET + (level - 1) * TARGET_PER_LEVEL
