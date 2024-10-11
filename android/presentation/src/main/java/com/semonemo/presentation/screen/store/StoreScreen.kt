package com.semonemo.presentation.screen.store

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.semonemo.domain.model.FrameDetail
import com.semonemo.domain.model.SellAssetDetail
import com.semonemo.presentation.R
import com.semonemo.presentation.component.CustomStoreFAB
import com.semonemo.presentation.component.ImageLoadingProgress
import com.semonemo.presentation.component.LoadingDialog
import com.semonemo.presentation.component.SectionFullViewButton
import com.semonemo.presentation.component.SectionHeader
import com.semonemo.presentation.screen.store.subScreen.StoreSubScreen
import com.semonemo.presentation.theme.Gray02
import com.semonemo.presentation.theme.GunMetal
import com.semonemo.presentation.theme.SemonemoTheme
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.theme.White
import com.semonemo.presentation.util.urlToIpfs
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun StoreRoute(
    modifier: Modifier = Modifier,
    navigateToFullView: (Boolean) -> Unit = {},
    navigateToSearch: () -> Unit = {},
    navigateToAssetSale: () -> Unit = {},
    navigateToFrameSale: () -> Unit = {},
    viewModel: StoreViewModel = hiltViewModel(),
    navigateToFrameDetail: (Long) -> Unit = {},
    navigateToAssetDetail: (Long) -> Unit = {},
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    StoreContent(
        modifier = modifier,
        navigateToAssetSale = navigateToAssetSale,
        navigateToFrameSale = navigateToFrameSale,
        navigateToFullView = navigateToFullView,
        navigateToSearch = navigateToSearch,
        uiState = uiState.value,
        navigateToFrameDetail = navigateToFrameDetail,
        uiEvent = viewModel.uiEvent,
        navigateToAssetDetail = navigateToAssetDetail,
        loadInfo = viewModel::loadStoreInfo,
    )
}

@Composable
fun StoreContent(
    modifier: Modifier = Modifier,
    navigateToFullView: (Boolean) -> Unit = {},
    navigateToSearch: () -> Unit = {},
    navigateToAssetSale: () -> Unit = {},
    navigateToFrameSale: () -> Unit = {},
    uiState: StoreUiState,
    uiEvent: SharedFlow<StoreUiEvent>,
    onShowError: (String) -> Unit = {},
    navigateToFrameDetail: (Long) -> Unit = {},
    navigateToAssetDetail: (Long) -> Unit = {},
    loadInfo: () -> Unit,
) {
    LaunchedEffect(Unit) {
        loadInfo()
    }

    LaunchedEffect(uiEvent) {
        uiEvent.collectLatest { event ->
            when (event) {
                is StoreUiEvent.Error -> onShowError(event.errorMessage)
            }
        }
    }

    if (uiState.isLoading) {
        LoadingDialog(modifier = Modifier.fillMaxSize())
    }

    StoreScreen(
        modifier = modifier,
        navigateToAssetSale = navigateToAssetSale,
        navigateToFrameSale = navigateToFrameSale,
        navigateToFullView = navigateToFullView,
        navigateToSearch = navigateToSearch,
        hotFrames = uiState.hotFrame,
        saleFrames = uiState.saleFrame,
        saleAssets = uiState.saleAsset,
        navigateToFrameDetail = navigateToFrameDetail,
        navigateToAssetDetail = navigateToAssetDetail,
    )
}

@Composable
fun StoreScreen(
    modifier: Modifier = Modifier,
    navigateToFullView: (Boolean) -> Unit = {},
    navigateToSearch: () -> Unit = {},
    navigateToAssetSale: () -> Unit = {},
    navigateToFrameSale: () -> Unit = {},
    hotFrames: List<FrameDetail> = listOf(),
    saleFrames: List<FrameDetail> = listOf(),
    saleAssets: List<SellAssetDetail> = listOf(),
    navigateToFrameDetail: (Long) -> Unit = {},
    navigateToAssetDetail: (Long) -> Unit = {},
) {
    val verticalScrollState = rememberScrollState()

    val frameHeight = if (saleFrames.isEmpty()) 0.dp else 300.dp
    val assetHeight = if (saleAssets.isEmpty()) 0.dp else 400.dp

    Surface(
        modifier = modifier.fillMaxSize(),
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
                    text = stringResource(R.string.recent_popular_frame_header),
                )
                Spacer(Modifier.weight(1f))
                Icon(
                    modifier =
                        Modifier
                            .padding(10.dp)
                            .clickable {
                                navigateToSearch()
                            },
                    painter = painterResource(id = R.drawable.ic_search_magnifier),
                    contentDescription = null,
                    tint = GunMetal,
                )
            }
            if (hotFrames.isEmpty()) {
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "현재 인기 프레임이 없어요! 🥲",
                        style = Typography.labelLarge,
                        color = Gray02,
                    )
                }
            } else {
                LazyRow {
                    items(hotFrames.size) { index ->
                        val frame = hotFrames[index]
                        HotRecentFrame(
                            frame = frame,
                            navigateToFrameDetail = navigateToFrameDetail,
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                SectionHeader(
                    modifier = Modifier.padding(10.dp),
                    text = stringResource(id = R.string.section_header_sell_frame),
                )
                SectionFullViewButton(onClick = {
                    navigateToFullView(true)
                })
            }
            if (saleFrames.isEmpty()) {
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "현재 판매 중인 프레임이 없어요! 🥲",
                        style = Typography.labelLarge,
                        color = Gray02,
                    )
                }
            } else {
                StoreSubScreen(
                    modifier =
                        Modifier
                            .height(frameHeight)
                            .fillMaxWidth(),
                    isFrame = true,
                    saleFrames = saleFrames,
                    navigateToFrameDetail = navigateToFrameDetail,
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                SectionHeader(
                    modifier = Modifier.padding(10.dp),
                    text = stringResource(id = R.string.section_header_sell_asset),
                )
                SectionFullViewButton(onClick = {
                    navigateToFullView(false)
                })
            }
            if (saleAssets.isEmpty()) {
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "현재 판매 중인 에셋이 없어요! 🥲",
                        style = Typography.labelLarge,
                        color = Gray02,
                    )
                }
            } else {
                StoreSubScreen(
                    modifier =
                        Modifier
                            .height(assetHeight)
                            .fillMaxWidth(),
                    isFrame = false,
                    saleAssets = saleAssets,
                    navigateToAssetDetail = navigateToAssetDetail,
                )
            }
            Spacer(modifier = Modifier.height(100.dp))
        }
        CustomStoreFAB(
            modifier = modifier,
            navigateToAssetSale = navigateToAssetSale,
            navigateToFrameSale = navigateToFrameSale,
        )
    }
}

@Composable
fun HotRecentFrame(
    modifier: Modifier = Modifier,
    frame: FrameDetail = FrameDetail(),
    navigateToFrameDetail: (Long) -> Unit = {},
) {
    Card(
        modifier =
            modifier
                .width(200.dp)
                .padding(horizontal = 10.dp)
                .aspectRatio(3f / 4f)
                .clickable { navigateToFrameDetail(frame.marketId) },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = White),
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            GlideImage(
                imageModel =
                    frame.nftInfo.data.image
                        .urlToIpfs(),
                contentScale = ContentScale.Fit,
                modifier = Modifier.padding(vertical = 10.dp),
                loading = {
                    ImageLoadingProgress(
                        modifier = Modifier,
                    )
                },
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun preview() {
    SemonemoTheme {
        StoreScreen()
    }
}
