package com.semonemo.presentation.screen.picture.camera.subscreen

import android.media.MediaPlayer
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.semonemo.presentation.theme.GunMetal
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.theme.WhiteGray
import com.semonemo.presentation.util.noRippleClickable
import kotlinx.coroutines.delay

/**
 * TODO
 *
 * @param totalTime  총 시간초
 * @param modifier
 * @param strokeWidth 없어지는 굵기
 * @param countdownColor 기본 색깔
 * @param backgroundColor 프로그래스바 색깔
 * @param onTimerEnd 끝났을 때 해야할 행동
 */

@Composable
fun CircularCountdownTimer(
    totalTime: Long,
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 5.dp,
    countdownColor: Color = GunMetal,
    backgroundColor: Color = WhiteGray,
    onTimerEnd: () -> Unit = {},
    timerTickSound: MediaPlayer,
) {
    var timeLeft by remember { mutableStateOf(totalTime) }
    var running by remember { mutableStateOf(false) }
    val progress by animateFloatAsState(
        targetValue = timeLeft / totalTime.toFloat(),
        animationSpec = tween(durationMillis = 1000, easing = LinearEasing),
        label = "",
    )
    LaunchedEffect(totalTime) {
        timeLeft = totalTime
        running = false
    }
    LaunchedEffect(running, timeLeft) {
        if (running && timeLeft > 0) {
            delay(1000L)
            timeLeft -= 1000L
            if (timerTickSound.isPlaying.not()) {
                timerTickSound.start()
            }
        } else if (timeLeft == 0L) {
            delay(1000L)
            onTimerEnd()
            timeLeft = totalTime
            running = false
            if (timerTickSound.isPlaying) {
                timerTickSound.pause()
            }
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.noRippleClickable { running = true },
    ) {
        Canvas(modifier = Modifier.size(50.dp)) {
            drawCircle(
                color = backgroundColor,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round),
            )
            drawArc(
                color = countdownColor,
                startAngle = -90f,
                sweepAngle = 360 * progress,
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round),
            )
        }
        Text(
            text = "${(timeLeft / 1000)}",
            style = Typography.titleLarge.copy(color = Color.White),
        )
    }
}
