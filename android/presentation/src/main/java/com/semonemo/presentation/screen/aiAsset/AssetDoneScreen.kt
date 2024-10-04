package com.semonemo.presentation.screen.aiAsset

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.semonemo.presentation.R
import com.semonemo.presentation.component.BoldTextWithKeywords
import com.semonemo.presentation.component.LoadingDialog
import com.semonemo.presentation.component.LongBlackButton
import com.semonemo.presentation.component.LongWhiteButton
import com.semonemo.presentation.theme.Gray02
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.util.addFocusCleaner
import com.semonemo.presentation.util.saveBase64ParseImageToFile
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import java.io.File

@Composable
fun AssetDoneRoute(
    modifier: Modifier = Modifier,
    popUpBackStack: () -> Unit = {},
    navigateToMy: (Long) -> Unit = {},
    viewModel: AssetViewModel = hiltViewModel(),
    onErrorSnackBar: (String) -> Unit = {},
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    AssetDoneContent(
        modifier = modifier,
        uiState = uiState.value,
        popUpBackStack = popUpBackStack,
        navigateToMy = navigateToMy,
        onErrorSnackBar = onErrorSnackBar,
        uiEvent = viewModel.uiEvent,
        removeBackGround = viewModel::removeBackground,
        uploadAsset = viewModel::uploadAsset,
    )
}

@Composable
fun AssetDoneContent(
    modifier: Modifier = Modifier,
    popUpBackStack: () -> Unit = {},
    navigateToMy: (Long) -> Unit = {},
    uiState: AssetDoneUiState,
    onErrorSnackBar: (String) -> Unit = {},
    uiEvent: SharedFlow<AssetDoneUiEvent>,
    removeBackGround: () -> Unit = {},
    uploadAsset: (File?) -> Unit = {},
) {
    LaunchedEffect(uiEvent) {
        uiEvent.collectLatest { event ->
            when (event) {
                is AssetDoneUiEvent.Error -> onErrorSnackBar(event.errorMessage)
                AssetDoneUiEvent.Done -> {
                    navigateToMy(-1)
                }
            }
        }
    }
    AssetDoneScreen(
        modifier = modifier,
        assetUrl = uiState.assetUrl,
        popUpBackStack = popUpBackStack,
        removeBackGround = removeBackGround,
        uploadAsset = uploadAsset,
    )

    if (uiState.isLoading) {
        LoadingDialog(
            loadingMessage = "AI가 열심히 배경을 제거하고 있어요...",
            subMessage = "조금만 기다려 주세요  (。＾▽＾)",
        )
    }
}

@Composable
fun AssetDoneScreen(
    modifier: Modifier = Modifier,
    assetUrl: String? = null,
    popUpBackStack: () -> Unit = {},
    removeBackGround: () -> Unit = {},
    uploadAsset: (File?) -> Unit = {},
) {
    val context = LocalContext.current

    val focusManager = LocalFocusManager.current
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .background(color = Color.White)
                .addFocusCleaner(
                    focusManager = focusManager,
                ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.fillMaxHeight(0.05f))
        Column(
            modifier = modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier.wrapContentSize(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                BoldTextWithKeywords(
                    fullText = stringResource(R.string.asset_done_title),
                    keywords = arrayListOf("완료"),
                    brushFlag = arrayListOf(true),
                    boldStyle = Typography.titleMedium.copy(fontSize = 22.sp),
                    normalStyle = Typography.labelLarge.copy(fontSize = 22.sp),
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    painter = painterResource(id = R.drawable.img_firecracker),
                    contentDescription = null,
                    tint = Color.Unspecified,
                )
            }
            BoldTextWithKeywords(
                fullText = stringResource(R.string.asset_done_title2),
                keywords = arrayListOf("보관함", "다시 제작"),
                brushFlag = arrayListOf(true, true),
                boldStyle = Typography.titleMedium.copy(fontSize = 22.sp),
                normalStyle = Typography.labelLarge.copy(fontSize = 22.sp),
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.03f))
            Text(
                text = stringResource(R.string.asset_done_script),
                color = Gray02,
                style = Typography.labelLarge.copy(fontSize = 16.sp),
            )
            Spacer(modifier = Modifier.height(20.dp))
            GlideImage(
                imageModel = assetUrl,
                modifier =
                    Modifier
                        .fillMaxWidth(0.6f)
                        .fillMaxHeight(0.5f),
                contentScale = ContentScale.Crop,
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.45f))
            Row(modifier = Modifier.fillMaxWidth(0.88f)) {
                LongBlackButton(
                    modifier = Modifier.weight(1f),
                    icon = null,
                    text = "배경 제거할래요",
                    onClick = removeBackGround,
                )
                Spacer(modifier = Modifier.weight(0.08f))
                LongBlackButton(
                    modifier = Modifier.weight(1f),
                    icon = null,
                    text = stringResource(R.string.save_asset),
                    onClick = {
                        assetUrl?.let {
                            val uri = saveBase64ParseImageToFile(context, it)
                            uri?.let {
                                uploadAsset(File(uri.path))
                            }
                        }
                    },
                )
            }
            Spacer(modifier = Modifier.height(13.dp))
            Spacer(modifier = Modifier.height(13.dp))
            LongWhiteButton(
                icon = null,
                text = stringResource(R.string.remake_asset),
                onClick = {
                    popUpBackStack()
                },
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AssetDoneScreenPreview() {
    AssetDoneScreen()
}
