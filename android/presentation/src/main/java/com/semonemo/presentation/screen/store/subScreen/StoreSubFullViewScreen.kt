package com.semonemo.presentation.screen.store.subScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.semonemo.domain.model.FrameDetail
import com.semonemo.domain.model.SellAssetDetail
import com.semonemo.presentation.component.CustomStoreFilter
import com.semonemo.presentation.component.LoadingDialog
import com.semonemo.presentation.component.StoreFilter
import com.semonemo.presentation.component.StoreItemCard
import com.semonemo.presentation.component.TopAppBar
import com.semonemo.presentation.component.TopAppBarNavigationType
import com.semonemo.presentation.component.assetFilters
import com.semonemo.presentation.component.frameFilters
import com.semonemo.presentation.theme.Gray02
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.util.urlToIpfs
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun StoreSubFullViewRoute(
    modifier: Modifier,
    isFrame: Boolean,
    viewModel: StoreFullViewModel = hiltViewModel(),
    popUpBackStack: () -> Unit,
    navigateToFrameDetail: (Long) -> Unit,
    navigateToAssetDetail: (Long) -> Unit,
    navigateToAssetSale: () -> Unit,
    navigateToFrameSale: () -> Unit,
    onShowErrorSnackBar: (String) -> Unit,
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    StoreSubFullContent(
        modifier = modifier,
        isFrame = isFrame,
        uiState = uiState.value,
        uiEvent = viewModel.uiEvent,
        loadNftList = viewModel::loadNftList,
        loadAssetList = viewModel::loadAssetList,
        popUpBackStack = popUpBackStack,
        navigateToFrameDetail = navigateToFrameDetail,
        navigateToAssetDetail = navigateToAssetDetail,
        navigateToAssetSale = navigateToAssetSale,
        navigateToFrameSale = navigateToFrameSale,
        onShowErrorSnackBar = onShowErrorSnackBar,
    )
}

@Composable
fun StoreSubFullContent(
    modifier: Modifier,
    isFrame: Boolean,
    uiState: StoreFullUiState,
    uiEvent: SharedFlow<StoreFullUiEvent>,
    loadNftList: (String) -> Unit,
    loadAssetList: (String) -> Unit,
    popUpBackStack: () -> Unit,
    navigateToFrameDetail: (Long) -> Unit,
    navigateToAssetDetail: (Long) -> Unit,
    navigateToAssetSale: () -> Unit,
    navigateToFrameSale: () -> Unit,
    onShowErrorSnackBar: (String) -> Unit,
) {
    LaunchedEffect(Unit) {
        if (isFrame) {
            loadNftList("latest")
        } else {
            loadAssetList("latest")
        }
    }
    LaunchedEffect(uiEvent) {
        uiEvent.collectLatest { event ->
            when (event) {
                is StoreFullUiEvent.Error -> onShowErrorSnackBar(event.errorMessage)
            }
        }
    }

    if (uiState.isLoading) {
        LoadingDialog(modifier = Modifier.fillMaxSize())
    }

    StoreSubFullViewScreen(
        modifier = modifier,
        isFrame = isFrame,
        popUpBackStack = popUpBackStack,
        navigateToFrameDetail = navigateToFrameDetail,
        navigateToAssetDetail = navigateToAssetDetail,
        navigateToAssetSale = navigateToAssetSale,
        navigateToFrameSale = navigateToFrameSale,
        frameList = uiState.frameList,
        assetList = uiState.assetList,
        loadNftList = loadNftList,
        loadAssetList = loadAssetList,
    )
}

