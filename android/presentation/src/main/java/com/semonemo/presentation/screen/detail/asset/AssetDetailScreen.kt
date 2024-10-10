package com.semonemo.presentation.screen.detail.asset

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.semonemo.domain.model.SellAssetDetail
import com.semonemo.presentation.BuildConfig
import com.semonemo.presentation.R
import com.semonemo.presentation.component.HashTag
import com.semonemo.presentation.component.LoadingDialog
import com.semonemo.presentation.component.LongBlackButton
import com.semonemo.presentation.component.LongUnableButton
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
import com.semonemo.presentation.util.toPrice
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.generated.Uint256
import java.util.Locale

@Composable
fun AssetDetailRoute(
    modifier: Modifier = Modifier,
    popUpBackStack: () -> Unit = {},
    onShowSnackBar: (String) -> Unit = {},
    viewModel: AssetDetailViewModel = hiltViewModel(),
    nftViewModel: NftViewModel = hiltViewModel(),
    navigateToDetail: (Long) -> Unit = {},
    navigateToProfile: (Long) -> Unit = {},
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    AssetDetailContent(
        modifier = modifier,
        popUpBackStack = popUpBackStack,
        onShowSnackBar = onShowSnackBar,
        uiState = uiState.value,
        uiEvent = viewModel.uiEvent,
        onClickedLikeAsset = viewModel::onLikedAsset,
        onClickedPurchase = viewModel::getBalance,
        sendTransaction = { price ->
            nftViewModel.sendTransaction(
                function =
                    Function(
                        "transferBalance",
                        listOf(
                            Address(uiState.value.asset.creator.address),
                            Uint256(price.toBigInteger().toPrice()),
                        ),
                        listOf<TypeReference<*>>(object : TypeReference<Uint256>() {}),
                    ),
                onSuccess = { hash ->
                    viewModel.purchaseAsset(
                        txHash = hash,
                        assetSellId = uiState.value.asset.assetSellId,
                    )
                },
                onError = {
                    onShowSnackBar(it)
                },
                contractAddress = BuildConfig.SYSTEM_CONTRACT_ADDRESS,
            )
        },
        navigateToDetail = navigateToDetail,
        navigateToProfile = {
            navigateToProfile(uiState.value.asset.creator.userId)
        },
    )
}

@Composable
fun AssetDetailContent(
    modifier: Modifier = Modifier,
    popUpBackStack: () -> Unit = {},
    onShowSnackBar: (String) -> Unit = {},
    onClickedPurchase: (Long) -> Unit = {},
    uiEvent: SharedFlow<AssetDetailUiEvent>,
    uiState: AssetDetailUiState,
    onClickedLikeAsset: (Boolean) -> Unit = {},
    sendTransaction: (Long) -> Unit = {},
    navigateToDetail: (Long) -> Unit = {},
    navigateToProfile: () -> Unit = {},
) {
    LaunchedEffect(uiEvent) {
        uiEvent.collectLatest { event ->
            when (event) {
                is AssetDetailUiEvent.Error -> onShowSnackBar(event.errorMessage)
                is AssetDetailUiEvent.Purchase -> sendTransaction(event.price)
                AssetDetailUiEvent.Success -> popUpBackStack()
            }
        }
    }

    val asset = uiState.asset
    AssetDetailScreen(
        modifier = modifier,
        popUpBackStack = popUpBackStack,
        assetUrl = asset.imageUrl,
        hashTag = asset.tags,
        hasBadge = true,
        nickname = asset.creator.nickname,
        isLiked = uiState.isLiked,
        heartCount = uiState.likedCount,
        price = asset.price.toDouble(),
        profileImageUrl = asset.creator.profileImage,
        onClickedAsset = onClickedLikeAsset,
        onClickedPurchase = onClickedPurchase,
        canPurchase = (uiState.userId == asset.creator.userId).not(),
        creatorAssets = uiState.creatorAssets,
        navigateToDetail = navigateToDetail,
        navigateToProfile = navigateToProfile,
    )
    if (uiState.isLoading) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .clickable(enabled = false) {},
        )
        LoadingDialog(
            lottieRes = R.raw.normal_load,
            loadingMessage = stringResource(R.string.load_asset_purchase),
            subMessage = stringResource(R.string.loading_sub_message),
        )
    }
}

