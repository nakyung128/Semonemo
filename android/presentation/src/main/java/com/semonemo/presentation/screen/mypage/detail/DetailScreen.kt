package com.semonemo.presentation.screen.mypage.detail

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.semonemo.presentation.BuildConfig
import com.semonemo.presentation.R
import com.semonemo.presentation.component.CustomDialog
import com.semonemo.presentation.component.HashTag
import com.semonemo.presentation.component.LoadingDialog
import com.semonemo.presentation.component.LongWhiteButton
import com.semonemo.presentation.component.PrivateTag
import com.semonemo.presentation.component.TopAppBar
import com.semonemo.presentation.theme.Main01
import com.semonemo.presentation.theme.SemonemoTheme
import com.semonemo.presentation.theme.Typography
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

// 다이얼로그 타입
enum class DialogType {
    PUBLIC,
    SALE,
}

@Composable
fun DetailRoute(
    modifier: Modifier,
    viewModel: DetailViewModel = hiltViewModel(),
    onSaleClicked: () -> Unit = {},
    onShowSnackBar: (String) -> Unit,
    popUpBackStack: () -> Unit,
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    DetailContent(
        modifier = modifier,
        uiEvent = viewModel.uiEvent,
        uiState = uiState.value,
        onPublicClicked = viewModel::openNft,
        onSaleClicked = onSaleClicked,
        onShowSnackBar = onShowSnackBar,
        popUpBackStack = popUpBackStack,
    )
}

@Composable
fun DetailContent(
    modifier: Modifier,
    uiEvent: SharedFlow<DetailUiEvent>,
    uiState: DetailUiState,
    onPublicClicked: () -> Unit,
    onSaleClicked: () -> Unit,
    onShowSnackBar: (String) -> Unit,
    popUpBackStack: () -> Unit,
) {
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(uiEvent) {
        uiEvent.collectLatest { event ->
            when (event) {
                is DetailUiEvent.OpenSuccess -> {
                    onShowSnackBar(event.message)
                }

                is DetailUiEvent.Error -> {
                    onShowSnackBar(event.errorMessage)
                    isLoading = false
                }

                else -> {
                    isLoading = true
                }
            }
        }
    }
    if (isLoading) {
        Log.d("nakyung", "여기")
        LoadingDialog()
    }

    DetailScreen(
        modifier = modifier,
        owner = uiState.owner,
        profileImg = uiState.profileImg,
        tags = uiState.tags,
        isOpen = uiState.isOpen,
        isOnSale = uiState.isOnSale,
        title = uiState.title,
        content = uiState.content,
        frameImg = uiState.image,
        onPublicClicked = onPublicClicked,
        onSaleClicked = onSaleClicked,
        popUpBackStack = popUpBackStack,
    )
}

