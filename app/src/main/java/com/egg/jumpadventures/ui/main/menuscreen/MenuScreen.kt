package com.egg.jumpadventures.ui.main.menuscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.egg.feedthechick.R
import com.egg.feedthechick.ui.main.component.SecondaryIconButton
import com.egg.feedthechick.ui.main.component.StartPrimaryButton

@Composable
fun MenuScreen(
    onStartGame: () -> Unit,
    lastScore: Int?,
    onOpenSettings: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.bg_menu),
            contentDescription = null,
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.Crop
        )

        SecondaryIconButton(
            onClick = onOpenSettings,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(horizontal = 24.dp, vertical = 24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Open settings",
                tint = Color.White,
                modifier = Modifier.fillMaxSize(0.8f)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp, vertical = 48.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.title),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(0.8f),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            Image(
                painter = painterResource(id = R.drawable.chicken_win),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(0.65f),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(32.dp))

            StartPrimaryButton(
                text = "Play",
                onClick = onStartGame,
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(60.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (lastScore != null) {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Last time: $lastScore corn",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color(0xFF6D3B1A),
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }

        }
    }
}
