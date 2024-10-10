package com.semonemo.presentation.component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.semonemo.presentation.theme.GunMetal
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.theme.White
import com.semonemo.presentation.theme.WhiteGray
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

private const val TAG = "CustomCountdownTimer"
@Composable
fun CustomCountdownTimer(deadline: LocalDateTime) {
    var currentTime by remember { mutableStateOf(LocalDateTime.now()) }
    var remainingTime by remember { mutableStateOf(calculateRemainingTime(currentTime, deadline).first) }
    var breakKey by remember { mutableStateOf(calculateRemainingTime(currentTime, deadline).second) }
    LaunchedEffect(key1 = currentTime) {
        while (true) {
            delay(1000)
            currentTime = LocalDateTime.now()
            remainingTime = calculateRemainingTime(currentTime, deadline).first
            breakKey = calculateRemainingTime(currentTime, deadline).second
            if(!breakKey) {
                break
            }
        }
    }
    if(!breakKey) {
        remainingTime = RemainingTime(0, 0, 0, 0)
    }
    Row(
        modifier =
        Modifier
            .background(Color.Unspecified)
            .padding(2.dp),

        verticalAlignment = Alignment.CenterVertically
    ) {
        TimeUnitBox(String.format("%02d", remainingTime.hours))
        Separator()
        TimeUnitBox(String.format("%02d", remainingTime.minutes))
        Separator()
        TimeUnitBox(String.format("%02d", remainingTime.seconds))
    }
}

@Composable
fun TimeUnitBox(text: String) {
    Box(
        modifier =
        Modifier
            .background(White)
            .wrapContentSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = Typography.bodyMedium.copy(fontSize = 12.sp, fontFeatureSettings = "tnum"),
            color = GunMetal,
        )
    }
}

@Composable
fun Separator() {
    Box(
        modifier =
        Modifier
            .background(White)
            .wrapContentSize()
            .padding(bottom = 1.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = ":",
            fontSize = 12.sp,
            style = Typography.bodyLarge,
            color = GunMetal,
        )
    }
}

data class RemainingTime(
    val days: Long,
    val hours: Long,
    val minutes: Long,
    val seconds: Long,
)

fun calculateRemainingTime(
    current: LocalDateTime,
    deadline: LocalDateTime,
): Pair<RemainingTime, Boolean> {
    val duration = ChronoUnit.SECONDS.between(current, deadline)
    val days = duration / (24 * 3600)
    val hours = (duration % (24 * 3600)) / 3600
    val minutes = (duration % 3600) / 60
    val seconds = duration % 60

    Log.d(TAG, "calculateRemainingTime: $days, $hours, $minutes, $seconds")
    if(days < 0L || hours < 0L || minutes < 0L || seconds < 0L) {
        return Pair(RemainingTime(0, 0, 0, 0), false)

    }
    return Pair(RemainingTime(days, hours, minutes, seconds), true)
}

@Preview(showBackground = true)
@Composable
fun CountdownTimerPreview() {
    val deadline =
        LocalDateTime
            .now()
            .plusDays(0)
            .plusHours(12)
            .plusMinutes(21)
            .plusSeconds(54)
    CustomCountdownTimer(deadline)
}

@Preview(showBackground = true)
@Composable
fun CountdownTimerNearEndPreview() {
    val deadline = LocalDateTime.now().plusHours(5)
    CustomCountdownTimer(deadline)
}

@Preview(showBackground = true)
@Composable
fun CountdownTimerDDayPreview() {
    val deadline =
        LocalDateTime
            .now()
            .plusHours(23)
            .plusMinutes(11)
            .plusSeconds(59)
    CustomCountdownTimer(deadline)
}
