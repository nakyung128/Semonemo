package com.semonemo.presentation.screen.store.frame

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.runtime.mutableIntStateOf
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
import com.semonemo.domain.model.myFrame.MyFrame
import com.semonemo.presentation.BuildConfig
import com.semonemo.presentation.R
import com.semonemo.presentation.component.CustomTab
import com.semonemo.presentation.component.HashTag
import com.semonemo.presentation.component.LoadingDialog
import com.semonemo.presentation.component.LongBlackButton
import com.semonemo.presentation.component.LongUnableButton
import com.semonemo.presentation.component.PriceTextField
import com.semonemo.presentation.component.TopAppBar
import com.semonemo.presentation.screen.nft.NftViewModel
import com.semonemo.presentation.theme.Gray01
import com.semonemo.presentation.theme.Gray02
import com.semonemo.presentation.theme.Gray03
import com.semonemo.presentation.theme.SemonemoTheme
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.theme.WhiteGray
import com.semonemo.presentation.util.addFocusCleaner
import com.semonemo.presentation.util.noRippleClickable
import com.semonemo.presentation.util.toPrice
import com.semonemo.presentation.util.urlToIpfs
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.generated.Uint256

@Composable
fun FrameSaleRoute(
    modifier: Modifier,
    popUpBackStack: () -> Unit,
    viewModel: FrameSaleViewModel = hiltViewModel(),
    nftViewModel: NftViewModel = hiltViewModel(),
    onShowSnackBar: (String) -> Unit,
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    FrameSaleContent(
        modifier = modifier,
        popUpBackStack = popUpBackStack,
        uiState = uiState.value,
        uiEvent = viewModel.uiEvent,
        onShowSnackBar = onShowSnackBar,
        selectFrame = viewModel::selectFrame,
        sendTransaction = { price ->
            nftViewModel.sendTransaction(
                function =
                    Function(
                        "createMarket",
                        listOf(
                            Uint256(
                                uiState.value.selectFrame
                                    ?.nftInfo
                                    ?.tokenId ?: 0L,
                            ),
                            Uint256(price.toBigInteger().toPrice()),
                        ),
                        listOf<TypeReference<*>>(object : TypeReference<Uint256>() {}),
                    ),
                onSuccess = { tx ->
                    viewModel.sellRegisterFrame(price = price.toBigInteger(), hash = tx)
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
fun FrameSaleContent(
    modifier: Modifier,
    popUpBackStack: () -> Unit,
    uiState: FrameSaleUiState,
    uiEvent: SharedFlow<FrameSaleUiEvent>,
    onShowSnackBar: (String) -> Unit,
    selectFrame: (MyFrame) -> Unit,
    sendTransaction: (String) -> Unit = {},
) {
    LaunchedEffect(uiEvent) {
        uiEvent.collectLatest { event ->
            when (event) {
                is FrameSaleUiEvent.Error -> onShowSnackBar(event.errorMessage)
                FrameSaleUiEvent.SaleDone -> popUpBackStack()
            }
        }
    }
    if (uiState.isLoading) {
        LoadingDialog()
    }
    FrameSaleScreen(
        modifier = modifier,
        popUpBackStack = popUpBackStack,
        frames = uiState.frames,
        selectFrame = selectFrame,
        selectedFrame = uiState.selectFrame,
        sendTransaction = sendTransaction,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FrameSaleScreen(
    modifier: Modifier = Modifier,
    popUpBackStack: () -> Unit = {},
    frames: List<MyFrame> = listOf(),
    selectedFrame: MyFrame? = null,
    selectFrame: (MyFrame) -> Unit = {},
    sendTransaction: (String) -> Unit = {},
) {
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    var price by remember { mutableStateOf("") } // 판매가
    var showBottomSheet by remember { mutableStateOf(false) } // bottomSheet 보임 여부
    var selectedIndex by remember { mutableIntStateOf(0) } // bottomSheet 선택된 탭 index

    val tabs = listOf("1x1", "2x2", "1x4")

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
            TopAppBar(modifier = Modifier, title = {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.frame_register_title),
                    style = Typography.bodyMedium.copy(fontSize = 20.sp),
                    textAlign = TextAlign.Center,
                )
            }, onNavigationClick = popUpBackStack)

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
                if (selectedFrame != null) {
                    GlideImage(
                        imageModel =
                            selectedFrame.nftInfo.data.image
                                .urlToIpfs(),
                        contentScale = ContentScale.Fit,
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
                            text = stringResource(R.string.frame_add_title),
                            style = Typography.bodySmall.copy(fontSize = 15.sp),
                            color = Gray02,
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(30.dp))
            // 통신 성공인 경우
            AnimatedVisibility(
                visible = selectedFrame != null,
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
                            text = stringResource(R.string.register_frame_title),
                            style = Typography.titleMedium.copy(fontSize = 16.sp),
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                        shape = RoundedCornerShape(10.dp),
                        color = WhiteGray,
                    ) {
                        Box(
                            modifier =
                                Modifier
                                    .fillMaxSize()
                                    .padding(start = 14.dp, end = 14.dp),
                            contentAlignment = Alignment.CenterStart,
                        ) {
                            selectedFrame?.let {
                                Text(
                                    text = selectedFrame.nftInfo.data.title,
                                    style = Typography.labelSmall.copy(fontSize = 13.sp),
                                    color = Gray01,
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(15.dp))
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                        contentAlignment = Alignment.CenterStart,
                    ) {
                        Text(
                            text = stringResource(R.string.register_frame_discription),
                            style = Typography.titleMedium.copy(fontSize = 16.sp),
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .heightIn(min = 130.dp),
                        shape = RoundedCornerShape(10.dp),
                        color = WhiteGray,
                    ) {
                        Box(
                            modifier =
                                Modifier
                                    .fillMaxSize()
                                    .padding(14.dp),
                            contentAlignment = Alignment.TopStart,
                        ) {
                            selectedFrame?.let {
                                Text(
                                    text = selectedFrame.nftInfo.data.content,
                                    style = Typography.labelSmall.copy(fontSize = 13.sp),
                                    color = Gray01,
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
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
                    LazyRow(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        content = {
                            val tags =
                                selectedFrame?.let {
                                    it.tags
                                } ?: listOf()
                            items(tags.size) { index ->
                                HashTag(
                                    keyword = tags[index],
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
            // 프레임 불러오기 Success면 LongBlackButton
            // 다른 상태면 LongUnableButton
            Spacer(modifier = Modifier.height(30.dp))
            if (selectedFrame != null) {
                LongBlackButton(
                    modifier =
                        Modifier
                            .fillMaxWidth(),
                    text = stringResource(R.string.register_btn_title),
                    icon = null,
                    onClick = {
                        sendTransaction(price)
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
                    Spacer(modifier = Modifier.height(10.dp))
                    CustomTab(
                        tabList = tabs,
                        selectedIndex = selectedIndex,
                        onTabSelected = { tab -> selectedIndex = tab },
                    )
                    AnimatedContent(
                        targetState = selectedIndex,
                        transitionSpec = {
                            (slideInHorizontally { width -> width } + fadeIn())
                                .togetherWith(
                                    slideOutHorizontally { width -> -width } + fadeOut(),
                                ).using(
                                    SizeTransform(clip = false),
                                )
                        },
                        label = "",
                    ) { targetIndex ->
                        LazyVerticalGrid(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .padding(horizontal = 20.dp, vertical = 20.dp),
                            columns = GridCells.Fixed(4),
                            state = rememberLazyGridState(),
                        ) {
                            val frameList = frames.filter { it.frameType == (selectedIndex + 1) }
                            items(frameList.size) { index ->
                                val frame = frameList[index]
                                GlideImage(
                                    imageModel =
                                        frame.nftInfo.data.image
                                            .urlToIpfs(),
                                    contentScale = ContentScale.Fit,
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
                                                showBottomSheet = false
                                                selectFrame(frame)
                                            },
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegisterScreenPreview() {
    SemonemoTheme {
        FrameSaleScreen()
    }
}