@Composable
fun StoreSubFullViewScreen(
    modifier: Modifier = Modifier,
    isFrame: Boolean = false,
    popUpBackStack: () -> Unit = {},
    navigateToFrameDetail: (Long) -> Unit = {},
    navigateToAssetDetail: (Long) -> Unit = {},
    navigateToAssetSale: () -> Unit = {},
    navigateToFrameSale: () -> Unit = {},
    frameList: List<FrameDetail> = listOf(),
    assetList: List<SellAssetDetail> = listOf(),
    loadNftList: (String) -> Unit = {},
    loadAssetList: (String) -> Unit = {},
) {
    var filterStates by remember {
        mutableStateOf(
            StoreFilter.entries.associateWith { filter ->
                when (filter) {
                    StoreFilter.DATE -> false
                    else -> null
                }
            },
        )
    }

    fun loadNftBasedOnFilter() {
        when {
            filterStates[StoreFilter.DATE] == true -> loadNftList("oldest")
            filterStates[StoreFilter.DATE] == false -> loadNftList("latest")
            filterStates[StoreFilter.PRICE] == true -> loadNftList("low")
            filterStates[StoreFilter.PRICE] == false -> loadNftList("high")
            filterStates[StoreFilter.LIKE] == true -> loadNftList("dislike")
            filterStates[StoreFilter.LIKE] == false -> loadNftList("like")
        }
    }

    fun loadAssetBasedOnFilter() {
        when {
            filterStates[StoreFilter.DATE] == true -> loadAssetList("oldest")
            filterStates[StoreFilter.DATE] == false -> loadAssetList("latest")
            filterStates[StoreFilter.PRICE] == true -> loadAssetList("low")
            filterStates[StoreFilter.PRICE] == false -> loadAssetList("high")
            filterStates[StoreFilter.VIEW] == true -> loadAssetList("unhit")
            filterStates[StoreFilter.VIEW] == false -> loadAssetList("hit")
            filterStates[StoreFilter.SALE] == true -> loadAssetList("unsell")
            filterStates[StoreFilter.SALE] == false -> loadAssetList("sell")
            filterStates[StoreFilter.LIKE] == true -> loadAssetList("dislike")
            filterStates[StoreFilter.LIKE] == false -> loadAssetList("like")
        }
    }

    Column(
        modifier = modifier,
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        TopAppBar(
            modifier = Modifier.fillMaxWidth(),
            navigationType = TopAppBarNavigationType.Back,
            onNavigationClick = popUpBackStack,
        )
        Spacer(modifier = Modifier.height(10.dp))
        CustomStoreFilter(
            modifier = Modifier,
            filters = if (isFrame) frameFilters else assetFilters,
            filterStates = filterStates,
            onFilterChange = { selectedFilter, newState ->
                // 선택된 필터만 활성화하고 나머지는 null로
                filterStates =
                    filterStates.mapValues { (filter, _) ->
                        if (filter == selectedFilter) newState else null
                    }
                if (isFrame) {
                    loadNftBasedOnFilter()
                } else {
                    loadAssetBasedOnFilter()
                }
            },
        )
        Spacer(modifier = Modifier.height(10.dp))
        when (isFrame) {
            true -> {
                if (frameList.isEmpty()) {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "현재 판매 중인 프레임이 없어요! 🥲",
                            style = Typography.labelLarge,
                            color = Gray02,
                        )
                    }
                } else {
                    LazyVerticalGrid(
                        modifier = Modifier,
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(8.dp, 8.dp, 8.dp, 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(frameList.size) { index ->
                            val frame = frameList[index]
                            StoreItemCard(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(0.6f)
                                        .clickable {
                                            navigateToFrameDetail(frame.marketId)
                                        },
                                author = frame.seller.nickname,
                                imgUrl =
                                    frame.nftInfo.data.image
                                        .urlToIpfs(),
                                price = frame.price.toInt(),
                                isLiked = frame.isLiked,
                            )
                        }
                    }
                }
            }

            false -> {
                if (assetList.isEmpty()) {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "현재 판매 중인 에셋이 없어요! 🥲",
                            style = Typography.labelLarge,
                            color = Gray02,
                        )
                    }
                } else {
                    LazyVerticalGrid(
                        modifier = Modifier,
                        columns = GridCells.Fixed(3),
                        contentPadding = PaddingValues(8.dp, 8.dp, 8.dp, 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(assetList.size) { index ->
                            val asset = assetList[index]
                            StoreItemCard(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(0.8f)
                                        .clickable {
                                            navigateToAssetDetail(asset.assetSellId)
                                        },
                                author = asset.creator.nickname,
                                imgUrl = asset.imageUrl,
                                price = asset.price.toInt(),
                                isLiked = asset.isLiked,
                            )
                        }
                    }
                }
            }
        }
    }
}
