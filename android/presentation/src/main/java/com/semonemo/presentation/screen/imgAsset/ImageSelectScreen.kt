package com.semonemo.presentation.screen.imgAsset

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.semonemo.presentation.R
import com.semonemo.presentation.component.ImageLoadingProgress
import com.semonemo.presentation.component.LoadingDialog
import com.semonemo.presentation.component.LongBlackButton
import com.semonemo.presentation.component.LongWhiteButton
import com.semonemo.presentation.screen.aiAsset.draw.AssetButtonList
import com.semonemo.presentation.theme.SemonemoTheme
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.theme.White
import com.semonemo.presentation.util.encodeImageToBase64FromUri
import com.semonemo.presentation.util.toAbsolutePath
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ImageSelectRoute(
    modifier: Modifier,
    popUpBackStack: () -> Unit,
    navigateToDone: (String) -> Unit,
    viewModel: ImageSelectViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ImageSelectContent(
        modifier = modifier,
        popUpBackStack = popUpBackStack,
        uiEvent = viewModel.uiEvent,
        navigateToDone = { navigateToDone(it) },
        makeAiAsset = { viewModel.makeAiAsset(it) },
        imageUri = uiState.imageUrl,
        updateImage = { viewModel.updateImageUri(it) },
        isLoading = uiState.isLoading,
        updateStyle = { viewModel.updateStyle(it) },
        paintingStyle = uiState.style.title,
    )
}

@Composable
fun ImageSelectContent(
    modifier: Modifier = Modifier,
    popUpBackStack: () -> Unit = {},
    navigateToDone: (String) -> Unit = {},
    makeAiAsset: (String) -> Unit = {},
    imageUri: Uri? = null,
    updateImage: (Uri) -> Unit = {},
    uiEvent: SharedFlow<ImageSelectUiEvent>,
    onShowErrorSnackBar: (String) -> Unit = {},
    isLoading: Boolean = false,
    updateStyle: (String) -> Unit = {},
    paintingStyle: String = "",
) {
    LaunchedEffect(uiEvent) {
        uiEvent.collectLatest { event ->
            when (event) {
                is ImageSelectUiEvent.Error -> onShowErrorSnackBar(event.errorMessage)
                is ImageSelectUiEvent.NavigateTo -> navigateToDone(event.imageUrl)
            }
        }
    }
    Box(modifier = modifier) {
        ImageSelectScreen(
            modifier = Modifier,
            navigateToDone = navigateToDone,
            makeAiAsset = makeAiAsset,
            imageUri = imageUri,
            updateImage = updateImage,
            paintingStyle = paintingStyle,
            updateStyle = updateStyle,
        )

        if (isLoading) {
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .clickable(enabled = false) {},
            )
            LoadingDialog(
                lottieRes = R.raw.normal_load,
                loadingMessage = stringResource(R.string.loading_message),
                subMessage = stringResource(R.string.loading_sub_message),
            )
        }
    }
}

@Composable
fun ImageSelectScreen(
    modifier: Modifier = Modifier,
    navigateToDone: (String) -> Unit = {},
    makeAiAsset: (String) -> Unit = {},
    imageUri: Uri? = null,
    updateImage: (Uri) -> Unit = {},
    paintingStyle: String = "",
    updateStyle: (String) -> Unit = {},
) {
    val titles =
        listOf(
            "없음",
            "실사",
            "카툰",
            "애니메이션",
        )
    val context = LocalContext.current
    val photoFromAlbumLauncher =
        rememberLauncherForActivityResult(
            contract =
                ActivityResultContracts.PickVisualMedia(),
            onResult = { uri ->
                uri?.let {
                    updateImage(it)
                    // selectedImg = it
                }
            },
        )

    Surface(
        modifier =
            modifier
                .fillMaxSize()
                .background(color = White),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(color = White)
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 100.dp)
                    .statusBarsPadding()
                    .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
            GlideImage(
                imageModel = imageUri,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.5f),
                contentScale = ContentScale.Fit,
                loading = {
                    ImageLoadingProgress(
                        modifier = Modifier,
                    )
                },
            )
            Spacer(modifier = Modifier.weight(0.4f))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.img_select_title),
                style = Typography.labelLarge.copy(fontSize = 15.sp),
                textAlign = TextAlign.Center,
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.img_select_style),
                style = Typography.labelLarge.copy(fontSize = 15.sp),
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(20.dp))
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                AssetButtonList(
                    titles = titles,
                    selectedBtn = paintingStyle,
                    onBtnSelected = { updateStyle(titles[it]) },
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            LongBlackButton(
                modifier = Modifier.fillMaxWidth(),
                icon = null,
                text = stringResource(R.string.img_select_confirm),
                onClick = {
                    if (paintingStyle == "없음") {
                        imageUri?.let {
                            navigateToDone(Uri.encode(it.toAbsolutePath(context)))
                        }
                    } else {
                        encodeImageToBase64FromUri(context = context, uri = imageUri)?.let {
                            makeAiAsset(it)
                        }
                    }
                    // AI 배경 제거 작업 후 완료 화면으로 이동
                    // S3 url 전달
                },
            )
            Spacer(modifier = Modifier.height(12.dp))
            LongWhiteButton(
                modifier = Modifier.fillMaxWidth(),
                icon = null,
                text = stringResource(R.string.img_select_again),
                onClick = {
                    photoFromAlbumLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
                    )
                },
            )
        }
    }
}

@Composable
@Preview(showSystemUi = true)
fun ImageSelectScreenPreview() {
    SemonemoTheme {
        ImageSelectScreen()
    }
}
