package com.egg.jumpadventures.ui.main.menuscreen.overlay

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.egg.jumpadventures.ui.main.component.GradientOutlinedText
import com.egg.jumpadventures.ui.main.component.SecondaryBackButton
import com.egg.jumpadventures.ui.main.settings.SettingsViewModel

@Composable
fun SettingsOverlay(
    onClose: () -> Unit,
    onPrivacy: () -> Unit = {},
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val cardShape = RoundedCornerShape(18.dp)
    val panelGrad = Brush.verticalGradient(listOf(Color(0xFFFFE6FE), Color(0xFFCC5EFF)))
    val ui by viewModel.ui.collectAsStateWithLifecycle()

    Box(
        Modifier
            .fillMaxSize()
            .background(Color(0x99000000))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClose() }
    ) {
        SecondaryBackButton(
            onClick = onClose,
            modifier = Modifier
                .padding(start = 16.dp, top = 24.dp)
                .size(60.dp)
        )

        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .clip(cardShape)
                .background(panelGrad)
                .border(2.dp, Color.White.copy(alpha = 0.4f), cardShape)
                .padding(vertical = 20.dp, horizontal = 18.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {}
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                GradientOutlinedText(
                    text = "Settings",
                    fontSize = 30.sp,
                    gradientColors = listOf(Color.White, Color.White)
                )

                ToggleRow(
                    title = "Music",
                    checked = ui.musicVolume > 0,
                    onCheckedChange = viewModel::toggleMusic
                )
                ToggleRow(
                    title = "Sounds",
                    checked = ui.soundVolume > 0,
                    onCheckedChange = viewModel::toggleSound
                )
                ToggleRow(
                    title = "Vibration",
                    checked = ui.vibrationEnabled,
                    onCheckedChange = viewModel::toggleVibration
                )

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Privacy policy",
                    color = Color.White,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0x33FFFFFF))
                        .padding(horizontal = 20.dp, vertical = 10.dp)
                        .clickable { onPrivacy() },
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
private fun ToggleRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0x33FFFFFF))
            .padding(horizontal = 14.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, color = Color.White, fontSize = 18.sp)
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color(0xFFFFF4CA),
                checkedTrackColor = Color(0xFFFF9DD0),
                uncheckedThumbColor = Color(0xFFE8E0FF),
                uncheckedTrackColor = Color(0xFF9D75C5)
            )
        )
    }
}
