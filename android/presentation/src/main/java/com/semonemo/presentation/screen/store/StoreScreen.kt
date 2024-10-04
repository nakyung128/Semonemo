package com.semonemo.presentation.screen.store

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
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
import com.semonemo.presentation.component.LoadingDialog
import com.semonemo.presentation.component.SectionFullViewButton
import com.semonemo.presentation.component.SectionHeader
import com.semonemo.presentation.screen.store.subScreen.StoreSubScreen
import com.semonemo.presentation.theme.GunMetal
import com.semonemo.presentation.theme.SemonemoTheme
import com.semonemo.presentation.theme.White
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
) {
    LaunchedEffect(uiEvent) {
        uiEvent.collectLatest { event ->
            when (event) {
                is StoreUiEvent.Error -> onShowError(event.errorMessage)
            }
        }
    }
    if (uiState.isLoading) {
        LoadingDialog()
    }
    StoreScreen(
        modifier = modifier,
        navigateToAssetSale = navigateToAssetSale,
        navigateToFrameSale = navigateToFrameSale,
        navigateToFullView = navigateToFullView,
        navigateToSearch = navigateToSearch,
        saleFrames = uiState.saleFrame,
        navigateToFrameDetail = navigateToFrameDetail,
        saleAssets = uiState.saleAsset,
    )
}

@Composable
fun StoreScreen(
    modifier: Modifier = Modifier,
    navigateToFullView: (Boolean) -> Unit = {},
    navigateToSearch: () -> Unit = {},
    navigateToAssetSale: () -> Unit = {},
    navigateToFrameSale: () -> Unit = {},
    saleFrames: List<FrameDetail> = listOf(),
    saleAssets: List<SellAssetDetail> = listOf(),
    navigateToFrameDetail: (Long) -> Unit = {},
) {
    val verticalScrollState = rememberScrollState()

    val frameHeight = if (saleFrames.isEmpty()) 0.dp else 300.dp
    val assetHeight = if (saleAssets.isEmpty()) 0.dp else 400.dp

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
            // 아마 리스트형태로 뷰페이저로 들어갈듯
            HotRecentFrame(
                imgUrl = "https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyNDA3MjhfMjQ5%2FMDAxNzIyMTc0NDI3NTUx.2i13wuVFmNnbi_PAAaWFaMoH8dnfMCELiKLi3FzWDowg.Jpv5rH4kLAXvpQvH7ZSiFATG9sCXuZxNlSx-Ac3hXlEg.JPEG%2FIMG%25A3%25DF2672.JPG&type=a340",
            )
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

            StoreSubScreen(
                modifier =
                    Modifier
                        .height(frameHeight)
                        .fillMaxWidth(),
                isFrame = true,
                saleFrames = saleFrames,
                navigateToFrameDetail = navigateToFrameDetail,
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                SectionHeader(text = stringResource(id = R.string.section_header_sell_asset))
                SectionFullViewButton(onClick = { navigateToFullView(false) })
            }
            StoreSubScreen(
                modifier =
                    Modifier
                        .height(assetHeight)
                        .fillMaxWidth(),
                isFrame = false,
                saleAssets = saleAssets,
            )
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
    imgUrl: String = "",
) {
    Card(
        modifier = modifier.padding(horizontal = 10.dp).height(200.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = White),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
        ) {
            GlideImage(
                imageModel = imgUrl,
                contentScale = ContentScale.Crop,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .weight(1f),
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
