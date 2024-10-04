package com.semonemo.presentation.screen.detail.frame

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.style.TextOverflow
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
import com.semonemo.presentation.theme.SemonemoTheme
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.util.noRippleClickable
import com.semonemo.presentation.util.urlToIpfs
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import java.util.Locale

@Composable
fun FrameDetailRoute(
    modifier: Modifier = Modifier,
    popUpBackStack: () -> Unit = {},
    onShowSnackBar: (String) -> Unit = {},
    viewModel: FrameDetailViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    FrameDetailContent(
        modifier = modifier,
        popUpBackStack = popUpBackStack,
        onShowSnackBar = onShowSnackBar,
        uiState = uiState.value,
        uiEvent = viewModel.uiEvent,
    )
}

@Composable
fun FrameDetailContent(
    modifier: Modifier = Modifier,
    popUpBackStack: () -> Unit = {},
    onShowSnackBar: (String) -> Unit = {},
    uiEvent: SharedFlow<FrameDetailUiEvent>,
    uiState: FrameDetailUiState,
) {
    LaunchedEffect(uiEvent) {
        uiEvent.collectLatest { event ->
            when (event) {
                is FrameDetailUiEvent.Error -> onShowSnackBar(event.errorMessage)
            }
        }
    }

    if (uiState.isLoading) {
        LoadingDialog()
    }
    val frame = uiState.frame
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
        isHeart = frame.isLiked,
        heartCount = frame.likeCount,
        price = frame.price.toDouble(),
        profileImageUrl = frame.seller.profileImage,
    )
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
    isHeart: Boolean = true,
    heartCount: Long = 100000,
    price: Double = 100.1,
) {
    val scrollState = rememberScrollState()
    val (expanded, isExpanded) =
        remember {
            mutableStateOf(false)
        }
    val (heart, isHeart) =
        remember {
            mutableStateOf(isHeart)
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
                    .navigationBarsPadding()
                    .padding(horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            TopAppBar(
                modifier = Modifier,
                title = {
                    Text(text = frameTitle, style = Typography.bodyLarge.copy(fontSize = 20.sp))
                },
                onNavigationClick = popUpBackStack,
            )
            GlideImage(
                imageModel = frameUrl.toUri(),
                contentScale = ContentScale.Fit,
                modifier =
                    Modifier
                        .padding(horizontal = 20.dp)
                        .size(width = 265.dp, height = 365.dp),
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
            Row(
                modifier = Modifier.align(Alignment.Start),
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
            Row(
                modifier = Modifier.align(Alignment.Start),
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
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.align(Alignment.Start),
                verticalAlignment = Alignment.Bottom,
            ) {
                Spacer(modifier = Modifier.weight(0.05f))
                Column(modifier = Modifier, horizontalAlignment = Alignment.CenterHorizontally) {
                    if (heart) {
                        Icon(
                            modifier =
                                Modifier
                                    .size(25.dp)
                                    .noRippleClickable { isHeart(heart.not()) },
                            painter =
                                painterResource(id = R.drawable.ic_toggle_heart_off),
                            contentDescription = "",
                        )
                    } else {
                        Icon(
                            modifier =
                                Modifier
                                    .size(25.dp)
                                    .noRippleClickable { isHeart(heart.not()) },
                            painter =
                                painterResource(id = R.drawable.ic_toggle_heart_on),
                            contentDescription = "",
                            tint = Color.Red,
                        )
                    }
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
                            .height(45.dp),
                    icon = R.drawable.ic_color_sene_coin,
                    text =
                        String.format(
                            Locale.KOREAN,
                            "%,.0f ",
                            price,
                        ) + stringResource(id = R.string.buy_price_message),
                )
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
