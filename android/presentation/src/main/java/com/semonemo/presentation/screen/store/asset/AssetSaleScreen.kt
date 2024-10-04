package com.semonemo.presentation.screen.store.asset

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.semonemo.domain.model.Asset
import com.semonemo.domain.model.SellAsset
import com.semonemo.presentation.R
import com.semonemo.presentation.component.HashTag
import com.semonemo.presentation.component.HashTagTextField
import com.semonemo.presentation.component.LoadingDialog
import com.semonemo.presentation.component.LongBlackButton
import com.semonemo.presentation.component.LongUnableButton
import com.semonemo.presentation.component.PriceTextField
import com.semonemo.presentation.theme.Gray01
import com.semonemo.presentation.theme.Gray02
import com.semonemo.presentation.theme.Gray03
import com.semonemo.presentation.theme.SemonemoTheme
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.util.addFocusCleaner
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AssetSaleRoute(
    modifier: Modifier,
    viewModel: AssetSaleViewModel = hiltViewModel(),
    navigateToStore: () -> Unit,
    onShowSnackBar: (String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AssetSaleContent(
        modifier = modifier,
        uiState = uiState,
        uiEvent = viewModel.uiEvent,
        onSaleButtonClick = viewModel::sellAsset,
        onShowSnackBar = onShowSnackBar,
        navigateToStore = navigateToStore,
    )
}

@Composable
fun AssetSaleContent(
    modifier: Modifier,
    uiState: AssetSaleUiState,
    uiEvent: SharedFlow<AssetSaleUiEvent>,
    onSaleButtonClick: (SellAsset) -> Unit,
    onShowSnackBar: (String) -> Unit,
    navigateToStore: () -> Unit,
) {
    LaunchedEffect(uiEvent) {
        uiEvent.collectLatest { event ->
            when (event) {
                is AssetSaleUiEvent.Error -> onShowSnackBar(event.message)
                AssetSaleUiEvent.SellSuccess -> navigateToStore()
            }
        }
    }
    if (uiState.isLoading) {
        LoadingDialog()
    }
    AssetSaleScreen(
        modifier = modifier,
        assets = uiState.assets,
        onSaleButtonClick = onSaleButtonClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssetSaleScreen(
    modifier: Modifier = Modifier,
    assets: List<Asset> = emptyList(),
    onSaleButtonClick: (SellAsset) -> Unit = {},
) {
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    var price by remember { mutableStateOf("") } // 판매가
    var showBottomSheet by remember { mutableStateOf(false) } // bottomSheet 보임 여부
    var selectedAsset by remember { mutableStateOf("") } // 선택된 에셋
    var asset by remember { mutableStateOf<Asset?>(null) }
    val tags = remember { mutableStateListOf<String>() }

    Surface(
        modifier =
            modifier
                .fillMaxSize()
                .background(color = Color.White)
                .verticalScroll(state = scrollState),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 15.dp)
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .addFocusCleaner(
                        focusManager = focusManager,
                    ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "에셋 판매",
                style = Typography.bodyMedium.copy(fontSize = 20.sp),
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(30.dp))
            Surface(
                modifier =
                    Modifier
                        .width(180.dp)
                        .height(240.dp),
                border = BorderStroke(width = 2.dp, color = Gray01),
                shape = RoundedCornerShape(10.dp),
                onClick = {
                    showBottomSheet = true
                },
            ) {
                if (selectedAsset != "") {
                    GlideImage(
                        imageModel = selectedAsset,
                        contentScale = ContentScale.Crop,
                    )
                } else {
                    Column(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_frame_plus),
                            contentDescription = "ic_frame_plus",
                            tint = Color.Unspecified,
                        )
                        Spacer(modifier = Modifier.height(9.dp))
                        Text(
                            text = "에셋을 추가해 주세요",
                            style = Typography.bodySmall.copy(fontSize = 15.sp),
                            color = Gray02,
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(30.dp))
            // 통신 성공인 경우
            AnimatedVisibility(
                visible = selectedAsset != "",
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically(),
            ) {
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .animateContentSize(),
                ) {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                        contentAlignment = Alignment.CenterStart,
                    ) {
                        Text(
                            text = stringResource(R.string.register_tag),
                            style = Typography.titleMedium.copy(fontSize = 16.sp),
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    HashTagTextField(
                        focusManager = focusManager,
                        onTagAddAction = { keyword ->
                            if (keyword.isNotBlank()) {
                                tags.add(keyword)
                            }
                        },
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        content = {
                            items(tags.size) { index ->
                                HashTag(
                                    keyword = tags[index],
                                    isEdit = true,
                                    onCloseClicked = {
                                        tags.remove(it)
                                    },
                                )
                            }
                        },
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                }
            }
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                contentAlignment = Alignment.CenterStart,
            ) {
                Text(
                    text = stringResource(R.string.register_price),
                    style = Typography.titleMedium.copy(fontSize = 16.sp),
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            PriceTextField(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                price = price,
                onPriceChange = { newPrice ->
                    price = newPrice
                },
            )
            Spacer(modifier = Modifier.height(30.dp))
            if (selectedAsset != "") {
                LongBlackButton(
                    modifier =
                        Modifier
                            .fillMaxWidth(),
                    text = stringResource(R.string.register_btn_title),
                    icon = null,
                    onClick = {
                        val sellAsset =
                            asset?.let {
                                SellAsset(
                                    assetId = it.assetId,
                                    price = price.toInt(),
                                    tags = tags,
                                )
                            }
                        sellAsset?.let {
                            onSaleButtonClick(sellAsset)
                        }
                    },
                )
            } else {
                LongUnableButton(
                    modifier =
                        Modifier
                            .fillMaxWidth(),
                    text = stringResource(R.string.register_btn_title),
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                containerColor = Color.White,
                shape = RoundedCornerShape(10.dp),
            ) {
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.45f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    LazyVerticalGrid(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(horizontal = 20.dp, vertical = 20.dp),
                        columns = GridCells.Fixed(4),
                        state = rememberLazyGridState(),
                    ) {
                        items(assets.size) { index ->
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
                                        ).clickable {
                                            selectedAsset = assets[index].imageUrl
                                            asset = assets[index]
                                            showBottomSheet = false
                                        },
                                imageModel = assets[index].imageUrl,
                                contentScale = ContentScale.Crop,
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Preview
@Composable
fun AssetSaleScreenPreview() {
    SemonemoTheme {
        AssetSaleScreen()
    }
}
