package com.semonemo.presentation.screen.auction.subScreen

import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.semonemo.presentation.component.LiveAuctionCard
import com.semonemo.presentation.screen.auction.AuctionViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Preview(showBackground = true, name = "AuctionReadScreen")
@Composable
fun ShortAuctionReadScreen(
    modifier: Modifier = Modifier,
    viewModel: AuctionViewModel = hiltViewModel(),
    navigateToAuctionDetail: (Long) -> Unit = {},
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .wrapContentHeight(),
    ) {
        // LazyRow의 스크롤 상태를 저장
        val listState = rememberLazyListState()
        // CoroutineScope를 기억
        val coroutineScope = rememberCoroutineScope()
        // 사용자가 마지막으로 스크롤한 시간을 추적
        var lastInteractionTime by remember { mutableStateOf(System.currentTimeMillis()) }
        // 경매 데이터
        val auctionDataList = viewModel.shortAuctionList.value

        // 사용자가 5초 동안 상호작용하지 않았을 때 자동으로 스크롤 시작
        LaunchedEffect(lastInteractionTime) {
            while (true) {
                if (System.currentTimeMillis() - lastInteractionTime > 5000) {
                    coroutineScope.launch {
                        val itemWidth =
                            listState.layoutInfo.visibleItemsInfo
                                .firstOrNull()
                                ?.size
                                ?: 160 // 기본 너비 (픽셀)
                        val viewportWidth = listState.layoutInfo.viewportSize.width
                        val totalScrollDistance =
                            itemWidth * auctionDataList.size - viewportWidth
                        val scrollDurationMillis = 100L // 전체 스크롤 시간 (밀리초)

                        // 애니메이션의 속도를 조절합니다.
                        val scrollSteps = (scrollDurationMillis / 10).toInt()
                        val stepDistance = totalScrollDistance / scrollSteps

                        var scrolledDistance = 0
                        while (scrolledDistance < totalScrollDistance) {
                            listState.animateScrollBy(stepDistance.toFloat())
                            scrolledDistance += stepDistance
                            delay(scrollDurationMillis / scrollSteps) // 스크롤 간의 간격 조정
                        }

                        // 스크롤이 끝나면 첫 번째 항목으로 돌아가기
                        listState.scrollToItem(0)
                    }
                }
                delay(2000L) // 스크롤 간의 간격 조정
            }
        }

        // 수평 스크롤 가능한 경매 리스트
        LazyRow(
            state = listState,
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier =
                Modifier
                    .pointerInput(Unit) {
                        // 사용자가 스크롤할 때마다 상호작용 시간 업데이트
                        detectTapGestures {
                            lastInteractionTime = System.currentTimeMillis()
                        }
                    },
        ) {
            items(auctionDataList) { data ->
                LiveAuctionCard(
                    modifier =
                        Modifier
                            .width(160.dp) // 카드의 너비 설정
                            .height(300.dp),
                    id = data.id,
                    status = data.status,
                    nftId = data.nftId,
                    nftImageUrl = data.nftImageUrl,
                    participants = data.participants,
                    startPrice = data.startPrice,
                    currentBid = data.currentBid,
                    finalPrice = data.finalPrice,
                    startTime = data.startTime,
                    endTime = data.endTime,
                    onClick = navigateToAuctionDetail,
                )
            }
        }
    }
}
