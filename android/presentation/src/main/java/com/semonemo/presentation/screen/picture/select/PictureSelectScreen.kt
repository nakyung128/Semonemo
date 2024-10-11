package com.semonemo.presentation.screen.picture.select

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.semonemo.domain.model.myFrame.MyFrame
import com.semonemo.presentation.R
import com.semonemo.presentation.component.ColorPalette
import com.semonemo.presentation.component.LoadingDialog
import com.semonemo.presentation.component.TopAppBar
import com.semonemo.presentation.screen.frame.FrameType
import com.semonemo.presentation.screen.picture.PictureUiEvent
import com.semonemo.presentation.screen.picture.PictureUiState
import com.semonemo.presentation.screen.picture.camera.CameraViewModel
import com.semonemo.presentation.screen.picture.select.subscreen.CreatePictureButton
import com.semonemo.presentation.theme.Gray02
import com.semonemo.presentation.theme.GunMetal
import com.semonemo.presentation.theme.Main01
import com.semonemo.presentation.theme.SemonemoTheme
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.theme.frameBackGroundColor
import com.semonemo.presentation.util.getTodayDate
import com.semonemo.presentation.util.noRippleClickable
import com.semonemo.presentation.util.saveBitmapToGallery
import com.semonemo.presentation.util.urlToIpfs
import com.skydoves.landscapist.glide.GlideImage
import dev.shreyaspatil.capturable.capturable
import dev.shreyaspatil.capturable.controller.rememberCaptureController
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun PictureSelectRoute(
    modifier: Modifier,
    popUpBackStack: () -> Unit,
    viewModel: CameraViewModel = hiltViewModel(),
    onShowSnackBar: (String) -> Unit = {},
    actionWithSnackBar: (Uri) -> Unit = {},
    type: Int,
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    PictureSelectContent(
        modifier = modifier,
        popUpBackStack = popUpBackStack,
        uiState = uiState.value,
        uiEvent = viewModel.uiEvent,
        onShowSnackBar = onShowSnackBar,
        loadMyFrame = viewModel::loadAvailableFrames,
        type = type,
        actionWithSnackBar = actionWithSnackBar,
    )

    DisposableEffect(Unit) {
        onDispose {
            viewModel.resetUiState()
        }
    }
}

