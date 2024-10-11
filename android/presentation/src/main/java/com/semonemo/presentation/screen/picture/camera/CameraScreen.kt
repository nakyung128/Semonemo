package com.semonemo.presentation.screen.picture.camera

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.MediaPlayer
import androidx.activity.compose.BackHandler
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.semonemo.presentation.R
import com.semonemo.presentation.component.TopAppBar
import com.semonemo.presentation.screen.frame.FrameType
import com.semonemo.presentation.screen.picture.PictureUiEvent
import com.semonemo.presentation.screen.picture.camera.subscreen.CameraPreviewWithPermission
import com.semonemo.presentation.screen.picture.camera.subscreen.CircularCountdownTimer
import com.semonemo.presentation.theme.Gray01
import com.semonemo.presentation.theme.Gray02
import com.semonemo.presentation.theme.Gray03
import com.semonemo.presentation.theme.GunMetal
import com.semonemo.presentation.theme.SemonemoTheme
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.theme.White
import com.semonemo.presentation.util.noRippleClickable
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun CameraRoute(
    modifier: Modifier,
    frameIdx: Int,
    popUpBackStack: () -> Unit,
    onShowSnackBar: (String) -> Unit,
    viewModel: CameraViewModel = hiltViewModel(),
    navigateToSelect: (Int) -> Unit,
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val frameType = FrameType.fromIdx(frameIdx) ?: FrameType.OneByOne
    CameraContent(
        modifier = modifier,
        popUpBackStack = {
            viewModel.resetUiState()
            popUpBackStack()
        },
        onShowSnackBar = onShowSnackBar,
        onTakePhoto = viewModel::takePhoto,
        navigateToSelect = navigateToSelect,
        uiEvent = viewModel.uiEvent,
        bitmaps = uiState.value.bitmaps,
        idx = frameType.idx,
    )
    BackHandler(onBack = {
        viewModel.resetUiState()
        popUpBackStack()
    })
}

@Composable
fun CameraContent(
    modifier: Modifier,
    popUpBackStack: () -> Unit,
    onShowSnackBar: (String) -> Unit,
    onTakePhoto: (Bitmap, Int) -> Unit = { _, _ -> },
    uiEvent: SharedFlow<PictureUiEvent>,
    bitmaps: List<Bitmap> = listOf(),
    navigateToSelect: (Int) -> Unit = {},
    idx: Int,
) {
    LaunchedEffect(uiEvent) {
        uiEvent.collectLatest { event ->
            when (event) {
                is PictureUiEvent.Error -> onShowSnackBar(event.errorMessage)
                PictureUiEvent.NavigateToSelect -> navigateToSelect(idx)
                else -> {}
            }
        }
    }
    val context = LocalContext.current
    val controller =
        remember {
            LifecycleCameraController(context).apply {
                setEnabledUseCases(CameraController.IMAGE_CAPTURE)
            }
        }

    CameraScreen(
        modifier = modifier,
        popUpBackStack = popUpBackStack,
        onShowSnackBar = onShowSnackBar,
        onTakePhoto = onTakePhoto,
        controller = controller,
        context = context,
        frameType = FrameType.fromIdx(idx),
        bitmaps = bitmaps,
    )
}

