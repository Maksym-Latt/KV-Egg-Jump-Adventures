package com.egg.jumpadventures.ui.main.gamescreen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.egg.jumpadventures.R
import com.egg.jumpadventures.ui.main.component.SecondaryIconButton
import com.egg.jumpadventures.ui.main.gamescreen.overlay.GameOverOverlay
import com.egg.jumpadventures.ui.main.gamescreen.overlay.GameSettingsOverlay
import com.egg.jumpadventures.ui.main.gamescreen.overlay.IntroOverlay
import com.egg.jumpadventures.ui.main.menuscreen.model.EggSkin
import kotlinx.coroutines.delay

@Composable
fun GameScreen(
    skin: EggSkin,
    onExitToMenu: (GameResult) -> Unit,
    viewModel: GameViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(skin) {
        viewModel.setSkin(skin)
        viewModel.showIntroOnEnter()
    }

    LaunchedEffect(state.running, state.isPaused, state.isGameOver) {
        while (state.running && !state.isPaused && !state.isGameOver) {
            delay(280)
            viewModel.tick()
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_PAUSE) {
                val current = viewModel.state.value
                if (current.running && !current.isPaused && !current.isGameOver) {
                    viewModel.pause()
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    BackHandler(enabled = state.running && !state.showIntro) {
        if (!state.isPaused && !state.isGameOver) {
            viewModel.pause()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFFFF2FF), Color(0xFFFFE3D2), Color(0xFFFFD8C2))
                )
            )
    ) {
        GameField(state = state, onCollect = viewModel::collectCoin)

        GameHud(
            coins = state.coins,
            height = state.height,
            onPause = viewModel::pause
        )

        if (state.showIntro) {
            IntroOverlay(onStart = viewModel::startRun)
        }

        if (state.isPaused && !state.isGameOver) {
            GameSettingsOverlay(
                onResume = viewModel::resume,
                onRetry = { viewModel.retry() },
                onHome = { onExitToMenu(viewModel.currentResult()) }
            )
        }

        if (state.isGameOver) {
            GameOverOverlay(
                result = viewModel.currentResult(),
                onRetry = { viewModel.retry() },
                onHome = { onExitToMenu(viewModel.currentResult()) }
            )
        }
    }
}

@Composable
private fun GameHud(
    coins: Int,
    height: Int,
    onPause: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 20.dp),
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.coin),
                contentDescription = null,
                modifier = Modifier.size(28.dp)
            )
            Text(
                text = "x$coins",
                color = Color(0xFF7B4A2D),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(start = 6.dp)
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "${height}m",
                color = Color(0xFF7B4A2D),
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp
            )
            Text(
                text = "Height",
                color = Color(0xFF9B6B4A),
                fontSize = 12.sp
            )
        }

        SecondaryIconButton(
            onClick = onPause,
            modifier = Modifier.size(52.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Pause,
                contentDescription = "Pause",
                tint = Color.White,
                modifier = Modifier.fillMaxSize(0.72f)
            )
        }
    }
}

@Composable
private fun GameField(state: GameUiState, onCollect: (Int) -> Unit) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 72.dp, bottom = 24.dp)
    ) {
        val width = maxWidth
        val height = maxHeight

        state.platforms.forEach { platform ->
            val x = width * platform.x
            val y = height * platform.y
            Image(
                painter = painterResource(id = R.drawable.platform),
                contentDescription = null,
                modifier = Modifier
                    .size(width = 120.dp, height = 24.dp)
                    .offset(x = x - 60.dp, y = y)
            )
        }

        state.coinsOnField.forEach { coin ->
            val x = width * coin.x
            val y = height * coin.y
            Image(
                painter = painterResource(id = R.drawable.coin),
                contentDescription = null,
                modifier = Modifier
                    .size(28.dp)
                    .offset(x = x - 14.dp, y = y)
                    .clickable { onCollect(coin.id) }
            )
        }

        val playerY = height * 0.62f
        val playerX = width * 0.5f
        Image(
            painter = painterResource(id = state.selectedSkin.playerSprite),
            contentDescription = null,
            modifier = Modifier
                .width(96.dp)
                .height(96.dp)
                .offset(x = playerX - 48.dp, y = playerY)
        )
    }
}
