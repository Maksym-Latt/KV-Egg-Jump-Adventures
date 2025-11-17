package com.egg.jumpadventures.ui.main.gamescreen

import androidx.activity.compose.BackHandler
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.egg.feedthechick.audio.rememberAudioController
import com.egg.feedthechick.ui.main.gamescreen.engine.ChickState
import com.egg.feedthechick.ui.main.gamescreen.engine.GameEvent
import kotlinx.coroutines.delay

// ----------------------- Composable -----------------------
@Composable
fun GameScreen(
    onExitToMenu: (Int) -> Unit,
    viewModel: GameViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {

    // ----------------------- Enter Intro Every Time -----------------------
    LaunchedEffect(Unit) {
        viewModel.showIntroOnEnter()
    }

    // ----------------------- Context -----------------------
    val context = LocalContext.current

    // ----------------------- Audio -----------------------
    val audio = rememberAudioController()
    

    // ----------------------- State -----------------------
    val state by viewModel.state.collectAsState()

    // ----------------------- FX State -----------------------
    data class LostFx(val id: Long, val at: Offset)
    val lostFx = remember { mutableStateListOf<LostFx>() }
    var fxCounter by remember { mutableLongStateOf(0L) }

    // ----------------------- Audio + FX events -----------------------
    LaunchedEffect(audio) {
        viewModel.events.collect { event ->
            when (event) {
                GameEvent.FeedSuccess -> audio.playGameFeed()
                GameEvent.Mistake -> audio.playGameLose()
                GameEvent.GameWon -> audio.playGameWin()
                is GameEvent.LostSeedOverflow -> {
                    lostFx += LostFx(id = fxCounter++, at = event.at)
                }
            }
        }
    }

    // ----------------------- Pause on App Background -----------------------
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_PAUSE) {
                val s = viewModel.state.value
                if (!s.showWin && !s.showSettings) {
                    viewModel.pauseAndOpenSettings()
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // ----------------------- Back button -----------------------
    BackHandler {
        if (!state.showSettings && !state.showWin) {
            viewModel.pauseAndOpenSettings()
        } else {
        }
    }

    // ----------------------- State -----------------------
    LaunchedEffect(state.chickState) {
        if (state.chickState != ChickState.Idle) {
            delay(1000)
            viewModel.acknowledgeChickIdle()
        }
    }

    // ----------------------- Layout -----------------------
    Surface(color = MaterialTheme.colorScheme.background) {

    }
}

