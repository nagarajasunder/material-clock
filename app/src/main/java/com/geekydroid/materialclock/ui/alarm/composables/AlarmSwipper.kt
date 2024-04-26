package com.geekydroid.materialclock.ui.alarm.composables

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AlarmSwipper(
    onSwipeLeft: () -> Unit,
    onSwipeRight: () -> Unit
) {
    BoxWithConstraints {

        var swipeEnabled by remember {
            mutableStateOf(true)
        }
        val animatableLeft = remember {
            Animatable(0F)
        }
        val animatableRight = remember {
            Animatable(0F)
        }
        var refreshKey by remember {
            mutableStateOf(false)
        }

        val density = LocalDensity.current

        val swipeBoxSize = 90.dp
        val swipeBoxPadding = 12.dp

        val swipeSize = with(LocalDensity.current) {
            maxWidth.div(2).minus(swipeBoxSize / 2f).minus(swipeBoxPadding * 2f).toPx()
        }

        val swipeAnimSize = with(LocalDensity.current) {
            maxWidth.div(2).minus(swipeBoxPadding).toPx()
        }

        val anchorPoints = DraggableAnchors {
            0f at 0F
            1F at -swipeSize
            2F at swipeSize
        }
        val anchoredDraggableState = remember {
            AnchoredDraggableState(
                initialValue = 0F,
                anchors = anchorPoints,
                velocityThreshold = { with(density) { 50.dp.toPx() } },
                positionalThreshold = { distance: Float -> distance * 0.25f },
                animationSpec = tween(500)
            )
        }


        val textAlpha = if (anchoredDraggableState.currentValue != 0F) {
            0F
        } else if (anchoredDraggableState.progress == 1F) {
            1F
        } else {
            1F - anchoredDraggableState.progress
        }

        LaunchedEffect(key1 = refreshKey) {
            animatableLeft.animateTo(swipeAnimSize, animationSpec = tween(2000))
            animatableLeft.animateTo(0F, animationSpec = tween(0))
            animatableRight.animateTo(swipeAnimSize, animationSpec = tween(2000))
            animatableRight.animateTo(0f, animationSpec = tween(0))
            refreshKey = !refreshKey
        }

        LaunchedEffect(key1 = anchoredDraggableState.currentValue) {
            if (anchoredDraggableState.currentValue == 1F) {
                swipeEnabled = false
                onSwipeLeft()
            } else if (anchoredDraggableState.currentValue == 2F) {
                swipeEnabled = false
                onSwipeRight()
            }
        }

        Box(
            modifier = Modifier
                .padding(swipeBoxPadding)
                .fillMaxWidth()
                .height(100.dp)
                .clip(CircleShape)
                .background(Color.DarkGray.copy(alpha = 0.8f))
                .drawWithContent {
                    if (anchoredDraggableState.offset == 0F) {
                        rotate(180f) {
                            drawRoundRect(
                                topLeft = Offset(
                                    size.width / 2F - (swipeBoxSize.value / 2f),
                                    size.height * 0.1f
                                ),
                                size = Size(animatableLeft.value, size.height * 0.8f),
                                color = Color.LightGray.copy(alpha = 0.5f),
                                cornerRadius = CornerRadius(100F)
                            )
                        }
                        drawRoundRect(
                            topLeft = Offset(
                                size.width / 2F - (swipeBoxSize.value / 2f),
                                size.height * 0.1f
                            ),
                            size = Size(animatableRight.value, size.height * 0.8f),
                            color = Color.LightGray.copy(alpha = 0.5f),
                            cornerRadius = CornerRadius(100F)
                        )
                    }
                    drawContent()
                },
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, end = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier.graphicsLayer {
                        alpha = textAlpha
                    },
                    text = "Snooze",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
                Text(
                    modifier = Modifier.graphicsLayer {
                        alpha = textAlpha
                    },
                    text = "Stop",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
            Box(
                modifier = Modifier
                    .offset {
                        IntOffset(anchoredDraggableState.offset.roundToInt(), 0)
                    }
                    .width(swipeBoxSize)
                    .height(80.dp)
                    .clip(CircleShape)
                    .anchoredDraggable(
                        state = anchoredDraggableState,
                        enabled = swipeEnabled,
                        orientation = Orientation.Horizontal
                    )
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                AnimatedVisibility(
                    enter = fadeIn(),
                    exit = fadeOut(),
                    visible = anchoredDraggableState.currentValue == 0f
                ) {
                    Icon(Icons.Default.Alarm, contentDescription = null)
                }
                AnimatedVisibility(
                    enter = fadeIn(),
                    exit = fadeOut(),
                    visible = anchoredDraggableState.currentValue != 0F
                ) {
                    Icon(Icons.Default.Check, contentDescription = null)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AlarmSwipperPreview() {
    val context = LocalContext.current
    Box(modifier = Modifier.fillMaxWidth()) {
        var swipeEnabled by remember {
            mutableStateOf(true)
        }
        AlarmSwipper(
            onSwipeLeft = {
                swipeEnabled = false
                Toast.makeText(context, "Swipe left", Toast.LENGTH_SHORT).show()
            },
            onSwipeRight = {
                swipeEnabled = false
                Toast.makeText(context, "Swipe right", Toast.LENGTH_SHORT).show()
            }
        )
    }
}
