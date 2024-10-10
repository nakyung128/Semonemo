package com.semonemo.presentation.animation

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.semonemo.presentation.theme.Typography

@Composable
fun LiveAnimation() {
    val infiniteTransition = rememberInfiniteTransition(label = "")

    // 중앙 원의 크기 애니메이션
    val centralCircleScale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 0.8f, // 시작값과 종료값을 같게 설정
        animationSpec =
            infiniteRepeatable(
                animation =
                    keyframes {
                        durationMillis = 2000 // 전체 주기를 2초로 설정
                        0.8f at 0 // 시작
                        1f at 500 // 1초에 최대 크기
                        1.0f at 1000 // 1.5초까지 최대 크기 유지
                        0.8f at 1500 // 2초에 다시 시작 크기로
                    },
                repeatMode = RepeatMode.Restart, // Reverse 대신 Restart 사용
            ),
        label = "central_circle",
    )

    // 파장 애니메이션
    val waveAnimations =
        List(1) { index ->
            infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec =
                    infiniteRepeatable(
                        animation = tween(2000, easing = LinearEasing, delayMillis = index * 0),
                        repeatMode = RepeatMode.Restart,
                    ),
                label = "wave_$index",
            )
        }

    Row(
        modifier = Modifier.wrapContentSize(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier.size(24.dp),
            contentAlignment = Alignment.Center,
        ) {
            // 파장 그리기
            waveAnimations.forEach { anim ->
                val scale by anim
                Canvas(modifier = Modifier.size(24.dp)) {
                    drawCircle(
                        color = Color.Red.copy(alpha = 0.7f * (1 - scale)),
                        radius = 12.dp.toPx() * (0.3f + 0.7f * scale),
                    )
                }
            }
            // 중앙 원 그리기
            Canvas(modifier = Modifier.size(24.dp)) {
                drawCircle(
                    color = Color.Red,
                    radius = 6.dp.toPx() * centralCircleScale,
                )
            }
        }
        Spacer(modifier = Modifier.width(2.dp))
        Text(
            text = "LIVE",
            color = Color.Red,
            fontWeight = FontWeight.Bold,
            style = Typography.titleSmall.copy(letterSpacing = 0.02.em),
        )
    }
}
