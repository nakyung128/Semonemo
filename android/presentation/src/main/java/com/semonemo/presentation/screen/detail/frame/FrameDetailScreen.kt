package com.semonemo.presentation.screen.detail.frame

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.semonemo.domain.model.FrameDetail
import com.semonemo.presentation.BuildConfig
import com.semonemo.presentation.R
import com.semonemo.presentation.component.HashTag
import com.semonemo.presentation.component.ImageLoadingProgress
import com.semonemo.presentation.component.LoadingDialog
import com.semonemo.presentation.component.LongBlackButton
import com.semonemo.presentation.component.NameWithBadge
import com.semonemo.presentation.component.TopAppBar
import com.semonemo.presentation.screen.nft.NftViewModel
import com.semonemo.presentation.theme.Gray02
import com.semonemo.presentation.theme.Gray03
import com.semonemo.presentation.theme.GunMetal
import com.semonemo.presentation.theme.Red
import com.semonemo.presentation.theme.SemonemoTheme
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.theme.WhiteGray
import com.semonemo.presentation.util.noRippleClickable
import com.semonemo.presentation.util.urlToIpfs
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.generated.Uint256
import java.util.Locale

@Composable
fun FrameDetailRoute(
    modifier: Modifier = Modifier,
    popUpBackStack: () -> Unit = {},
    onShowSnackBar: (String) -> Unit = {},
    viewModel: FrameDetailViewModel = hiltViewModel(),
    nftViewModel: NftViewModel = hiltViewModel(),
    navigateToDetail: (Long) -> Unit = {},
    navigateToProfile: (Long) -> Unit = {},
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    FrameDetailContent(
        modifier = modifier,
        popUpBackStack = popUpBackStack,
        onShowSnackBar = onShowSnackBar,
        uiState = uiState.value,
        uiEvent = viewModel.uiEvent,
        onClickedLikeNft = viewModel::onClickedLikeNft,
        onClickedPurchase = viewModel::getBalance,
        sendTransaction = {
            nftViewModel.sendTransaction(
                function =
                    Function(
                        "buyMarket",
                        listOf(Uint256(uiState.value.frame.nftInfo.tokenId)),
                        listOf<TypeReference<*>>(object : TypeReference<Uint256>() {}),
                    ),
                onSuccess = { hash ->
                    viewModel.purchaseNft(txHash = hash, marketId = uiState.value.frame.marketId)
                },
                onError = {
                    onShowSnackBar(it)
                },
                contractAddress = BuildConfig.SYSTEM_CONTRACT_ADDRESS,
            )
        },
        frame = uiState.value.frame,
        navigateToDetail = navigateToDetail,
        navigateToProfile = {
            navigateToProfile(uiState.value.frame.seller.userId)
        },
        cancelSale = {
            nftViewModel.sendTransaction(
                function =
                    Function(
                        "cancelMarket",
                        listOf(
                            Uint256(
                                uiState.value.frame.nftInfo.tokenId,
                            ),
                        ),
                        listOf<TypeReference<*>>(object : TypeReference<Uint256>() {}),
                    ),
                onSuccess = { txHash ->
                    viewModel.cancelSaleNft(
                        txHash = txHash,
                        marketId = uiState.value.frame.marketId,
                    )
                },
                onError = {
                    onShowSnackBar(it)
                },
                contractAddress = BuildConfig.SYSTEM_CONTRACT_ADDRESS,
            )
        },
    )
}

