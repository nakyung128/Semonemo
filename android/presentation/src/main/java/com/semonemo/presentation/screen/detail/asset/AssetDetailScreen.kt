package com.semonemo.presentation.screen.detail.asset

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.semonemo.presentation.R
import com.semonemo.presentation.component.HashTag
import com.semonemo.presentation.component.LoadingDialog
import com.semonemo.presentation.component.LongBlackButton
import com.semonemo.presentation.component.NameWithBadge
import com.semonemo.presentation.component.TopAppBar
import com.semonemo.presentation.theme.GunMetal
import com.semonemo.presentation.theme.Red
import com.semonemo.presentation.theme.SemonemoTheme
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.theme.WhiteGray
import com.semonemo.presentation.util.noRippleClickable
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import java.util.Locale

@Composable
fun AssetDetailRoute(
    modifier: Modifier = Modifier,
    popUpBackStack: () -> Unit = {},
    onShowSnackBar: (String) -> Unit = {},
    viewModel: AssetDetailViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    AssetDetailContent(
        modifier = modifier,
        popUpBackStack = popUpBackStack,
        onShowSnackBar = onShowSnackBar,
        uiState = uiState.value,
        uiEvent = viewModel.uiEvent,
        onClickedLikeAsset = viewModel::onLikedAsset,
    )
}

@Composable
fun AssetDetailContent(
    modifier: Modifier = Modifier,
    popUpBackStack: () -> Unit = {},
    onShowSnackBar: (String) -> Unit = {},
    uiEvent: SharedFlow<AssetDetailUiEvent>,
    uiState: AssetDetailUiState,
    onClickedLikeAsset: (Boolean) -> Unit = {},
) {
    LaunchedEffect(uiEvent) {
        uiEvent.collectLatest { event ->
            when (event) {
                is AssetDetailUiEvent.Error -> onShowSnackBar(event.errorMessage)
            }
        }
    }

    if (uiState.isLoading) {
        LoadingDialog()
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
    )
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
) {
    val scrollState = rememberScrollState()
    val (expanded, isExpanded) =
        remember {
            mutableStateOf(false)
        }
    val maxLines = 2 // 2줄 이상일 때 아이콘 표시
    Surface(
        modifier =
            Modifier
                .fillMaxSize()
                .background(color = Color.White)
                .verticalScroll(state = scrollState),
    ) {
        Column(
            modifier =
                modifier
                    .statusBarsPadding()
                    .navigationBarsPadding(),
//                    .padding(horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            TopAppBar(
                modifier = Modifier,
                onNavigationClick = popUpBackStack,
            )
            GlideImage(
                imageModel = assetUrl.toUri(),
                contentScale = ContentScale.Fit,
                modifier =
                    Modifier
                        .fillMaxWidth(0.8f)
                        .padding(horizontal = 20.dp)
                        .aspectRatio(1f)
                        .background(color = WhiteGray, shape = RoundedCornerShape(10.dp)),
            )
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
            Row(
                modifier =
                    Modifier
                        .align(Alignment.Start)
                        .padding(horizontal = 10.dp),
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
            Row(
                modifier =
                    Modifier
                        .align(Alignment.Start)
                        .padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    modifier = Modifier,
                    painter = painterResource(id = R.drawable.img_graph),
                    contentDescription = "",
                )

                Text(text = stringResource(R.string.price_chart), style = Typography.bodyLarge)
            }
            Spacer(modifier = Modifier.height(10.dp))
            Image(
                modifier = Modifier,
                painter = painterResource(id = R.drawable.price_graph),
                contentDescription = "",
            )
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier.align(Alignment.Start),
                verticalAlignment = Alignment.Bottom,
            ) {
                Spacer(modifier = Modifier.weight(0.05f))
                Column(modifier = Modifier, horizontalAlignment = Alignment.CenterHorizontally) {
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
                )
            }
            Spacer(modifier = Modifier.height(0.dp))
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
        )
    }
}
