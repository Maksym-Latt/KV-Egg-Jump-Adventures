package com.egg.jumpadventures.ui.main.gamescreen.overlay

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.egg.jumpadventures.R
import com.egg.jumpadventures.ui.main.component.ChromaticStrokeLabel
import com.egg.jumpadventures.ui.main.component.AmberActionButton
import com.egg.jumpadventures.ui.main.component.PrimaryCoreActionButton
import com.egg.jumpadventures.ui.main.component.ActionStyle
import com.egg.jumpadventures.ui.main.gamescreen.GameResult

// ----------------------- Win overlay -----------------------
@Composable
fun WinOverlay(
    result: GameResult,
    onNextLevel: () -> Unit,
    onHome: () -> Unit,
) {
    ResultOverlayContainer {

        Spacer(modifier = Modifier.weight(1f))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ResultTitle(text = "YOU WIN!", background = Color(0xFFFFDCC9))
        }

        Spacer(modifier = Modifier.weight(0.5f))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            WinChickenWithCoins(
                chickSize = 140.dp,
                coinSize = 26.dp
            )

            Spacer(modifier = Modifier.height(12.dp))

            ResultStat(
                label = "+${result.coins}",
                iconRes = R.drawable.coin,
                background = Color(0xFFFFE8D4),
                contentColor = Color(0xFFF28E3A)
            )
        }
        Spacer(modifier = Modifier.weight(0.5f))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AmberActionButton(
                label = "TRY AGAIN",
                onPress = onNextLevel,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            PrimaryCoreActionButton(
                label = "MENU",
                onPress = onHome,
                modifier = Modifier.fillMaxWidth(),
                style = ActionStyle.PeachReturn
            )
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}


@Composable
fun GameOverOverlay(
    result: GameResult,
    targetCoins: Int,
    onRetry: () -> Unit,
    onHome: () -> Unit,
) {
    ResultOverlayContainer {
        Spacer(modifier = Modifier.weight(1f))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GameOverHeader()
        }

        Spacer(modifier = Modifier.weight(0.5f))
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GameOverStatsPanel(
                coins = result.coins,
                height = result.height
            )

            Spacer(modifier = Modifier.height(12.dp))

            GoalReminder(targetCoins = targetCoins)
        }
        Spacer(modifier = Modifier.weight(0.5f))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AmberActionButton(
                label = "TRY AGAIN",
                onPress = onRetry,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            PrimaryCoreActionButton(
                label = "MENU",
                onPress = onHome,
                modifier = Modifier.fillMaxWidth(),
                style = ActionStyle.PeachReturn
            )
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun ResultOverlayContainer(content: @Composable ColumnScope.() -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Image(
            painter = painterResource(id = R.drawable.bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp, vertical = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            content = content
        )
    }
}

@Composable
private fun ResultTitle(text: String, background: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(background)
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        ChromaticStrokeLabel(
            caption = text,
            textScale = 28.sp,
            colorRange = listOf(Color(0xFFFFB15C), Color(0xFFFFB15C))
        )
    }
}

@Composable
private fun ResultStat(
    label: String,
    iconRes: Int,
    background: Color,
    contentColor: Color,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(background)
            .padding(horizontal = 18.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier.size(30.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = label,
            color = contentColor,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@Composable
private fun GoalReminder(targetCoins: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(Color(0xFFFFF2E6), Color(0xFFFFE0C8))
                )
            )
            .padding(horizontal = 16.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Goal: $targetCoins coins",
            color = Color(0xFF7B4A2D),
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun WinChickenWithCoins(
    chickSize: Dp,
    coinSize: Dp,
) {
    Box(
        modifier = Modifier
            .size(chickSize * 1.4f),
        contentAlignment = Alignment.Center
    ) {
        val coinPainter = painterResource(id = R.drawable.coin)

        Image(
            painter = coinPainter,
            contentDescription = null,
            modifier = Modifier
                .size(coinSize)
                .align(Alignment.TopCenter)
                .offset(y = 4.dp),
            contentScale = ContentScale.Fit
        )

        Image(
            painter = coinPainter,
            contentDescription = null,
            modifier = Modifier
                .size(coinSize)
                .align(Alignment.TopStart)
                .offset(x = 10.dp, y = 18.dp),
            contentScale = ContentScale.Fit
        )

        Image(
            painter = coinPainter,
            contentDescription = null,
            modifier = Modifier
                .size(coinSize)
                .align(Alignment.TopEnd)
                .offset(x = (-10).dp, y = 18.dp),
            contentScale = ContentScale.Fit
        )

        Image(
            painter = coinPainter,
            contentDescription = null,
            modifier = Modifier
                .size(coinSize)
                .align(Alignment.CenterStart)
                .offset(x = 4.dp, y = 18.dp),
            contentScale = ContentScale.Fit
        )

        Image(
            painter = coinPainter,
            contentDescription = null,
            modifier = Modifier
                .size(coinSize)
                .align(Alignment.CenterEnd)
                .offset(x = (-4).dp, y = 18.dp),
            contentScale = ContentScale.Fit
        )

        Image(
            painter = painterResource(id = R.drawable.title_chicken),
            contentDescription = null,
            modifier = Modifier.size(chickSize),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
private fun GameOverHeader() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "💔",
            fontSize = 40.sp
        )

        ResultTitle(text = "YOU FELL!", background = Color(0xFFFFD6CF))
    }
}

@Composable
private fun GameOverStatsPanel(
    coins: Int,
    height: Int,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFFFFEBDC))
            .padding(vertical = 14.dp, horizontal = 18.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.coin),
                        contentDescription = null,
                        modifier = Modifier.size(26.dp),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Coins",
                        color = Color(0xFFB0642D),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = coins.toString(),
                    color = Color(0xFFF28E3A),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Divider(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp),
                color = Color(0xFFFFD0B4).copy(alpha = 0.8f)
            )

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.platform),
                        contentDescription = null,
                        modifier = Modifier.size(26.dp),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Height",
                        color = Color(0xFF7B4A2D),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "$height m",
                    color = Color(0xFF7B4A2D),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }
}


@Preview(
    name = "WIN OVERLAY",
    showBackground = true,
    device = "spec:width=411dp,height=891dp"
)

@Preview(
    name = "WIN OVERLAY",
    showBackground = true,
    device = "spec:width=411dp,height=891dp"
)
@Composable
private fun PreviewWinOverlay() {
    WinOverlay(
        result = GameResult(
            coins = 250,
            height = 0,
            level = 3,
            targetCoins = 200,
            hasWon = true,
            finished = true
        ),
        onNextLevel = {},
        onHome = {}
    )
}

@Preview(
    name = "GAME OVER",
    showBackground = true,
    device = "spec:width=411dp,height=891dp"
)
@Composable
private fun PreviewGameOverOverlay() {
    GameOverOverlay(
        result = GameResult(
            coins = 201,
            height = 155,
            level = 3,
            targetCoins = 300,
            hasWon = false,
            finished = true
        ),
        targetCoins = 300,
        onRetry = {},
        onHome = {}
    )
}