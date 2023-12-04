package com.geekydroid.materialclock.ui.timer.composables

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.geekydroid.materialclock.R
import com.geekydroid.materialclock.ui.theme.timerInputRemovalColor

@Composable
fun TimerInputButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: (String) -> Unit,
    color: Color = Color.Yellow,
    @DrawableRes iconRes: Int? = null
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val cardWidth = (screenWidth / 3)
    val cardHeight = ((screenHeight.value * 0.35) / 3).dp
    val circularCardSize = if (cardWidth < cardHeight) cardWidth else cardHeight
    Card(
        modifier = modifier
            .width(circularCardSize)
            .height(circularCardSize)
            .padding(2.dp)
            .clip(CircleShape)
            .clickable {
                onClick(text)
            },
        colors = CardDefaults.cardColors(containerColor = color),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (iconRes != null) {
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = stringResource(id = R.string.backspace),
                    tint = Color.Black
                )
            } else {
                Text(
                    text = text,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.displaySmall
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TimerButtonPreview() {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        TimerInputButton(
            modifier = Modifier.size(100.dp),
            iconRes = R.drawable.backspace,
            color = timerInputRemovalColor,
            text = "X", onClick = {

            })
        TimerInputButton(
            modifier = Modifier.size(100.dp),
            iconRes = R.drawable.backspace,
            color = timerInputRemovalColor,
            text = "X", onClick = {

            })
        TimerInputButton(
            modifier = Modifier.size(100.dp),
            color = timerInputRemovalColor,
            text = "5", onClick = {

            })
    }
}