package com.geekydroid.materialclock.ui.timer.composables

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.geekydroid.materialclock.R
import com.geekydroid.materialclock.ui.theme.timerAddOneMinColor
import com.geekydroid.materialclock.ui.theme.timerProgressColor
import com.geekydroid.materialclock.ui.theme.timerStopButtonColor
import com.geekydroid.materialclock.ui.theme.weekDaySelectedColor

@Composable
fun TimerCard(
    modifier: Modifier = Modifier,
    timerLabel:String,
    timerText:String,
    timerProgress:Float
) {

    val timerProgressAnimated by animateFloatAsState(
        targetValue = timerProgress,
        label = "",
        animationSpec = spring(
            dampingRatio = 1f,
            stiffness = Spring.StiffnessVeryLow
        )
    )

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
                Text(text = timerLabel, style = MaterialTheme.typography.headlineSmall)
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
                    trackColor = Color.Gray,
                    progress = timerProgressAnimated,
                    strokeCap = StrokeCap.Round,
                    color = timerProgressColor
                )
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = timerText,
                        style = MaterialTheme.typography.displayMedium,
                        maxLines = 2
                    )
                    Icon(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {

                            },
                        painter = painterResource(id = R.drawable.restart_alt_24px),
                        contentDescription = stringResource(id = R.string.reset),
                        tint = timerProgressColor
                    )

                }

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
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = timerAddOneMinColor)
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
                    colors = ButtonDefaults.buttonColors(containerColor = timerStopButtonColor)
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

    TimerCard(
        timerText = "100:59:58",
        timerLabel = "5m Timer",
        timerProgress = 90.0f
    )
}