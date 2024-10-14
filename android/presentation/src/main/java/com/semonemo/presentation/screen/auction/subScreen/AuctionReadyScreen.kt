package com.semonemo.presentation.screen.auction.subScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.semonemo.presentation.R
import com.semonemo.presentation.component.LongBlackButton
import com.semonemo.presentation.component.LongUnableButton
import com.semonemo.presentation.screen.auction.AuctionDetailViewModel
import com.semonemo.presentation.screen.auction.AuctionStatus
import com.semonemo.presentation.screen.auction.UserStatus
import com.semonemo.presentation.theme.Typography

@Preview(showBackground = true)
@Composable
fun AuctionReadyScreen(
    modifier: Modifier = Modifier,
    viewModel: AuctionDetailViewModel = hiltViewModel(),
) {
    val lottieComposition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.auction_ready))
    Surface(
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            LottieAnimation(
                composition = lottieComposition,
                restartOnPlay = true,
                iterations = LottieConstants.IterateForever,
                contentScale = ContentScale.Fit,
                modifier =
                    Modifier
                        .fillMaxHeight(0.5f)
                        .scale(1.5f),
            )
            Text(
                text = "경매 준비 중...",
                fontSize = 24.sp,
                style = Typography.bodyMedium,
            )
            Spacer(modifier = Modifier.weight(1f))
            if (viewModel.registerId == viewModel.userId) {
                if (viewModel.auctionStatus.value == AuctionStatus.READY) {
                    LongBlackButton(
                        modifier =
                            Modifier
                                .fillMaxWidth(),
                        text = stringResource(R.string.auction_start_button_title),
                        icon = null,
                        onClick = {
                            viewModel.startAuction()
                        },
                    )
                } else {
                    LongUnableButton(
                        modifier =
                            Modifier
                                .fillMaxWidth(),
                        text = stringResource(R.string.auction_start_button_title),
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (viewModel.auctionStatus.value == AuctionStatus.READY && viewModel.userStatus.value == UserStatus.NOT_READY) {
                LongBlackButton(
                    modifier =
                        Modifier
                            .fillMaxWidth(),
                    text = stringResource(R.string.user_ready_button_title),
                    icon = null,
                    onClick = {
                        viewModel.joinAuction()
                    },
                )
            } else if (viewModel.auctionStatus.value == AuctionStatus.PROGRESS && viewModel.userStatus.value == UserStatus.NOT_READY) {
                LongBlackButton(
                    modifier =
                        Modifier
                            .fillMaxWidth(),
                    text = stringResource(R.string.user_start_button_title),
                    icon = null,
                    onClick = {
                        viewModel.joinAuction()
                    },
                )
            } else {
                LongUnableButton(
                    modifier =
                        Modifier
                            .fillMaxWidth(),
                    text = stringResource(R.string.user_start_button_title),
                )
            }

            // 추가적인 UI 요소 (예: 로딩 애니메이션 등)
        }
    }
}
