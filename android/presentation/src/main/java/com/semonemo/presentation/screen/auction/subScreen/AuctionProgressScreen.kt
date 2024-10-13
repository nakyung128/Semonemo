package com.semonemo.presentation.screen.auction.subScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.semonemo.presentation.component.AuctionBidPriceInput
import com.semonemo.presentation.component.BidPriceUnitButton
import com.semonemo.presentation.component.CustomAuctionProgressBar
import com.semonemo.presentation.component.CustomNoRippleButton
import com.semonemo.presentation.screen.auction.AuctionDetailViewModel
import com.semonemo.presentation.screen.auction.BidMessage
import com.walletconnect.android.internal.common.scope
import kotlinx.coroutines.launch

@Composable
fun AuctionProgressScreen(
    modifier: Modifier = Modifier,
    viewModel: AuctionDetailViewModel = hiltViewModel(),
) {
    val adjustEndTime =
        viewModel.auctionBidLog.value
            .lastOrNull()
            ?.endTime
    Surface(
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(),
        ) {
            Column(
                modifier = Modifier.weight(1f),
            ) {
                CustomAuctionProgressBar(auctionStatus = viewModel.auctionStatus.value, endTime = adjustEndTime ?: viewModel.endTime.value)
                Spacer(modifier = Modifier.height(10.dp))
                BidInfo(
                    modifier = Modifier,
                    bidLogList = viewModel.auctionBidLog.value,
                    startPrice = viewModel.topPrice.longValue,
                    userId = viewModel.userId,
                    // 위에서 선언한 auctionBidLog
                )

                Spacer(modifier = Modifier.height(10.dp))
            }

            AuctionBidPriceInput(
                modifier = Modifier,
                currentPrice = viewModel.topPrice.longValue,
                adjustedAmount = viewModel.myBidPrice.longValue,
                adjustedPercentage = viewModel.myPercentage.intValue,
                onClear = { viewModel.adjustClear() },
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                BidPriceUnitButton(
                    modifier = Modifier.weight(1f),
                    bidPricePercentage = 100,
                    bidPriceUnit = viewModel.bidPriceUnit.longValue * 10,
                    onClick = {
                        viewModel.adjustBidPrice(viewModel.bidPriceUnit.longValue * 10, 100)
                        viewModel.adjustUserBidButton()
                    },
                )
                BidPriceUnitButton(
                    modifier = Modifier.weight(1f),
                    bidPricePercentage = 50,
                    bidPriceUnit = viewModel.bidPriceUnit.longValue * 5,
                    onClick = {
                        viewModel.adjustBidPrice(viewModel.bidPriceUnit.longValue * 5, 50)
                        viewModel.adjustUserBidButton()
                    },
                )
                BidPriceUnitButton(
                    modifier = Modifier.weight(1f),
                    bidPricePercentage = 10,
                    bidPriceUnit = viewModel.bidPriceUnit.longValue,
                    onClick = {
                        viewModel.adjustBidPrice(viewModel.bidPriceUnit.longValue, 10)
                        viewModel.adjustUserBidButton()
                    },
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            CustomNoRippleButton(
                text = "입찰하기",
                enabled = viewModel.validateUserBid.value,
                onClick = {
                    scope.launch {
                        viewModel.stompSession.value?.let { session ->
                            viewModel.webSocketManager.value?.sendBid(
                                session,
                                viewModel.auctionId,
                                BidMessage(
                                    viewModel.userId,
                                    viewModel.anonym.intValue,
                                    viewModel.topPrice.longValue + viewModel.myBidPrice.longValue,
                                ),
                            )
                        }
                    }
                },
            )
        }
    }
}
