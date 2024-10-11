package com.semonemo.presentation.screen.auction

import android.media.MediaPlayer
import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.semonemo.presentation.BuildConfig
import com.semonemo.presentation.R
import com.semonemo.presentation.component.AuctionBidMessage
import com.semonemo.presentation.component.AuctionTopAppBarTitle
import com.semonemo.presentation.component.CustomDialog
import com.semonemo.presentation.component.CustomTextButton
import com.semonemo.presentation.component.ImageLoadingProgress
import com.semonemo.presentation.component.TopAppBar
import com.semonemo.presentation.component.TopAppBarNavigationType
import com.semonemo.presentation.screen.auction.subScreen.AuctionCancelScreen
import com.semonemo.presentation.screen.auction.subScreen.AuctionEndScreen
import com.semonemo.presentation.screen.auction.subScreen.AuctionProgressScreen
import com.semonemo.presentation.screen.auction.subScreen.AuctionReadyScreen
import com.semonemo.presentation.theme.GunMetal
import com.semonemo.presentation.theme.Typography
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "AuctionDetailScreen"

// 경매 상태 관리
enum class AuctionStatus(
    val page: Int,
) {
    READY(0),
    PROGRESS(1),
    END(2),
    CANCEL(3),
}

// 유저 상태 관리
enum class UserStatus {
    NOT_READY, // 준비 전
    READY, // 준비
    IN_PROGRESS, // 플레이 중
    COMPLETED, // 종료
}

// 참가 모드
enum class ParticipationStatus {
    PARTICIPANTS, // 참가자
    OBSERVER, // 관찰자
}

