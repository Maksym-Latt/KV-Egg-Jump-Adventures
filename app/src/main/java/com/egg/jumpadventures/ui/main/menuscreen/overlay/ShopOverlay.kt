package com.egg.jumpadventures.ui.main.menuscreen.overlay

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
    val cardShape = RoundedCornerShape(22.dp)
    val panelGrad = Brush.verticalGradient(listOf(Color(0xFFFFE6FE), Color(0xFFCC5EFF)))

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
                .padding(start = 20.dp, top = 24.dp)
                .size(60.dp)
        )

        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .width(320.dp)
                .clip(cardShape)
                .background(panelGrad)
                .border(2.dp, Color(0x66FFFFFF), cardShape)
                .padding(vertical = 18.dp, horizontal = 14.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {}
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                GradientOutlinedText(
                    text = "Skin Shop",
                    fontSize = 32.sp,
                    gradientColors = listOf(Color.White, Color.White)
                )

                Spacer(Modifier.height(10.dp))

                skins.forEach { skin ->
                    ShopItemCard(
                        skin = skin,
                        owned = owned.contains(skin),
                        selected = selected == skin,
                        coins = coins,
                        onBuy = { onBuy(skin) },
                        onSelect = { onSelect(skin) }
                    )
                    Spacer(Modifier.height(10.dp))
                }
            }
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
) {
    val shape = RoundedCornerShape(18.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(Color(0x1AFFFFFF))
            .border(2.dp, Color(0x40FFFFFF), shape)
            .padding(horizontal = 12.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = androidx.compose.ui.res.painterResource(id = skin.shopPreview),
            contentDescription = null,
            modifier = Modifier.size(86.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = skin.title,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = androidx.compose.ui.res.painterResource(id = R.drawable.coin),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "x${skin.price}",
                    color = Color(0xFFFFF4CA),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
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
                        else -> {}
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            if (!owned && coins < skin.price) {
                Text(
                    text = "Need ${skin.price - coins} more coins",
                    color = Color(0xFFFFF4CA),
                    fontSize = 12.sp,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