@Composable
fun CameraScreen(
    modifier: Modifier = Modifier,
    popUpBackStack: () -> Unit = {},
    onShowSnackBar: (String) -> Unit = {},
    onTakePhoto: (Bitmap, Int) -> Unit = { _, _ -> },
    controller: LifecycleCameraController,
    context: Context = LocalContext.current,
    frameType: FrameType = FrameType.OneByOne,
    bitmaps: List<Bitmap> = listOf(),
) {
    val takePicturePlayer =
        remember {
            MediaPlayer.create(context, R.raw.sound_take_picture)
        }
    val timerTickSoundPlayer =
        remember {
            MediaPlayer.create(context, R.raw.clock_time)
        }

    var selectedIndex by remember { mutableStateOf(0) }
    val (cnt, setCount) = remember { mutableStateOf(frameType.amount) }
    val options = listOf(3, 10)

    val timerTime =
        remember {
            mutableLongStateOf(options[selectedIndex] * 1000L)
        }
    LaunchedEffect(timerTime) {
        timerTime.value = options[selectedIndex] * 1000L
    }

    DisposableEffect(Unit) {
        onDispose {
            controller.unbind()
            takePicturePlayer.release()
            timerTickSoundPlayer.release()
        }
    }

    Surface(
        modifier =
            modifier
                .fillMaxSize(),
        color = GunMetal,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .background(GunMetal),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            TopAppBar(modifier = Modifier, onNavigationClick = popUpBackStack, iconColor = White)
            Spacer(modifier = Modifier.height(5.dp))
            Box(
                modifier =
                    if (frameType != FrameType.OneByFour) {
                        Modifier
                            .fillMaxWidth()
                            .aspectRatio(3f / 4f)
                    } else {
                        Modifier
                            .fillMaxWidth()
                            .aspectRatio(6.5f / 4f)
                    },
                contentAlignment = Alignment.Center,
            ) {
                CameraPreviewWithPermission(
                    modifier =
                        Modifier
                            .fillMaxSize(),
                    controller = controller,
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .align(Alignment.Start)
                        .padding(start = 10.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    SingleChoiceSegmentedButtonRow {
                        options.forEachIndexed { index, label ->
                            SegmentedButton(
                                modifier = Modifier,
                                shape =
                                    SegmentedButtonDefaults.itemShape(
                                        index = index,
                                        count = options.size,
                                    ),
                                onClick = {
                                    selectedIndex = index
                                    timerTime.value = options[selectedIndex] * 1000L
                                },
                                selected = index == selectedIndex,
                                colors =
                                    SegmentedButtonDefaults.colors(
                                        inactiveBorderColor = Gray02,
                                        inactiveContainerColor = Gray02,
                                        activeContainerColor = White,
                                        activeBorderColor = White,
                                    ),
                                icon = {
                                    if (index == selectedIndex) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_auction_clock),
                                            contentDescription = null,
                                            modifier = Modifier.size(20.dp),
                                        )
                                    }
                                },
                            ) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("${label}s", style = Typography.bodyLarge)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "${bitmaps.size} / ${frameType.amount}",
                        style = Typography.bodyLarge.copy(color = White),
                    )
                    Spacer(modifier = Modifier.weight(0.2f))
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier.wrapContentSize(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    modifier = Modifier.noRippleClickable { popUpBackStack() },
                    text = stringResource(id = R.string.mypage_cancel_tag),
                    style = Typography.bodyMedium.copy(color = White),
                )
                Spacer(modifier = Modifier.weight(1f))
                CircularCountdownTimer(
                    totalTime = timerTime.longValue,
                    countdownColor = White,
                    backgroundColor = Gray01,
                    modifier = Modifier,
                    onTimerEnd = {
                        takePhoto(
                            context = context,
                            controller = controller,
                            onPhotoTaken = onTakePhoto,
                            onErrorSnackBar = onShowSnackBar,
                            amount = cnt,
                            mediaPlayer = takePicturePlayer,
                        )
                        setCount(cnt - 1)
                    },
                    timerTickSound = timerTickSoundPlayer,
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    modifier =
                        Modifier
                            .size(40.dp)
                            .background(shape = CircleShape, color = Gray03)
                            .padding(7.dp)
                            .clickable {
                                controller.cameraSelector =
                                    if (controller.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                                        CameraSelector.DEFAULT_FRONT_CAMERA
                                    } else {
                                        CameraSelector.DEFAULT_BACK_CAMERA
                                    }
                            },
                    painter = painterResource(id = R.drawable.ic_coin_exchange),
                    contentDescription = "switch camera",
                )
                Spacer(modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

private fun takePhoto(
    controller: LifecycleCameraController,
    amount: Int,
    onPhotoTaken: (Bitmap, Int) -> Unit,
    context: Context,
    onErrorSnackBar: (String) -> Unit,
    mediaPlayer: MediaPlayer,
) {
    if (!mediaPlayer.isPlaying) {
        mediaPlayer.start()
    }
    controller.takePicture(
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                super.onCaptureSuccess(image)
                val matrix =
                    Matrix().apply {
                        postRotate(image.imageInfo.rotationDegrees.toFloat())
                    }
                if (controller.cameraSelector == CameraSelector.DEFAULT_FRONT_CAMERA) {
                    matrix.postScale(-1f, 1f)
                }
                val rotatedBitmap =
                    Bitmap.createBitmap(
                        image.toBitmap(),
                        0,
                        0,
                        image.width,
                        image.height,
                        matrix,
                        true,
                    )

                onPhotoTaken(rotatedBitmap, amount)
            }

            override fun onError(exception: ImageCaptureException) {
                super.onError(exception)
                onErrorSnackBar(exception.message.toString())
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
fun CameraScreenPreview() {
    SemonemoTheme {
        val mockController = LifecycleCameraController(LocalContext.current)
        CameraScreen(
            controller = mockController,
        )
    }
}
