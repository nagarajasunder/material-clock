package com.geekydroid.materialclock.ui.timer.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.geekydroid.materialclock.R
import com.geekydroid.materialclock.ui.theme.timerInputSelectedColor
import com.geekydroid.materialclock.ui.theme.weekDaySelectedColor

@Composable
fun TimerCard(
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "59m 38s Timer", style = MaterialTheme.typography.headlineSmall)
                Card(
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(containerColor = weekDaySelectedColor)
                ) {
                    Icon(
                        modifier = Modifier
                            .size(24.dp)
                            .padding(4.dp),
                        painter = painterResource(id = R.drawable.close_24px),
                        contentDescription = null
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(250.dp),
                    strokeWidth = 12.dp,
                    trackColor = Color.LightGray,
                    progress = 0.7f,
                    strokeCap = StrokeCap.Round,
                    color = timerInputSelectedColor
                )

            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    modifier = Modifier
                        .weight(0.5f)
                        .fillMaxWidth()
                        .padding(4.dp),
                    onClick = { /*TODO*/ },
                    shape = CircleShape
                ) {
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = "+1:00",
                        style = MaterialTheme.typography.headlineSmall,
                    )
                }
                Button(
                    modifier = Modifier
                        .weight(0.5f)
                        .fillMaxWidth()
                        .padding(4.dp),
                    onClick = { /*TODO*/ },
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
                ) {
                    Icon(
                        modifier = Modifier.padding(20.dp),
                        painter = painterResource(id = R.drawable.pause_24px),
                        contentDescription = null,
                        tint = Color.Black
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TimerCardPreview() {

    TimerCard()
}