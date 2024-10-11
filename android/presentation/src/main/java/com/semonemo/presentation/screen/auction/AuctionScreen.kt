package com.semonemo.presentation.screen.auction

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.semonemo.presentation.R
import com.semonemo.presentation.component.CustomAuctionFAB
import com.semonemo.presentation.component.SectionHeader
import com.semonemo.presentation.screen.auction.subScreen.AuctionReadScreen
import com.semonemo.presentation.theme.Gray02
import com.semonemo.presentation.theme.GunMetal
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.util.noRippleClickable
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AuctionScreen(
    modifier: Modifier = Modifier,
    viewModel: AuctionViewModel = hiltViewModel(),
    onShowError: (String) -> Unit = {},
    navigateToAuctionDetail: (Long) -> Unit = {},
    navigateToAuctionRegister: () -> Unit = {},
) {
    val auctionUiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val verticalScrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.loadShortAuction()
    }

    LaunchedEffect(viewModel.uiEvent) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is AuctionUiEvent.Error -> onShowError(event.errorMessage)
            }
        }
    }

    Surface(
        modifier =
            modifier
                .fillMaxSize(),
        color = Color.White,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .verticalScroll(state = verticalScrollState),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                SectionHeader(
                    modifier = Modifier.padding(10.dp),
                    text = stringResource(R.string.auction_progress_header),
                )
                Box(
                    modifier =
                        Modifier
                            .padding(5.dp)
                            .noRippleClickable(
                                onClick = { viewModel.loadShortAuction() },
                            ),
                ) {
                    Row(
                        modifier = Modifier.padding(end = 5.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            modifier = Modifier.size(16.dp),
                            painter = painterResource(R.drawable.ic_reload),
                            contentDescription = "reload",
                            tint = Color.Unspecified,
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            modifier = Modifier,
                            text = stringResource(R.string.reload_label),
                            color = GunMetal,
                            style = Typography.labelMedium,
                        )
                    }
                }
            }

            if (auctionUiState.progressAuctionList.isEmpty()) {
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "현재 진행중 경매가 없어요! 🥲",
                        style = Typography.labelLarge,
                        color = Gray02,
                    )
                }
            } else {
                AuctionReadScreen(
                    auctionDataList = auctionUiState.progressAuctionList,
                    navigateToAuctionDetail = navigateToAuctionDetail,
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            // 예정된 경매
            SectionHeader(
                modifier = Modifier.padding(10.dp),
                text = stringResource(R.string.auction_ready_header),
            )
            if (auctionUiState.readyAuctionList.isEmpty()) {
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "현재 예정된 경매가 없어요! 🥲",
                        style = Typography.labelLarge,
                        color = Gray02,
                    )
                }
            } else {
                AuctionReadScreen(
                    auctionDataList = auctionUiState.readyAuctionList,
                    navigateToAuctionDetail = navigateToAuctionDetail,
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            // 낙찰된 경매
            SectionHeader(
                modifier = Modifier.padding(10.dp),
                text = stringResource(R.string.auction_end_header),
            )
            if (auctionUiState.endAuctionList.isEmpty()) {
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "현재 낙찰된 경매가 없어요! 🥲",
                        style = Typography.labelLarge,
                        color = Gray02,
                    )
                }
            } else {
                AuctionReadScreen(
                    auctionDataList = auctionUiState.endAuctionList,
                    navigateToAuctionDetail = navigateToAuctionDetail,
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            // 유찰된 경매
            SectionHeader(
                modifier = Modifier.padding(10.dp),
                text = stringResource(R.string.auction_cancel_header),
            )
            if (auctionUiState.cancelAuctionList.isEmpty()) {
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "현재 유찰된 경매가 없어요! 🥲",
                        style = Typography.labelLarge,
                        color = Gray02,
                    )
                }
            } else {
                AuctionReadScreen(
                    auctionDataList = auctionUiState.cancelAuctionList,
                    navigateToAuctionDetail = navigateToAuctionDetail,
                )
            }
            Spacer(modifier = Modifier.height(100.dp))
            // 여기 아래에 진행 예정인 것 넣으면 됨.
        }
        CustomAuctionFAB(
            modifier = modifier,
            navigateToAuctionRegister = navigateToAuctionRegister,
        )
    }
}