@Composable
fun FrameDetailContent(
    modifier: Modifier = Modifier,
    popUpBackStack: () -> Unit = {},
    onShowSnackBar: (String) -> Unit = {},
    uiEvent: SharedFlow<FrameDetailUiEvent>,
    uiState: FrameDetailUiState,
    onClickedLikeNft: (Boolean) -> Unit = {},
    onClickedPurchase: (Long) -> Unit = {},
    sendTransaction: () -> Unit = {},
    frame: FrameDetail = FrameDetail(),
    navigateToDetail: (Long) -> Unit = {},
    navigateToProfile: () -> Unit = {},
    cancelSale: () -> Unit = {},
) {
    LaunchedEffect(uiEvent) {
        uiEvent.collectLatest { event ->
            when (event) {
                is FrameDetailUiEvent.Error -> onShowSnackBar(event.errorMessage)
                is FrameDetailUiEvent.LoadCoin -> sendTransaction()
                FrameDetailUiEvent.Success -> popUpBackStack()
                is FrameDetailUiEvent.CancelSale -> {
                    onShowSnackBar(event.errorMessage)
                    popUpBackStack()
                }
            }
        }
    }
    FrameDetailScreen(
        modifier = modifier,
        popUpBackStack = popUpBackStack,
        frameTitle = frame.nftInfo.data.title,
        frameUrl =
            frame.nftInfo.data.image
                .urlToIpfs(),
        hashTag = frame.tags,
        hasBadge = true,
        nickname = frame.seller.nickname,
        frameContent = frame.nftInfo.data.content,
        isLiked = uiState.isLiked,
        heartCount = uiState.likedCount,
        price = frame.price.toDouble(),
        profileImageUrl = frame.seller.profileImage,
        onClickedLikeNft = onClickedLikeNft,
        onClickedPurchase = onClickedPurchase,
        creatorFrames = uiState.creatorFrames,
        navigateToDetail = navigateToDetail,
        canPurchase = (uiState.userId == frame.seller.userId).not(),
        navigateToProfile = navigateToProfile,
        cancelSale = cancelSale,
    )
    if (uiState.isLoading) {
        LoadingDialog(
            lottieRes = R.raw.normal_load,
            subMessage = stringResource(R.string.loading_sub_message),
        )
    }
}

