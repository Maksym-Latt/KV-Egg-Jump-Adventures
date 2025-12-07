package com.egg.jumpadventures.ui.main.menuscreen.overlay

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.egg.jumpadventures.R
import com.egg.jumpadventures.ui.main.component.ChromaticStrokeLabel
import com.egg.jumpadventures.ui.main.component.AmberActionButton
import com.egg.jumpadventures.ui.main.component.PrimaryCoreActionButton
import com.egg.jumpadventures.ui.main.component.ActionStyle
import com.egg.jumpadventures.ui.main.component.VectorRetreatNode
import com.egg.jumpadventures.ui.main.component.LaunchActionButton
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

    val screenBg = Brush.verticalGradient(
        listOf(
            Color(0xFF4C2FBF),
            Color(0xFFB53ACF),
            Color(0xFFFFB8A0)
        )
    )

    val panelGrad = Brush.verticalGradient(
        listOf(
            Color(0x40FFFFFF),
            Color(0x30FFFFFF)
        )
    )

    val pagerState = rememberPagerState(
        initialPage = skins.indexOf(selected).coerceAtLeast(0)
    ) { skins.size }

    LaunchedEffect(selected) {
        val index = skins.indexOf(selected)
        if (index >= 0) {
            pagerState.scrollToPage(index)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(screenBg)
            .windowInsetsPadding(WindowInsets.displayCutout),
    ) {
        VectorRetreatNode(
            trigger = onClose,
            modifier = Modifier
                .padding(start = 20.dp)
                .size(56.dp)
        )

        ShopCoinsBadge(
            coins = coins,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 24.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            ChromaticStrokeLabel(
                caption = "SKIN SHOP",
                textScale = 30.sp,
                colorRange = listOf(Color.White, Color.White)
            )

            Spacer(modifier = Modifier.weight(0.3f))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    pageSpacing = 18.dp
                ) { page ->
                    val skin = skins[page]

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally)
                    ) {
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
                }

                PagerIndicator(
                    count = skins.size,
                    current = pagerState.currentPage,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 16.dp)
                )
            }

            Spacer(modifier = Modifier.weight(0.1f))


            PrimaryCoreActionButton(
                label = "Back",
                onPress = onClose,
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(52.dp),
                style = ActionStyle.PeachReturn
            )
            Spacer(modifier = Modifier.weight(0.3f))
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
    var showNotEnough by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .requiredWidth(220.dp)
            .clip(shape)
            .background(panelGrad)
            .padding(horizontal = 18.dp, vertical = 18.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {

        Image(
            painter = painterResource(id = skin.shopPreview),
            contentDescription = null,
            modifier = Modifier.size(120.dp)
        )

        Text(
            text = skin.title,
            color = Color.White,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 18.sp
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.coin),
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "x${skin.price}",
                color = Color(0xFFFFF4CA),
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
        }


        val buttonClick: () -> Unit = {
            when {
                selected -> {
                    showNotEnough = false
                }
                owned -> {
                    showNotEnough = false
                    onSelect()
                }
                coins >= skin.price -> {
                    showNotEnough = false
                    onBuy()
                }
                else -> {
                    showNotEnough = true
                }
            }
        }

        when {
            selected -> {
                LaunchActionButton(
                    label = "SELECTED",
                    onPress = buttonClick,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            owned -> {
                AmberActionButton(
                    label = "EQUIP",
                    onPress = buttonClick,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            else -> {
                AmberActionButton(
                    label = "BUY",
                    onPress = buttonClick,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        AnimatedVisibility(
            visible = showNotEnough,
            modifier = Modifier.padding(top = 6.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(999.dp))
                    .background(Color(0xCCC74242))
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Not enough coins: need ${skin.price - coins} more",
                    color = Color.White,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
            }
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

@Composable
private fun ShopCoinsBadge(
    coins: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0x33FFFFFF))
            .border(1.dp, Color(0x55FFFFFF), RoundedCornerShape(24.dp))
            .padding(horizontal = 14.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.coin),
            contentDescription = null,
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = "x$coins",
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
        )
    }
}
