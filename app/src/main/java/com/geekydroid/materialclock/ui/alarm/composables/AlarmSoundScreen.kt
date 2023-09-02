package com.geekydroid.materialclock.ui.alarm.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.geekydroid.materialclock.R
import com.geekydroid.materialclock.application.constants.Constants
import com.geekydroid.materialclock.application.constants.Constants.ARG_ALARM_ID
import com.geekydroid.materialclock.application.constants.Constants.ARG_HIDE_BOTTOM_BAR
import com.geekydroid.materialclock.application.notification.AlarmNotificationHelper
import com.geekydroid.materialclock.ui.alarm.viewmodel.AlarmSoundViewModel
import com.geekydroid.materialclock.ui.theme.blue

const val alarmSoundScreenRoute = "alarmsoundscreen/{$ARG_ALARM_ID}/{$ARG_HIDE_BOTTOM_BAR}"


fun buildAlarmSoundScreenRoute(alarmId:Int,hideBottomBar: Boolean = true) = "alarmsoundscreen/$alarmId/$hideBottomBar"

fun NavGraphBuilder.alarmSoundScreen(navHostController: NavHostController) {
    composable(
        route = alarmSoundScreenRoute,
        arguments = listOf(
            navArgument(name = ARG_HIDE_BOTTOM_BAR) {
                type =  NavType.BoolType
                defaultValue = true
            },
            navArgument(name = ARG_ALARM_ID) {
                type = NavType.IntType
                defaultValue = -1
            }
        )
    ) {
        AlarmSoundScreen(navHostController = navHostController)
    }
}

@Composable
fun AlarmSoundScreen(
    modifier: Modifier = Modifier,
    viewModel: AlarmSoundViewModel = hiltViewModel(),
    navHostController: NavHostController,
) {

    val context = LocalContext.current
    val currentAlarmSoundIdx by viewModel.currentAlarmSoundIdx.collectAsStateWithLifecycle()
    var playIndex by remember {
        mutableStateOf(-1)
    }
    val alarmId = navHostController.currentBackStackEntry?.arguments?.getInt(ARG_ALARM_ID)
    if (alarmId == null) {
        navHostController.popBackStack()
        return
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.setAlarmId(alarmId)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Icon(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable {
                        navHostController.popBackStack()
                    },
                painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                contentDescription = null
            )
        }
        Text(
            text = stringResource(R.string.alarm_sound),
            modifier = Modifier.padding(top = 16.dp),
            style = MaterialTheme.typography.displaySmall
        )
        Text(
            text = stringResource(R.string.device_sounds),
            modifier = Modifier.padding(top = 16.dp),
            style = MaterialTheme.typography.bodyMedium.copy(color = blue)
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {

            items(Constants.alarmSoundsList.size) {
                val alarmSound = Constants.alarmSoundsList[it]
                AlarmSoundCard(
                    soundLabel = alarmSound.soundLabel,
                    onSoundClicked = { isPlaying ->
                        if (!isPlaying) {
                            AlarmNotificationHelper.playSound(context,alarmSound.soundId)
                            playIndex = it
                        }
                        else {
                            AlarmNotificationHelper.stopSound()
                            playIndex = -1
                        }
                        viewModel.updateAlarmSoundId(soundId =alarmSound.soundId)
                    },
                    isPlaying = playIndex == it,
                    isSelected = currentAlarmSoundIdx == it
                )
            }
        }
    }

}

@Composable
fun AlarmSoundCard(
    modifier: Modifier = Modifier,
    soundLabel: String,
    isSelected: Boolean = false,
    isPlaying:Boolean,
    onSoundClicked: (Boolean) -> Unit
) {



    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(color = if (isSelected) Color(0xFF666666) else Color.Transparent)
            .clickable {
                onSoundClicked(isPlaying)
            }
    ) {
        Row(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.9f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.outline_notifications_24),
                    contentDescription = "notification"
                )

                Text(
                    text = soundLabel,
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(start = 12.dp),
                    style = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.Start),
                )

                AnimatedVisibility(visible = isPlaying) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_music_note_24),
                        modifier = Modifier.padding(horizontal = 4.dp),
                        contentDescription = null
                    )
                }
            }

            Row {
                AnimatedVisibility(visible = isSelected) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_check_circle_24),
                        modifier = Modifier.padding(horizontal = 4.dp),
                        contentDescription = "checked"
                    )
                }
            }
        }
    }
}