package com.semonemo.presentation.screen.auction.subScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.semonemo.domain.model.AuctionBidLog
import com.semonemo.presentation.R
import com.semonemo.presentation.theme.Gray02
import com.semonemo.presentation.theme.GunMetal
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.util.spToDp

@Composable
fun BidInfo(
    modifier: Modifier = Modifier,
    bidLogList: List<AuctionBidLog>,
    startPrice: Long, // 추가된 파라미터
    userId: Long = 0,
) {
    Column(
        modifier = modifier,
    ) {
        val fontSize = 14

        // 입찰 기록이 있을 때 입찰자 목록 표시
        // 상위 입찰자 표시
        // bidAmount 기준으로 내림차순 정렬
        val sortedBidLog = bidLogList.sortedByDescending { it.bidAmount }

        if (bidLogList.isNotEmpty()) {
            val topBidder = sortedBidLog.first()
            sortedBidLog.drop(1)
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier =
                    Modifier
                        .fillMaxWidth(),
            ) {
                Text(
                    text = String.format("%03d번", topBidder.anonym) + if (userId == topBidder.userId) String.format(" (MY)") else "",
                    fontSize = (fontSize + 4).sp, // 적절한 텍스트 크기 조정
                    style =
                        Typography.bodyMedium.copy(
                            fontFeatureSettings = "tnum",
                            color = GunMetal,
                        ),
                )
                Text(
                    text =
                        String.format(
                            "%,d ${stringResource(id = R.string.coin_price_unit)}",
                            topBidder.bidAmount,
                        ),
                    fontSize = (fontSize + 4).sp, // 적절한 텍스트 크기 조정
                    style =
                        Typography.bodyMedium.copy(
                            fontFeatureSettings = "tnum",
                            color = GunMetal,
                        ),
                )
            }
        } else {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier =
                    Modifier
                        .fillMaxWidth(),
            ) {
                Text(
                    text = "기준가",
                    fontSize = (fontSize + 4).sp, // 적절한 텍스트 크기 조정
                    style =
                        Typography.bodyMedium.copy(
                            fontFeatureSettings = "tnum",
                            color = GunMetal,
                        ),
                )
                Text(
                    text =
                        String.format(
                            "%,d ${stringResource(id = R.string.coin_price_unit)}",
                            startPrice,
                        ),
                    fontSize = (fontSize + 4).sp, // 적절한 텍스트 크기 조정
                    style =
                        Typography.bodyMedium.copy(
                            fontFeatureSettings = "tnum",
                            color = GunMetal,
                        ),
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 나머지 입찰자 리스트 표시
        val itemHeight = fontSize.sp.spToDp() + 8.dp // 텍스트 크기 + 패딩으로 높이 계산

        LazyColumn(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(itemHeight * 4),
            // 원하는 높이 설정 (필요에 따라 조정 가능)
        ) {
            items(if (bidLogList.isNotEmpty())sortedBidLog.drop(1) else sortedBidLog) { bidder ->
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(itemHeight),
                ) {
                    Text(
                        text = String.format("%03d번", bidder.anonym) + if (userId == bidder.userId) String.format(" (MY)") else "",
                        fontSize = fontSize.sp, // 기본 텍스트 크기
                        style =
                            Typography.labelMedium.copy(
                                fontFeatureSettings = "tnum",
                                color = Gray02,
                            ),
                    )
                    Text(
                        text =
                            String.format(
                                "%,d ${stringResource(id = R.string.coin_price_unit)}",
                                bidder.bidAmount,
                            ),
                        fontSize = fontSize.sp, // 기본 텍스트 크기
                        style =
                            Typography.labelMedium.copy(
                                fontFeatureSettings = "tnum",
                                color = Gray02,
                            ),
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun BidInfoPreview_NoBids() {
    BidInfo(
        bidLogList = emptyList(),
        startPrice = 10000, // 예시 기준가
    )
}

@Composable
@Preview(showBackground = true)
fun BidInfoPreview_WithBids() {
    val sampleBidLogs =
        listOf(
            AuctionBidLog(userId = 1L, anonym = 101, bidAmount = 15000),
            AuctionBidLog(userId = 2L, anonym = 102, bidAmount = 12000),
            AuctionBidLog(userId = 3L, anonym = 103, bidAmount = 11000),
            AuctionBidLog(userId = 4L, anonym = 103, bidAmount = 10000),
            AuctionBidLog(userId = 1L, anonym = 101, bidAmount = 8000),
            AuctionBidLog(userId = 6L, anonym = 753, bidAmount = 7000),
        )
    BidInfo(
        bidLogList = sampleBidLogs,
        startPrice = 10000, // 예시 기준가
        userId = 1L,
    )
}