@Composable
fun AssetDetailScreen(
    modifier: Modifier = Modifier,
    popUpBackStack: () -> Unit = {},
    assetUrl: String = "",
    hashTag: List<String> = listOf(),
    profileImageUrl: String = "",
    nickname: String = "짜이한",
    hasBadge: Boolean = true,
    isLiked: Boolean = true,
    heartCount: Long = 100000,
    price: Double = 100.1,
    onClickedAsset: (Boolean) -> Unit = {},
    onClickedPurchase: (Long) -> Unit = {},
    canPurchase: Boolean = true,
    creatorAssets: List<SellAssetDetail> = listOf(),
    navigateToDetail: (Long) -> Unit = {},
    navigateToProfile: () -> Unit = {},
) {
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
                    .background(color = Color.White)
                    .statusBarsPadding()
                    .navigationBarsPadding(),
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            TopAppBar(
                modifier = Modifier,
                onNavigationClick = popUpBackStack,
            )
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.fillMaxHeight(0.015f))
                GlideImage(
                    imageModel = assetUrl.toUri(),
                    contentScale = ContentScale.Fit,
                    modifier =
                        Modifier
                            .fillMaxWidth(0.8f)
                            .padding(horizontal = 20.dp)
                            .aspectRatio(1f)
                            .background(
                                color = WhiteGray.copy(alpha = 0.7f),
                                shape = RoundedCornerShape(10.dp),
                            ),
                )
                Spacer(modifier = Modifier.fillMaxHeight(0.025f))
                LazyRow(
                    modifier =
                        Modifier
                            .wrapContentHeight()
                            .padding(horizontal = 10.dp)
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
                        Modifier
                            .align(Alignment.Start)
                            .padding(horizontal = 10.dp)
                            .noRippleClickable {
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
                Spacer(modifier = Modifier.height(20.dp))
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 15.dp),
                    color = WhiteGray,
                )
                Spacer(modifier = Modifier.height(20.dp))
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(start = 15.dp),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    Text(text = "해당 제작자의 다른 작품", style = Typography.bodyLarge)
                }
                Spacer(modifier = Modifier.height(15.dp))
                if (creatorAssets.isEmpty()) {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .weight(1f),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "판매 중인 다른 에셋이 없어요",
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
                        rows = GridCells.Fixed(2),
                        verticalArrangement = Arrangement.Center,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        state = rememberLazyGridState(),
                    ) {
                        items(creatorAssets.size) { index ->
                            val asset = creatorAssets[index]
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
                                            navigateToDetail(asset.assetSellId)
                                        },
                                imageModel = asset.imageUrl,
                                contentScale = ContentScale.Inside,
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier.align(Alignment.Start),
                    verticalAlignment = Alignment.Bottom,
                ) {
                    Spacer(modifier = Modifier.weight(0.05f))
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
                                            onClickedAsset(true)
                                        } else {
                                            onClickedAsset(false)
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
                                    .padding(end = 10.dp)
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
                        LongUnableButton(
                            modifier =
                                Modifier
                                    .weight(1f)
                                    .padding(end = 10.dp)
                                    .height(50.dp),
                            text = "본인이 제작한 에셋은 구매할 수 없습니다.",
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
fun AssetDetailScreenPreview() {
    SemonemoTheme {
        val hashTag = listOf("아이유", "인사이드 아웃", "단발")
        AssetDetailScreen(
            hashTag = hashTag,
            assetUrl = "https://flexible.img.hani.co.kr/flexible/normal/800/534/imgdb/original/2024/0318/20240318500152.jpg",
            profileImageUrl = "https://flexible.img.hani.co.kr/flexible/normal/800/534/imgdb/original/2024/0318/20240318500152.jpg",
            canPurchase = false,
        )
    }
}
