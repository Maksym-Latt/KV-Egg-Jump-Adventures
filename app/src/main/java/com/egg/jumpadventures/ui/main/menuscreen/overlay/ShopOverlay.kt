package com.egg.jumpadventures.ui.main.menuscreen.overlay

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.egg.jumpadventures.R
import com.egg.jumpadventures.ui.main.component.GradientOutlinedText
import com.egg.jumpadventures.ui.main.component.OrangePrimaryButton
import com.egg.jumpadventures.ui.main.component.SecondaryBackButton
import com.egg.jumpadventures.ui.main.menuscreen.model.EggSkin

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShopOverlay(
    skins: List<EggSkin>,
    owned: Set<EggSkin>,
    selected: EggSkin,
    coins: Int,
    onClose: () -> Unit,
    onSelect: (EggSkin) -> Unit,
    onBuy: (EggSkin) -> Unit,
) {
    val cardShape = RoundedCornerShape(24.dp)
    val panelGrad = Brush.verticalGradient(listOf(Color(0xFFFFE6FE), Color(0xFFCC5EFF)))
    val pagerState = rememberPagerState(initialPage = skins.indexOf(selected).coerceAtLeast(0)) {
        skins.size
    }

    LaunchedEffect(selected) {
        val index = skins.indexOf(selected)
        if (index >= 0) {
            pagerState.scrollToPage(index)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xCC000000), Color(0x99000000))))
    ) {
        SecondaryBackButton(
            onClick = onClose,
            modifier = Modifier
                .padding(start = 20.dp, top = 24.dp)
                .size(60.dp)
        )

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            GradientOutlinedText(
                text = "Skin Shop",
                fontSize = 32.sp,
                gradientColors = listOf(Color.White, Color.White)
            )

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.72f)
            ) { page ->
                val skin = skins[page]
                ShopItemCard(
                    skin = skin,
                    owned = owned.contains(skin),
                    selected = selected == skin,
                    coins = coins,
                    onBuy = { onBuy(skin) },
                    onSelect = { onSelect(skin) },
                    panelGrad = panelGrad,
                    shape = cardShape
                )
            }

            PagerIndicator(
                count = skins.size,
                current = pagerState.currentPage,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            OrangePrimaryButton(
                text = "Back",
                onClick = onClose,
                modifier = Modifier.fillMaxWidth(0.6f)
            )
        }
    }
}

@Composable
private fun ShopItemCard(
    skin: EggSkin,
    owned: Boolean,
    selected: Boolean,
    coins: Int,
    onBuy: () -> Unit,
    onSelect: () -> Unit,
    panelGrad: Brush,
    shape: RoundedCornerShape,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(panelGrad)
            .border(2.dp, Color(0x66FFFFFF), shape)
            .padding(horizontal = 18.dp, vertical = 18.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Image(
            painter = androidx.compose.ui.res.painterResource(id = skin.shopPreview),
            contentDescription = null,
            modifier = Modifier.size(140.dp)
        )

        Text(
            text = skin.title,
            color = Color.White,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 22.sp
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = androidx.compose.ui.res.painterResource(id = R.drawable.coin),
                contentDescription = null,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "x${skin.price}",
                color = Color(0xFFFFF4CA),
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
        }
        val buttonText = when {
            selected -> "Selected"
            owned -> "Choose"
            else -> "Buy"
        }
        OrangePrimaryButton(
            text = buttonText,
            onClick = {
                when {
                    selected -> onSelect()
                    owned -> onSelect()
                    coins >= skin.price -> onBuy()
                    else -> {},
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        if (!owned && coins < skin.price) {
            Text(
                text = "Need ${skin.price - coins} more coins",
                color = Color(0xFFFFF4CA),
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun PagerIndicator(count: Int, current: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(count) { index ->
            val isActive = index == current
            Box(
                modifier = Modifier
                    .size(if (isActive) 12.dp else 8.dp)
                    .clip(CircleShape)
                    .background(if (isActive) Color.White else Color(0x55FFFFFF))
            )
        }
    }
}
