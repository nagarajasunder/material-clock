package com.geekydroid.materialclock.ui.stopwatch.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun StopwatchScreen(
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Stopwatch screen")
    }

}

@Preview
@Composable
fun WeekRow() {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        WeekComponent(
            modifier = Modifier.padding(4.dp),
            text = "S"
        )
        WeekComponent(
            modifier = Modifier.padding(4.dp),
            text = "M"
        )
        WeekComponent(
            modifier = Modifier.padding(4.dp),
            text = "T"
        )
        WeekComponent(
            modifier = Modifier.padding(4.dp),
            text = "W"
        )
        WeekComponent(
            modifier = Modifier.padding(4.dp),
            text = "T"
        )
        WeekComponent(
            modifier = Modifier.padding(4.dp),
            text = "F"
        )
        WeekComponent(
            modifier = Modifier.padding(4.dp),
            text = "S"
        )

    }
}


@Composable
fun WeekComponent(modifier: Modifier = Modifier, text: String) {
    Card(
        modifier = modifier,
        shape = CircleShape
    ) {
        Text(text = text, modifier = Modifier.padding(8.dp))
    }
}