@Composable
fun PictureSelectContent(
    modifier: Modifier,
    popUpBackStack: () -> Unit,
    uiState: PictureUiState,
    uiEvent: SharedFlow<PictureUiEvent>,
    onShowSnackBar: (String) -> Unit,
    loadMyFrame: (Int) -> Unit,
    type: Int,
    actionWithSnackBar: (Uri) -> Unit = {},
) {
    LaunchedEffect(uiEvent) {
        uiEvent.collectLatest { event ->
            when (event) {
                is PictureUiEvent.Error -> onShowSnackBar(event.errorMessage)
                else -> {}
            }
        }
    }
    PictureSelectScreen(
        modifier = modifier,
        popUpBackStack = popUpBackStack,
        pictures = uiState.bitmaps,
        frames = uiState.frames,
        type = FrameType.fromIdx(type),
        onShowSnackBar = onShowSnackBar,
        actionWithSnackBar = actionWithSnackBar,
    )
    if (uiState.isLoading) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .clickable(enabled = false) {},
        )
        LoadingDialog(
            lottieRes = R.raw.normal_load,
            loadingMessage = stringResource(R.string.frame_loading_title),
            subMessage = stringResource(R.string.loading_sub_message),
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalComposeApi::class)
@Composable
fun PictureSelectScreen(
    modifier: Modifier = Modifier,
    popUpBackStack: () -> Unit = {},
    pictures: List<Bitmap> = listOf(),
    frames: List<MyFrame> = listOf(),
    type: FrameType = FrameType.OneByOne,
    onShowSnackBar: (String) -> Unit = {},
    actionWithSnackBar: (Uri) -> Unit = {},
) {
    val captureController = rememberCaptureController()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var check =
        remember {
            mutableStateOf(false)
        }
    var selectedFrameIndex =
        remember {
            mutableIntStateOf(-1)
        }
    val selectedPictures = remember { mutableStateOf(mutableListOf<Bitmap>()) }
    var selectedColor by remember { mutableStateOf(Color.Black) }
    Surface(
        modifier =
            modifier
                .fillMaxSize()
                .background(brush = Main01),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            TopAppBar(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                onNavigationClick = popUpBackStack,
                actionButtons = {
                    val isEnabled = selectedPictures.value.size == type.amount
                    CreatePictureButton(
                        modifier = Modifier,
                        isEnabled = isEnabled,
                        onClick = {
                            if (isEnabled) {
                                scope.launch {
                                    val bitmapAsync = captureController.captureAsync()
                                    try {
                                        val frame = frames[selectedFrameIndex.intValue]
                                        val bitmap = bitmapAsync.await().asAndroidBitmap()
                                        saveBitmapToGallery(
                                            context = context,
                                            bitmap = bitmap,
                                            nickname = frame.owner.nickname,
                                            frameTitle = frame.nftInfo.data.title,
                                            onSuccess = {
                                                actionWithSnackBar(it)
                                                popUpBackStack()
                                            },
                                        )
                                    } catch (error: Throwable) {
                                        onShowSnackBar(error.message ?: "")
                                    }
                                }
                            } else {
                                onShowSnackBar("사진을 선택해 주세요!")
                            }
                        },
                    )
                },
            )
            Spacer(modifier = Modifier.height(10.dp))
            if (selectedFrameIndex.intValue == -1) {
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(350.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "프레임을 선택해 주세요! 📸",
                        style = Typography.bodyMedium.copy(fontSize = 15.sp),
                        color = Gray02,
                    )
                }
                selectedPictures.value.clear()
            } else {
                when (type) {
                    FrameType.OneByOne -> {
                        Box(
                            modifier =
                                Modifier
                                    .fillMaxWidth(0.6f)
                                    .height(345.dp)
                                    .capturable(captureController),
                            contentAlignment = Alignment.BottomEnd,
                        ) {
                            Column(
                                modifier =
                                    Modifier
                                        .fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Box(
                                    modifier =
                                        Modifier
                                            .fillMaxWidth()
                                            .height(5.dp),
                                )
                                Row(
                                    modifier =
                                        Modifier
                                            .fillMaxWidth()
                                            .wrapContentHeight(),
                                ) {
                                    Box(
                                        modifier =
                                            Modifier
                                                .width(5.dp)
                                                .height(280.dp),
                                    )
                                    Box(
                                        modifier =
                                            Modifier
                                                .fillMaxWidth()
                                                .height(280.dp),
                                    ) {
                                        selectedPictures.value.forEach { bitmap ->
                                            Image(
                                                bitmap = bitmap.asImageBitmap(),
                                                contentDescription = null,
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier.fillMaxSize(),
                                            )
                                        }
                                    }
                                    Box(
                                        modifier =
                                            Modifier
                                                .width(5.dp)
                                                .height(280.dp),
                                    )
                                }
                                Box(
                                    modifier =
                                        Modifier
                                            .fillMaxWidth()
                                            .height(60.dp),
                                )
                            }
                            GlideImage(
                                imageModel =
                                    frames[selectedFrameIndex.intValue]
                                        .nftInfo.data.image
                                        .urlToIpfs(),
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .height(345.dp),
                            )
                            if (check.value) {
                                Text(
                                    modifier = Modifier.padding(bottom = 5.dp, end = 5.dp),
                                    text = getTodayDate(),
                                    style =
                                    Typography.labelSmall.copy(
                                        fontSize = 11.sp,
                                        color = selectedColor,
                                    ),
                                )
                            }
                        }
                    }

                    FrameType.TwoByTwo -> {
                        Box(
                            modifier =
                                Modifier
                                    .fillMaxWidth(0.6f)
                                    .height(344.dp)
                                    .capturable(captureController),
                            contentAlignment = Alignment.BottomEnd,
                        ) {
                            Column(modifier = Modifier.fillMaxSize()) {
                                Box(
                                    modifier =
                                        Modifier
                                            .fillMaxWidth()
                                            .height(5.dp),
                                )
                                Row(
                                    modifier =
                                        Modifier
                                            .fillMaxWidth()
                                            .wrapContentHeight(),
                                ) {
                                    Box(
                                        modifier =
                                            Modifier
                                                .width(5.dp)
                                                .height(137.dp),
                                    )
                                    Box(
                                        modifier =
                                            Modifier
                                                .weight(1f)
                                                .height(137.dp),
                                    ) {
                                        if (selectedPictures.value.size >= 1) {
                                            Image(
                                                bitmap = selectedPictures.value[0].asImageBitmap(),
                                                contentDescription = null,
                                                contentScale = ContentScale.Crop,
                                                modifier =
                                                    Modifier
                                                        .fillMaxSize(),
                                            )
                                        }
                                    }
                                    Box(
                                        modifier =
                                            Modifier
                                                .width(5.dp)
                                                .height(137.dp),
                                    )
                                    Box(
                                        modifier =
                                            Modifier
                                                .weight(1f)
                                                .height(137.dp),
                                    ) {
                                        if (selectedPictures.value.size >= 2) {
                                            Image(
                                                bitmap = selectedPictures.value[1].asImageBitmap(),
                                                contentDescription = null,
                                                contentScale = ContentScale.Crop,
                                                modifier =
                                                    Modifier
                                                        .fillMaxSize()
                                                        .align(Alignment.Center),
                                            )
                                        }
                                    }
                                    Box(
                                        modifier =
                                            Modifier
                                                .width(5.dp)
                                                .height(137.dp),
                                    )
                                }
                                Box(
                                    modifier =
                                        Modifier
                                            .fillMaxWidth()
                                            .height(5.dp),
                                )
                                Row(
                                    modifier =
                                        Modifier
                                            .fillMaxWidth()
                                            .wrapContentHeight(),
                                ) {
                                    Box(
                                        modifier =
                                            Modifier
                                                .width(5.dp)
                                                .height(137.dp),
                                    )
                                    Box(
                                        modifier =
                                            Modifier
                                                .weight(1f)
                                                .height(137.dp),
                                    ) {
                                        if (selectedPictures.value.size >= 3) {
                                            Image(
                                                bitmap = selectedPictures.value[2].asImageBitmap(),
                                                contentDescription = null,
                                                contentScale = ContentScale.Crop,
                                                modifier =
                                                    Modifier
                                                        .fillMaxSize()
                                                        .align(Alignment.Center),
                                            )
                                        }
                                    }
                                    Box(
                                        modifier =
                                            Modifier
                                                .width(5.dp)
                                                .height(137.dp),
                                    )
                                    Box(
                                        modifier =
                                            Modifier
                                                .weight(1f)
                                                .height(137.dp),
                                    ) {
                                        if (selectedPictures.value.size >= 4) {
                                            Image(
                                                bitmap = selectedPictures.value[3].asImageBitmap(),
                                                contentDescription = null,
                                                contentScale = ContentScale.Crop,
                                                modifier =
                                                    Modifier
                                                        .fillMaxSize()
                                                        .align(Alignment.Center),
                                            )
                                        }
                                    }
                                    Box(
                                        modifier =
                                            Modifier
                                                .width(5.dp)
                                                .height(137.dp),
                                    )
                                }
                                Box(
                                    modifier =
                                        Modifier
                                            .fillMaxWidth()
                                            .height(60.dp),
                                )
                            }
                            GlideImage(
                                imageModel =
                                    frames[selectedFrameIndex.intValue]
                                        .nftInfo.data.image
                                        .urlToIpfs(),
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .height(344.dp),
                            )
                            if (check.value) {
                                Text(
                                    modifier = Modifier.padding(bottom = 5.dp, end = 5.dp),
                                    text = getTodayDate(),
                                    style =
                                    Typography.labelSmall.copy(
                                        fontSize = 11.sp,
                                        color = selectedColor,
                                    ),
                                )
                            }
                        }
                    }

                    FrameType.OneByFour -> {
                        Box(
                            modifier =
                                Modifier
                                    .fillMaxWidth(0.3f)
                                    .height(365.dp)
                                    .capturable(captureController),
                            contentAlignment = Alignment.BottomEnd,
                        ) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Box(
                                    modifier =
                                        Modifier
                                            .fillMaxWidth()
                                            .height(5.dp),
                                )
                                repeat(4) { index ->
                                    Row(modifier = Modifier.fillMaxWidth()) {
                                        Box(
                                            modifier =
                                                Modifier
                                                    .width(5.dp)
                                                    .height(75.dp),
                                        )
                                        Box(
                                            modifier =
                                                Modifier
                                                    .weight(1f)
                                                    .height(75.dp),
                                        ) {
                                            RenderImage(
                                                modifier =
                                                    Modifier
                                                        .fillMaxSize()
                                                        .align(Alignment.Center),
                                                index = index,
                                                selectedPictures = selectedPictures.value,
                                            )
                                        }
                                        Box(
                                            modifier =
                                                Modifier
                                                    .width(5.dp)
                                                    .height(75.dp),
                                        )
                                    }
                                    Box(
                                        modifier =
                                            Modifier
                                                .fillMaxWidth()
                                                .height(5.dp),
                                    )
                                }
                                Box(
                                    modifier =
                                        Modifier
                                            .fillMaxWidth()
                                            .height(40.dp),
                                    contentAlignment = Alignment.BottomCenter,
                                ) {
                                    if (check.value) {
                                        Text(
                                            text = getTodayDate(),
                                            style =
                                                Typography.labelSmall.copy(
                                                    fontSize = 12.sp,
                                                    color = selectedColor,
                                                ),
                                        )
                                    }
                                }
                            }
                            GlideImage(
                                imageModel =
                                    frames[selectedFrameIndex.intValue]
                                        .nftInfo.data.image
                                        .urlToIpfs(),
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .height(365.dp),
                            )
                            if (check.value) {
                                Text(
                                    modifier = Modifier.padding(bottom = 5.dp, end = 5.dp),
                                    text = getTodayDate(),
                                    style =
                                    Typography.labelSmall.copy(
                                        fontSize = 11.sp,
                                        color = selectedColor,
                                    ),
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            LazyRow(
                modifier =
                    modifier
                        .align(Alignment.Start)
                        .padding(start = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                content = {
                    items(count = pictures.size) { index ->
                        val isSelected = selectedPictures.value.contains(pictures[index])
                        Image(
                            modifier =
                                Modifier
                                    .clip(RoundedCornerShape((10.dp)))
                                    .width(70.dp)
                                    .height(70.dp)
                                    .graphicsLayer(alpha = if (isSelected) 0.5f else 1f)
                                    .noRippleClickable {
                                        selectedPictures.value =
                                            selectedPictures.value
                                                .toMutableList()
                                                .apply {
                                                    if (isSelected) {
                                                        remove(pictures[index])
                                                    } else {
                                                        add(pictures[index])
                                                    }
                                                }
                                    },
                            bitmap = pictures[index].asImageBitmap(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                        )
                    }
                },
            )
            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = GunMetal,
            )
            Spacer(modifier = Modifier.height(15.dp))
            Row(
                modifier =
                    Modifier
                        .align(Alignment.Start)
                        .fillMaxWidth()
                        .padding(start = 15.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Checkbox(
                    modifier = Modifier.size(10.dp),
                    checked = check.value,
                    onCheckedChange = { check.value = !check.value },
                    colors =
                        CheckboxDefaults.colors(
                            checkedColor = GunMetal,
                            uncheckedColor = GunMetal,
                            checkmarkColor = Color.White,
                        ),
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = stringResource(R.string.picture_date), style = Typography.labelMedium)
                Spacer(modifier = Modifier.width(20.dp))
                ColorPalette(
                    colors = frameBackGroundColor,
                    circleSize = 25,
                    selectedColor = selectedColor,
                    onColorSelected = {
                        selectedColor = it
                    },
                )
            }
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 12.dp)
                        .align(Alignment.Start),
                text = stringResource(R.string.my_frame),
                style = Typography.bodySmall,
            )
            Spacer(modifier = Modifier.height(20.dp))
            LazyRow(
                modifier =
                    modifier
                        .align(Alignment.Start)
                        .fillMaxHeight(1f)
                        .padding(start = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                content = {
                    items(frames.size) { index ->
                        val frame = frames[index]
                        val imgUrl =
                            frame.nftInfo.data.image
                                .urlToIpfs()
                        GlideImage(
                            imageModel = imgUrl.toUri(),
                            contentScale = ContentScale.Fit,
                            modifier =
                                Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .height(200.dp)
                                    .aspectRatio(1f)
                                    .noRippleClickable {
                                        if (selectedFrameIndex.value == index) {
                                            selectedFrameIndex.value = -1
                                        } else {
                                            selectedFrameIndex.value = index
                                        }
                                    },
                        )
                    }
                },
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PictureSelectScreenPreview() {
    SemonemoTheme {
        PictureSelectScreen()
    }
}
