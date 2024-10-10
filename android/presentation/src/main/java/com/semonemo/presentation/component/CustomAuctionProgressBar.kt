package com.semonemo.presentation.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.semonemo.presentation.screen.auction.AuctionStatus
import com.semonemo.presentation.theme.GunMetal
import com.semonemo.presentation.theme.ProgressGreen
import com.semonemo.presentation.theme.ProgressRed
import com.semonemo.presentation.theme.ProgressYellow
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.theme.WhiteGray
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.math.max

@Composable
fun CustomAuctionProgressBar(
    modifier: Modifier = Modifier,
    auctionStatus: AuctionStatus = AuctionStatus.READY,
    initialTime: Float = 15f, // 초기 시간 설정 (초 단위)
    endTime: LocalDateTime = LocalDateTime.now().plusSeconds(15),
) {
    // endTime을 epoch millis로 변환 (타임존을 지정)
    val endTimeMillis =
        remember(endTime) {
            endTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        }

    var timeLeft by remember { mutableStateOf(initialTime) }

    val progress = timeLeft / initialTime // 진행률

    val progressColor =
        when {
            progress > 0.5f -> lerp(ProgressGreen, ProgressYellow, (1 - progress) * 2)
            else -> lerp(ProgressYellow, ProgressRed, (0.5f - progress) * 2)
        }

    LaunchedEffect(endTimeMillis) {
        if (auctionStatus == AuctionStatus.PROGRESS) {
            while (true) {
                val currentTimeMillis = parseLocalDateTimeToLong(LocalDateTime.now())
                val updatedTime = max(0f, (endTimeMillis - currentTimeMillis) / 1000f)
                timeLeft = if(updatedTime < 0L) 0f else updatedTime
                delay(100L) // 0.1초마다 업데이트
                if(auctionStatus == AuctionStatus.END) {
                    break
                }
            }
        }
    }

    Box(
        contentAlignment = Alignment.CenterEnd,
        modifier =
            modifier
                .fillMaxWidth()
                .height(30.dp),
    ) {
        // 커스텀 프로그레스바
        Canvas(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(30.dp),
        ) {
            // 배경
            drawRoundRect(
                color = WhiteGray,
                size = size,
                cornerRadius = CornerRadius(10.dp.toPx()),
            )
            // 진행 바
            drawRoundRect(
                color = progressColor, // 남은 진행 바 색상
                size = size.copy(width = size.width * progress),
                cornerRadius = CornerRadius(10.dp.toPx()),
            )
        }

        // 남은 시간 텍스트
        Text(
            modifier = Modifier.padding(end = 8.dp),
            text = String.format("%.1fs", timeLeft),
            style =
                Typography.bodyMedium.copy(
                    fontFeatureSettings = "tnum",
                ),
            color = GunMetal,
        )
    }
}

@Composable
@Preview(showBackground = true)
fun CustomAuctionProgressBarPreview() {
    // 예시 종료 시간: 현재 시간으로부터 15초 후
    val exampleEndTime = LocalDateTime.now().plusSeconds(15)
    CustomAuctionProgressBar(
        modifier = Modifier.padding(16.dp),
        initialTime = 15f,
        endTime = exampleEndTime,
    )
}

/** LocalDateTime을 Long으로 파싱 **/
fun parseLocalDateTimeToLong(localDateTime: LocalDateTime): Long {
    return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    // systemDefault는 현재 디바이스의 지역을 의미함
}