@Preview
@Composable
@OptIn(ExperimentalAnimationApi::class)
fun AuctionDetailScreen(
    modifier: Modifier = Modifier,
    auctionId: Long = 0L,
    registerId: Long = 0L,
    popUpBackStack: () -> Unit = {},
    viewModel: AuctionDetailViewModel = hiltViewModel(),
    onShowSnackBar: (String) -> Unit = {},
) {
    val pagerState = rememberPagerState(pageCount = { AuctionStatus.entries.size })
    val ipfsUrl = BuildConfig.IPFS_READ_URL
    val imgUrl = ipfsUrl + "ipfs/" + viewModel.nftImageUrl.value
    val context = LocalContext.current

    val webSocketManager = viewModel.webSocketManager.value
    // stompSession을 remember로 관리하여 전역적으로 유지
    val stompSession = viewModel.stompSession

    var showDialog by remember { mutableStateOf(false) }
    val auctionCongratulationsAnimation by rememberLottieComposition(
        spec =
            LottieCompositionSpec.RawRes(
                R.raw.auction_congratulations,
            ),
    )

    @Composable
    fun showCustomDialog(
        onConfirmClicked: () -> Unit,
        titleKeywords: List<String> = emptyList(),
        contentKeywords: List<String> = emptyList(),
    ) {
        CustomDialog(
            title = stringResource(R.string.auction_bid_give_up_title),
            content = stringResource(R.string.auction_bid_give_up_content),
            onConfirmMessage = stringResource(R.string.auction_bid_give_up_confirm),
            onDismissMessage = stringResource(R.string.auction_bid_give_up_dismiss),
            titleKeywords = titleKeywords,
            contentKeywords = contentKeywords,
            titleBrushFlag = titleKeywords.map { false },
            contentBrushFlag = contentKeywords.map { false },
            onConfirm = {
                showDialog = false // 다이얼로그 닫기
                onConfirmClicked()
            },
            onDismiss = {
                showDialog = false
            },
        )
    }

    LaunchedEffect(viewModel.uiEvent) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is AuctionUiEvent.Error -> onShowSnackBar(event.errorMessage)
            }
        }
    }

    LaunchedEffect(auctionId) {
        // 세션이 초기화되지 않았을 때만 연결
        if (stompSession.value == null) {
            stompSession.value = webSocketManager?.connectToAuction()
        }
        val headersUriMain = "/topic/auction/$auctionId"
        val headersUriStart = "/topic/auction/$auctionId/start"
        val headersUriBid = "/topic/auction/$auctionId/bid"
        val headersUriEnd = "/topic/auction/$auctionId/end"
        val headersUriParticipants = "/topic/auction/$auctionId/participants"
        // 경매 업데이트를 구독
        stompSession.value?.let { session ->
            launch {
                // start 구독 : 경매 시작
                webSocketManager?.subscribeToAuction(
                    session,
                    headersUriStart,
                    onAuctionStart = {
                        viewModel.updateStartMessage(it)
                        viewModel.updateBidPriceUnit()
                        viewModel.validateUserBid()
                    },
                )
            }
            launch {
                // main 구독 : 입찰 수신
                webSocketManager?.subscribeToAuction(
                    session,
                    headersUriMain,
                    onBidUpdate = {
                        viewModel.updateBidLog(it.toAuctionBidLog())
                        viewModel.adjustClear()
                        viewModel.updateBidPriceUnit()
                        viewModel.observeBidMessage(it)
                        viewModel.validateUserBid()
                    },
                )
            }
            launch {
                // bid 구독 : 입찰 발신
                webSocketManager?.subscribeToAuction(
                    session,
                    headersUriBid,
                )
            }
            launch {
                // end 구독 : 경매 종료
                webSocketManager?.subscribeToAuction(
                    session,
                    headersUriEnd,
                    onAuctionEnd = {
                        viewModel.updateEndMessage(it)
                    },
                )
            }
            launch {
                // participants 구독 : 참여자 수
                webSocketManager?.subscribeToAuction(
                    session,
                    headersUriParticipants,
                    onParticipants = {
                        viewModel.updateParticipantsMessage(it)
                    },
                )
            }
        }
    }
    // 경매 상태에 따른 유저 플로우
    LaunchedEffect(viewModel.auctionStatus.value) {
        when (viewModel.auctionStatus.value) {
            AuctionStatus.PROGRESS -> { // 경매 시작 | 유저 준비
                if (viewModel.userStatus.value == UserStatus.READY) {
                    viewModel.userStatus.value = UserStatus.IN_PROGRESS
                    pagerState.scrollToPage(AuctionStatus.PROGRESS.page)
                }
            }

            AuctionStatus.END -> {
                pagerState.scrollToPage(AuctionStatus.END.page)
            }

            AuctionStatus.CANCEL -> {
                pagerState.scrollToPage(AuctionStatus.CANCEL.page)
            }

            else -> {}
        }
    }
    // 유저 상태에 따른 유저 플로우
    LaunchedEffect(viewModel.userStatus.value) {
        when (viewModel.userStatus.value) {
            UserStatus.READY -> {
                if (viewModel.auctionStatus.value == AuctionStatus.PROGRESS) {
                    viewModel.userStatus.value = UserStatus.IN_PROGRESS
                    pagerState.scrollToPage(AuctionStatus.PROGRESS.page)
                }
            }

            UserStatus.IN_PROGRESS -> {
                if (viewModel.auctionStatus.value == AuctionStatus.PROGRESS) {
                    Log.d(TAG, "AuctionDetailScreen: 중간 이동")
                    pagerState.scrollToPage(AuctionStatus.PROGRESS.page)
                }
            }

            else -> {
                Log.d(TAG, "AuctionDetailScreen: 여기로 오다뇨")
            }
        }
    }

    // 사운드 재생을 위한 LaunchedEffect 추가
    LaunchedEffect(viewModel.bid.value) {
        if (viewModel.userStatus.value == UserStatus.IN_PROGRESS) {
            // MediaPlayer 초기화
            val mediaPlayer = MediaPlayer.create(context, R.raw.auction_bid_sound)
            mediaPlayer.start()
            // 사운드 재생 완료 후 리소스 해제
            mediaPlayer.setOnCompletionListener {
                mediaPlayer.release()
            }
        }
    }

    // 경매 상태에 따른 사운드
    LaunchedEffect(viewModel.auctionStatus.value) {
        if (viewModel.auctionStatus.value == AuctionStatus.PROGRESS) { // 경매 시작
            // MediaPlayer 초기화
            val mediaPlayer = MediaPlayer.create(context, R.raw.auction_start_sound)
            mediaPlayer.start()
            // 사운드 재생 완료 후 리소스 해제
            mediaPlayer.setOnCompletionListener {
                mediaPlayer.release()
            }
        } else if (viewModel.auctionStatus.value == AuctionStatus.END || viewModel.auctionStatus.value == AuctionStatus.CANCEL) {
            val mediaPlayer = MediaPlayer.create(context, R.raw.auction_end_sound)
            mediaPlayer.start()
            // 사운드 재생 완료 후 리소스 해제
            mediaPlayer.setOnCompletionListener {
                mediaPlayer.release()
            }
        }
    }

    Column(
        modifier =
            modifier
                .padding(16.dp)
                .statusBarsPadding(),
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        TopAppBar(
            modifier = Modifier,
            title = { AuctionTopAppBarTitle(modifier = Modifier, viewModel = viewModel) },
            navigationType = TopAppBarNavigationType.None,
            actionButtons = {
                CustomTextButton(
                    text = stringResource(id = R.string.auction_bid_give_up_button),
                    isPossible = true,
                    onClick = { showDialog = true },
                )
            },
        )
        Spacer(modifier = modifier.height(30.dp))
        GlideImage(
            imageModel = imgUrl,
            contentScale = ContentScale.Fit,
            modifier =
                modifier
                    .height(270.dp)
                    .width(180.dp)
                    .align(Alignment.CenterHorizontally),
            loading = {
                ImageLoadingProgress(
                    modifier = Modifier,
                )
            },
        )
        Spacer(modifier = modifier.height(20.dp))

        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_auction_human),
                    contentDescription = "participant",
                    tint = Color.Unspecified,
                )
                Spacer(modifier = Modifier.width(4.dp))

                // AnimatedContent를 사용하여 숫자 전환 애니메이션 적용
                AnimatedContent(
                    targetState = viewModel.participant.value,
                    transitionSpec = {
                        // 숫자가 증가하는 경우 아래에서 위로 이동
                        if (targetState > initialState) {
                            slideInVertically(
                                animationSpec = tween(300),
                                initialOffsetY = { height -> height },
                            ) + fadeIn(animationSpec = tween(300)) with
                                slideOutVertically(
                                    animationSpec = tween(300),
                                    targetOffsetY = { height -> -height },
                                ) + fadeOut(animationSpec = tween(300))
                        } else {
                            // 숫자가 감소하는 경우 위에서 아래로 이동
                            slideInVertically(
                                animationSpec = tween(300),
                                initialOffsetY = { height -> -height },
                            ) + fadeIn(animationSpec = tween(300)) with
                                slideOutVertically(
                                    animationSpec = tween(300),
                                    targetOffsetY = { height -> height },
                                ) + fadeOut(animationSpec = tween(300))
                        }.using(
                            SizeTransform(clip = false),
                        )
                    },
                ) { animatedParticipantCount ->
                    Text(
                        text = "${String.format("%,d", animatedParticipantCount)}",
                        fontSize = 20.sp,
                        style =
                            Typography.titleMedium.copy(
                                fontFeatureSettings = "tnum",
                                color = GunMetal,
                            ),
                    )
                }
                Text(
                    text = " 명",
                    fontSize = 20.sp,
                    style = Typography.labelMedium.copy(color = GunMetal),
                )
            }
            Row(modifier = Modifier.wrapContentWidth()) {
                Text(
                    text = "보유 PAY : ",
                    fontSize = 20.sp,
                    style =
                        Typography.labelMedium.copy(
                            color = GunMetal,
                        ),
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = String.format("%,d", viewModel.userCoinBalance.longValue),
                    fontSize = 20.sp,
                    style =
                        Typography.titleMedium.copy(
                            fontFeatureSettings = "tnum",
                            color = GunMetal,
                        ),
                )
            }
        }

        HorizontalPager(
            modifier = modifier.fillMaxWidth(),
            state = pagerState,
            userScrollEnabled = false, // TODO : 테스트 후 false
        ) { page ->
            when (AuctionStatus.entries[page]) {
                AuctionStatus.READY ->
                    AuctionReadyScreen(
                        modifier = modifier,
                        viewModel = viewModel,
                    )

                AuctionStatus.PROGRESS ->
                    AuctionProgressScreen(
                        modifier = modifier,
                        viewModel = viewModel,
                    )

                AuctionStatus.END ->
                    AuctionEndScreen(
                        modifier = modifier,
                        viewModel = viewModel,
                        popUpBackStack = popUpBackStack,
                    )

                AuctionStatus.CANCEL ->
                    AuctionCancelScreen(
                        modifier = modifier,
                        viewModel = viewModel,
                        popUpBackStack = popUpBackStack,
                    )
            }
        }
    }
    if (viewModel.auctionStatus.value == AuctionStatus.END && viewModel.result.value?.winner == viewModel.userId) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter,
        ) {
            LottieAnimation(
                composition = auctionCongratulationsAnimation,
                restartOnPlay = true,
                iterations = LottieConstants.IterateForever,
                contentScale = ContentScale.Fit,
                modifier =
                    Modifier
                        .fillMaxSize(1.0f)
                        .scale(1.2f)
                        .padding(bottom = 250.dp),
            )
        }
    }
    if(viewModel.userStatus.value == UserStatus.IN_PROGRESS) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter,
        ) {
            AnimatedVisibility(
                visible = viewModel.observedBidMessage.value,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                AuctionBidMessage(
                    modifier = Modifier.padding(top = 250.dp), // 화면 위에서 조금 아래로 내림
                    icon = R.drawable.ic_color_sene_coin,
                    bidPrice = viewModel.topPrice.longValue,
                    anonym = viewModel.anonym.intValue,
                    user = viewModel.observedBidSubmit.value,
                )
            }
        }
    }


    // Composable이 사라질 때 leaveAuction() 호출
    DisposableEffect(Unit) {
        onDispose {
            if(viewModel.userStatus.value == UserStatus.READY || viewModel.userStatus.value == UserStatus.IN_PROGRESS) {
                viewModel.leaveAuction()
            }

        }
    }

    if (showDialog) {
        showCustomDialog(
            onConfirmClicked = {
                popUpBackStack()
            },
        )
    }
}
