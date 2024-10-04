package com.semonemo.presentation.screen.store

import androidx.compose.foundation.background
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

@Composable
fun StoreRoute(
    modifier: Modifier = Modifier,
    navigateToFullView: (Boolean) -> Unit = {},
    navigateToSearch: () -> Unit = {},
    navigateToAssetSale: () -> Unit = {},
    navigateToFrameSale: () -> Unit = {},
    viewModel: StoreViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    StoreContent(
        modifier = modifier,
        navigateToAssetSale = navigateToAssetSale,
        navigateToFrameSale = navigateToFrameSale,
        navigateToFullView = navigateToFullView,
        navigateToSearch = navigateToSearch,
        uiState = uiState.value,
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
) {
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
) {
    val verticalScrollState = rememberScrollState()
    Surface(
        modifier =
            modifier
                .fillMaxSize()
                .background(color = Color.White),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .background(color = Color.White)
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
                Spacer(modifier.weight(1f))
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
                modifier = modifier.fillMaxWidth(),
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
                    modifier
                        .height(300.dp)
                        .fillMaxWidth(),
                isFrame = true,
                saleFrames,
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier =
                    modifier
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
                    modifier
                        .height(400.dp)
                        .fillMaxWidth(),
                isFrame = false,
            )
            Spacer(modifier = Modifier.height(100.dp))
        }
        CustomStoreFAB(
            modifier = modifier,
            navigateToAssetSale = navigateToAssetSale,
            navigateToFrameSale = navigateToFrameSale,
        )
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SectionHeader(text = stringResource(id = R.string.section_header_sell_frame))
            SectionFullViewButton(onClick = {
                navigateToFullView(true)
            })
        }

        StoreSubScreen(
            modifier =
                modifier
                    .height(300.dp)
                    .fillMaxWidth(),
            isFrame = true,
            saleFrames = saleFrames,
        )
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SectionHeader(text = stringResource(id = R.string.section_header_sell_asset))
            SectionFullViewButton(onClick = { navigateToFullView(false) })
        }
        StoreSubScreen(
            modifier =
                modifier
                    .height(400.dp)
                    .fillMaxWidth(),
            isFrame = false,
        )
        Spacer(modifier = Modifier.height(100.dp))
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
                modifier
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