@Composable
fun FrameDetailScreen(
    modifier: Modifier = Modifier,
    popUpBackStack: () -> Unit = {},
    frameTitle: String = "아이유와 한컷",
    frameUrl: String = "",
    hashTag: List<String> = listOf(),
    profileImageUrl: String = "",
    nickname: String = "짜이한",
    hasBadge: Boolean = true,
    frameContent: String = "아이유와 한컷! 아이유와 한컷! 아이유와 한컷! 아이유와 한컷! 아이유와 한컷!  123123123123123123123123123121231231",
    isLiked: Boolean = true,
    heartCount: Long = 100000,
    price: Double = 100.1,
    onClickedLikeNft: (Boolean) -> Unit = {},
    onClickedPurchase: (Long) -> Unit = {},
    creatorFrames: List<FrameDetail> = listOf(),
    navigateToDetail: (Long) -> Unit = {},
    canPurchase: Boolean = false,
    navigateToProfile: () -> Unit = {},
    cancelSale: () -> Unit = {},
) {
    val (expanded, isExpanded) =
        remember {
            mutableStateOf(false)
        }
    val maxLines = 2
    Surface(
        modifier =
            Modifier
                .fillMaxSize(),
        color = Color.White,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(color = Color.White)
                    .statusBarsPadding()
                    .navigationBarsPadding(),
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            TopAppBar(
                modifier =
                Modifier,
                title = {
                    Text(text = frameTitle, style = Typography.bodyLarge.copy(fontSize = 17.sp))
                },
                onNavigationClick = popUpBackStack,
            )
            Column(
                modifier =
                    modifier
                        .wrapContentSize()
                        .padding(horizontal = 15.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                GlideImage(
                    imageModel = frameUrl.toUri(),
                    contentScale = ContentScale.Fit,
                    modifier =
                        Modifier
                            .fillMaxWidth(0.7f)
                            .aspectRatio(3f / 4f)
                            .padding(horizontal = 20.dp),
                    loading = {
                        ImageLoadingProgress(
                            modifier = Modifier,
                        )
                    },
                )
                LazyRow(
                    modifier =
                        Modifier
                            .wrapContentHeight()
                            .align(Alignment.Start),
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    content = {
                        items(hashTag.size) { index ->
                            HashTag(
                                keyword = hashTag[index],
                            )
                        }
                    },
                )
                Spacer(modifier = Modifier.fillMaxHeight(0.05f))
                Row(
                    modifier =
                        Modifier.align(Alignment.Start).noRippleClickable {
                            navigateToProfile()
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                ) {
                    GlideImage(
                        imageModel = profileImageUrl.toUri(),
                        contentScale = ContentScale.Crop,
                        modifier =
                            Modifier
                                .size(30.dp)
                                .clip(shape = CircleShape),
                        loading = {
                            ImageLoadingProgress(
                                modifier = Modifier,
                            )
                        },
                        failure = {
                            Image(
                                painter =
                                    painterResource(
                                        id =
                                            R.drawable.ic_place_holder,
                                    ),
                                contentDescription = null,
                            )
                        },
                    )
                    if (hasBadge) {
                        NameWithBadge(
                            name = nickname,
                            size = 18,
                        )
                    } else {
                        Text(
                            text = nickname,
                            style = Typography.bodySmall.copy(fontSize = 14.sp, color = GunMetal),
                        )
                    }
                }
                Spacer(modifier = Modifier.fillMaxHeight(0.035f))
                Row(modifier = Modifier) {
                    Text(
                        modifier =
                            Modifier
                                .weight(1f)
                                .clickable { isExpanded(expanded.not()) },
                        text = frameContent,
                        maxLines = if (expanded) Int.MAX_VALUE else maxLines, // expanded 상태에 따라 줄 수 제한
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                HorizontalDivider(color = WhiteGray)
                Spacer(modifier = Modifier.height(20.dp))
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    Text(text = "해당 제작자의 다른 작품", style = Typography.bodyLarge)
                }
                Spacer(modifier = Modifier.fillMaxHeight(0.05f))
                if (creatorFrames.isEmpty()) {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .weight(1f),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "판매 중인 다른 프레임이 없어요",
                            style = Typography.labelLarge,
                            color = Gray02,
                        )
                    }
                } else {
                    LazyHorizontalGrid(
                        modifier =
                            modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(horizontal = 10.dp),
                        rows = GridCells.Fixed(1),
                        verticalArrangement = Arrangement.Center,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        state = rememberLazyGridState(),
                    ) {
                        items(creatorFrames.size) { index ->
                            val frame = creatorFrames[index]

                            GlideImage(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(1f)
                                        .padding(8.dp)
                                        .clip(shape = RoundedCornerShape(10.dp))
                                        .border(
                                            width = 1.dp,
                                            shape = RoundedCornerShape(10.dp),
                                            color = Gray03,
                                        ).noRippleClickable {
                                            navigateToDetail(frame.marketId)
                                        },
                                imageModel =
                                    frame.nftInfo.data.image
                                        .urlToIpfs(),
                                contentScale = ContentScale.Inside,
                                loading = {
                                    ImageLoadingProgress(
                                        modifier = Modifier,
                                    )
                                },
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier.align(Alignment.Start),
                    verticalAlignment = Alignment.Bottom,
                ) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Column(
                        modifier = Modifier,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Icon(
                            modifier =
                                Modifier
                                    .size(25.dp)
                                    .noRippleClickable {
                                        if (isLiked.not()) {
                                            onClickedLikeNft(true)
                                        } else {
                                            onClickedLikeNft(false)
                                        }
                                    },
                            painter =
                                if (isLiked.not()) {
                                    painterResource(id = R.drawable.ic_toggle_heart_off)
                                } else {
                                    painterResource(
                                        id = R.drawable.ic_toggle_heart_on,
                                    )
                                },
                            contentDescription = "",
                            tint = if (isLiked.not()) GunMetal else Red,
                        )
                        Text(
                            text = String.format(Locale.KOREAN, "%,.0f", heartCount.toDouble()),
                            style = Typography.bodySmall,
                        )
                    }
                    Spacer(modifier = Modifier.weight(0.05f))
                    if (canPurchase) {
                        LongBlackButton(
                            modifier =
                                Modifier
                                    .weight(1f)
                                    .height(50.dp),
                            icon = R.drawable.ic_color_sene_coin,
                            text =
                                String.format(
                                    Locale.KOREAN,
                                    "%,.0f ",
                                    price,
                                ) + stringResource(id = R.string.buy_price_message),
                            onClick = { onClickedPurchase(price.toLong()) },
                        )
                    } else {
                        LongBlackButton(
                            modifier =
                                Modifier
                                    .weight(1f)
                                    .height(50.dp),
                            icon = R.drawable.ic_color_sene_coin,
                            text = "프레임 판매 취소",
                            onClick = { cancelSale() },
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun FrameDetailScreenPreview() {
    SemonemoTheme {
        val hashTag = listOf("아이유", "인사이드 아웃", "단발")
        FrameDetailScreen(
            hashTag = hashTag,
            frameUrl = "https://flexible.img.hani.co.kr/flexible/normal/800/534/imgdb/original/2024/0318/20240318500152.jpg",
            profileImageUrl = "https://flexible.img.hani.co.kr/flexible/normal/800/534/imgdb/original/2024/0318/20240318500152.jpg",
        )
    }
}
