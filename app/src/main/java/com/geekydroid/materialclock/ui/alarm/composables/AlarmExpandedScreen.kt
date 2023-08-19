package com.geekydroid.materialclock.ui.alarm.composables

import androidx.compose.foundation.Image
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.geekydroid.materialclock.R

@Composable
fun AlarmExpandedScreen(
    modifier: Modifier = Modifier,
    alarmLabel: String,
) {

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Alarm",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(8.dp)
        )
        Text(
            text = alarmLabel,
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier.padding(8.dp)
        )
        AlarmActionCard(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 100.dp)
        )

    }

}


@Composable
fun AlarmActionCard(modifier: Modifier = Modifier) {

    var cardWidth = 0.dp

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = CircleShape,
        colors = CardDefaults.cardColors(
            containerColor = Color.LightGray,
            contentColor = Color.Black
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Snooze", style = MaterialTheme.typography.headlineSmall)
            Card(
                shape = CircleShape,
                colors = CardDefaults.cardColors(
                    contentColor = Color.White
                )
            ) {
                Box(
                    modifier = Modifier.size(72.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.baseline_access_alarm_24),
                        contentDescription = null
                    )
                }
            }
            Text(text = "Stop", style = MaterialTheme.typography.headlineSmall)
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AlarmExpandedScreenPreview() {
    SwipeToDismiss(
        state = rememberDismissState(),
        background = {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Gray)
            ) {
                Box(){}
            }
    }, dismissContent = { Text(text = "Hello") })
    //AlarmExpandedScreen(alarmLabel = "Hello world")
}