@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    owner: String = "",
    profileImg: String = "",
    tags: List<String> = listOf(),
    isOpen: Boolean = false,
    isOnSale: Boolean = false,
    title: String = "",
    content: String = "",
    frameImg: String = "",
    onPublicClicked: () -> Unit = {},
    onSaleClicked: () -> Unit = {},
    popUpBackStack: () -> Unit = {},
) {
    val scrollState = rememberScrollState()
    var showDialog by remember { mutableStateOf<DialogType?>(null) }

    val ipfsUrl = BuildConfig.IPFS_READ_URL
    val imgUrl = ipfsUrl + "ipfs/" + frameImg

    @Composable
    fun showCustomDialog(
        title: String,
        content: String,
        onConfirmClicked: () -> Unit,
        titleKeywords: List<String> = emptyList(),
        contentKeywords: List<String> = emptyList(),
    ) {
        CustomDialog(
            title = title,
            content = content,
            onConfirmMessage = stringResource(R.string.mypage_change_title),
            onDismissMessage = stringResource(R.string.mypage_cancel_tag),
            titleKeywords = titleKeywords,
            contentKeywords = contentKeywords,
            titleBrushFlag = titleKeywords.map { false },
            contentBrushFlag = contentKeywords.map { false },
            onConfirm = {
                showDialog = null // 다이얼로그 닫기
                onConfirmClicked()
            },
            onDismiss = {
                showDialog = null
            },
        )
    }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .verticalScroll(state = scrollState)
                .background(brush = Main01)
                .statusBarsPadding()
                .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        TopAppBar(
            onNavigationClick = popUpBackStack,
        )
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = title,
                style = Typography.bodyLarge,
            )
            if (!isOpen) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd,
                ) {
                    PrivateTag(
                        modifier = Modifier.padding(end = 10.dp),
                        title = stringResource(R.string.private_tag),
                    )
                }
            } else {
                if (!isOnSale) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterEnd,
                    ) {
                        PrivateTag(
                            modifier = Modifier.padding(end = 10.dp),
                            title = stringResource(R.string.not_sale_tag),
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
        GlideImage(
            modifier =
                Modifier
                    .size(width = 265.dp, height = 365.dp),
            contentScale = ContentScale.Fit,
            imageModel = imgUrl,
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            GlideImage(
                modifier =
                    Modifier
                        .size(25.dp)
                        .clip(CircleShape),
                imageModel = profileImg,
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = owner,
                style = Typography.bodySmall.copy(fontSize = 15.sp),
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
            contentAlignment = Alignment.CenterStart,
        ) {
            Text(
                text = content,
                style = Typography.labelMedium.copy(fontSize = 18.sp),
            )
        }
        Spacer(modifier = Modifier.height(15.dp))
        LazyRow(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            content = {
                items(count = tags.size) { index ->
                    HashTag(keyword = tags[index])
                }
            },
        )
        Spacer(modifier = Modifier.height(50.dp))
        if (!isOpen) {
            LongWhiteButton(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                icon = null,
                text = stringResource(R.string.change_to_public_nft),
                onClick = { showDialog = DialogType.PUBLIC },
            )
        } else {
            if (!isOnSale) {
                LongWhiteButton(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                    icon = null,
                    text = stringResource(R.string.change_to_private_nft),
                    onClick = { showDialog = DialogType.PUBLIC },
                )
                Spacer(modifier = Modifier.height(8.dp))
                LongWhiteButton(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                    icon = null,
                    text = stringResource(R.string.change_to_sale_nft),
                    onClick = { showDialog = DialogType.SALE },
                )
            } else {
                LongWhiteButton(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                    icon = null,
                    text = stringResource(id = R.string.change_to_private_nft),
                    onClick = { showDialog = DialogType.PUBLIC },
                )
                Spacer(modifier = Modifier.height(8.dp))
                LongWhiteButton(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                    icon = null,
                    text = stringResource(R.string.change_to_not_sale_nft),
                    onClick = { showDialog = DialogType.SALE },
                )
            }
        }
    }
    when (showDialog) {
        DialogType.PUBLIC -> {
            val (dialogTitle, dialogContent) =
                if (!isOpen) {
                    stringResource(R.string.public_dialog_title) to stringResource(R.string.public_dialog_content)
                } else {
                    stringResource(R.string.private_dialog_title) to stringResource(R.string.private_dialog_content)
                }

            val (titleKeywords, contentKeywords) =
                if (!isOpen) {
                    listOf("공개로 변경") to listOf("판매", "비공개로 변경")
                } else {
                    listOf("비공개로 변경") to listOf("볼 수 없으며", "공개로 변경")
                }

            showCustomDialog(
                title = dialogTitle,
                content = dialogContent,
                onConfirmClicked = onPublicClicked,
                titleKeywords = titleKeywords,
                contentKeywords = contentKeywords,
            )
        }

        DialogType.SALE -> {
            val (dialogTitle, dialogContent) =
                if (!isOnSale) {
                    stringResource(R.string.sale_dialog_title) to stringResource(R.string.sale_dialog_content)
                } else {
                    stringResource(R.string.not_sale_dialog_title) to stringResource(R.string.not_sale_dialog_content)
                }

            val (titleKeywords, contentKeywords) =
                if (!isOnSale) {
                    listOf("판매") to listOf("구매할 수 있게", "비판매로 변경")
                } else {
                    listOf("비판매") to listOf("구매할 수 없게", "판매로 변경")
                }

            showCustomDialog(
                title = dialogTitle,
                content = dialogContent,
                onConfirmClicked = onSaleClicked,
                titleKeywords = titleKeywords,
                contentKeywords = contentKeywords,
            )
        }

        null -> {}
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun DetailScreenPreview() {
    SemonemoTheme {
        DetailScreen()
    }
}
