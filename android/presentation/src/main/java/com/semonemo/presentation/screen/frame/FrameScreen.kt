package com.semonemo.presentation.screen.frame

import android.graphics.Bitmap
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.semonemo.domain.model.Asset
import com.semonemo.presentation.R
import com.semonemo.presentation.component.ColorPalette
import com.semonemo.presentation.component.CustomTab
import com.semonemo.presentation.component.ImageLoadingProgress
import com.semonemo.presentation.component.LongBlackButton
import com.semonemo.presentation.component.TopAppBar
import com.semonemo.presentation.screen.aiAsset.draw.AssetButtonList
import com.semonemo.presentation.theme.FrameBlue
import com.semonemo.presentation.theme.FrameGreen
import com.semonemo.presentation.theme.FrameOrange
import com.semonemo.presentation.theme.FramePink
import com.semonemo.presentation.theme.FramePurple
import com.semonemo.presentation.theme.Gray01
import com.semonemo.presentation.theme.GunMetal
import com.semonemo.presentation.theme.SemonemoTheme
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.theme.WhiteGray
import com.semonemo.presentation.util.noRippleClickable
import com.skydoves.landscapist.glide.GlideImage
import dev.shreyaspatil.capturable.capturable
import dev.shreyaspatil.capturable.controller.CaptureController
import dev.shreyaspatil.capturable.controller.rememberCaptureController
import kotlinx.coroutines.launch
import java.util.UUID

data class OverlayAsset(
    val uuid: UUID = UUID.randomUUID(),
    var imageUrl: String,
)

data class AssetPosition(
    var offsetX: Float = 0f,
    var offsetY: Float = 0f,
    var rotation: Float = 0f,
    var scale: Float = 0.5f,
)

@Composable
fun FrameRoute(
    modifier: Modifier = Modifier,
    navigateToFrameDone: () -> Unit,
    viewModel: FrameViewModel = hiltViewModel(),
    onErrorSnackBar: (String) -> Unit,
    popBackStack: () -> Unit,
) {
    LaunchedEffect(Unit) {
        viewModel.loadMyAssets()
    }
    FrameScreen(
        modifier = modifier,
        navigateToFrameDone = navigateToFrameDone,
        updateFrame = viewModel::updateFrame,
        onErrorSnackBar = onErrorSnackBar,
        assets = viewModel.assets.value,
        popBackStack = popBackStack,
    )
}

@OptIn(ExperimentalComposeApi::class)
@Composable
fun FrameScreen(
    modifier: Modifier = Modifier,
    navigateToFrameDone: () -> Unit = {},
    popBackStack: () -> Unit = {},
    updateFrame: (Bitmap, FrameType) -> Unit = { _, _ -> },
    onErrorSnackBar: (String) -> Unit = {},
    assets: List<Asset> = listOf(),
) {
    BackHandler {
        popBackStack()
    }
    val captureController = rememberCaptureController()
    val scope = rememberCoroutineScope()
    val tabs =
        listOf(
            stringResource(R.string.frame_size),
            stringResource(R.string.frame_background),
            stringResource(R.string.frame_asset),
        )
    val sizes =
        listOf(
            stringResource(R.string.frame_one_by_one),
            stringResource(R.string.frame_one_by_four),
            stringResource(R.string.frame_two_by_two),
        )
    val frameColors =
        listOf(
            Color.Black,
            Color.White,
            FramePink,
            FrameOrange,
            FrameGreen,
            FrameBlue,
            FramePurple,
        )
    val deepColors =
        listOf(
            Color.Red,
            Color.Green,
            Color.Cyan,
            Color.Blue,
            Color.Magenta,
            Gray01,
        )

    var frameType by remember { mutableStateOf(FrameType.OneByOne) }
    var selectedIndex by remember { mutableIntStateOf(0) }
    var selectedBtn by remember { mutableStateOf("1x1") }
    var selectedColor by remember { mutableStateOf<Color?>(Color.Black) }
    val overlayAssets = remember { mutableStateListOf<OverlayAsset>() }

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
                    .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.fillMaxHeight(0.015f))
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.frame_description),
                        style = Typography.labelMedium.copy(fontSize = 16.sp),
                        color = GunMetal,
                    )
                },
                onNavigationClick = popBackStack,
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.02f))
            FramePreview(
                frameType = frameType,
                captureController = captureController,
                overlayAssets = overlayAssets,
                backgroundColor = selectedColor,
                onDeleteAsset = { asset ->
                    overlayAssets.remove(asset)
                },
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
            CustomTab(
                modifier = Modifier.fillMaxWidth(),
                tabList = tabs,
                selectedIndex = selectedIndex,
                onTabSelected = {
                    selectedIndex = it
                },
            )
            when (selectedIndex) {
                0 -> {
                    Box(
                        modifier =
                            Modifier
                                .wrapContentSize()
                                .padding(top = 25.dp),
                        contentAlignment = Alignment.TopCenter,
                    ) {
                        AssetButtonList(
                            titles = sizes,
                            selectedBtn = selectedBtn,
                            onBtnSelected = {
                                selectedBtn = it
                                when (it) {
                                    "1x1" -> frameType = FrameType.OneByOne
                                    "1x4" -> frameType = FrameType.OneByFour
                                    "2x2" -> frameType = FrameType.TwoByTwo
                                }
                            },
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }

                1 -> {
                    Column(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(horizontal = 24.dp),
                        horizontalAlignment = Alignment.Start,
                    ) {
                        Spacer(modifier = Modifier.fillMaxHeight(0.07f))
                        ColorPalette(
                            colors = frameColors,
                            circleSize = 35,
                            selectedColor = selectedColor,
                            onColorSelected = {
                                selectedColor = it
                            },
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                        ColorPalette(
                            colors = deepColors,
                            circleSize = 35,
                            selectedColor = selectedColor,
                            onColorSelected = {
                                selectedColor = it
                            },
                        )
                        Spacer(modifier = Modifier.fillMaxHeight(0.13f))
                    }
                }

                2 -> {
                    LazyVerticalGrid(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(horizontal = 20.dp, vertical = 20.dp),
                        columns = GridCells.Fixed(4),
                        state = rememberLazyGridState(),
                    ) {
                        items(assets.size) { index ->
                            GlideImage(
                                imageModel = assets[index].imageUrl,
                                contentScale = ContentScale.Crop,
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(1f)
                                        .padding(8.dp)
                                        .clip(shape = RoundedCornerShape(10.dp))
                                        .background(color = WhiteGray)
                                        .clickable { overlayAssets.add(OverlayAsset(imageUrl = assets[index].imageUrl)) },
                                loading = {
                                    ImageLoadingProgress(
                                        modifier = Modifier,
                                    )
                                },
                                failure = {
                                    Image(
                                        painter =
                                            painterResource(
                                                id =
                                                    R.drawable.ic_place_holder,
                                            ),
                                        contentDescription = null,
                                    )
                                },
                            )
                        }
                    }
                }
            }
            LongBlackButton(
                icon = null,
                text = stringResource(R.string.frame_done_btn_title),
                onClick = {
                    scope.launch {
                        val bitmapAsync = captureController.captureAsync()
                        try {
                            val bitmap = bitmapAsync.await().asAndroidBitmap()
                            updateFrame(bitmap, frameType)
                        } catch (error: Throwable) {
                            onErrorSnackBar(error.message ?: "")
                        }
                    }
                    navigateToFrameDone()
                },
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FramePreview(
    modifier: Modifier = Modifier,
    captureController: CaptureController,
    overlayAssets: SnapshotStateList<OverlayAsset>,
    frameType: FrameType,
    backgroundColor: Color? = null,
    backgroundBrush: Brush? = null,
    onDeleteAsset: (OverlayAsset) -> Unit,
) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }
    var parentSize by remember { mutableStateOf(IntSize.Zero) } // 부모 박스 크기
    var contentSize by remember { mutableStateOf(IntSize.Zero) } // 프레임 크기

    val transformableState =
        rememberTransformableState { zoomChange, offsetChange, _ ->
            scale = (scale * zoomChange).coerceIn(1f, 3f)
            val newOffsetX = offsetX + offsetChange.x
            val newOffsetY = offsetY + offsetChange.y

            // 부모 박스와 프레임의 크기를 고려하여 이동 제한
            val maxOffsetX =
                if (contentSize.width * scale > parentSize.width) {
                    (contentSize.width * scale - parentSize.width) / 2
                } else {
                    0f // 부모 박스가 더 크면 이동할 필요가 없으므로 0으로 제한
                }

            val maxOffsetY =
                if (contentSize.height * scale > parentSize.height) {
                    (contentSize.height * scale - parentSize.height) / 2
                } else {
                    0f // 부모 박스가 더 크면 이동할 필요가 없으므로 0으로 제한
                }

            offsetX = newOffsetX.coerceIn(-maxOffsetX, maxOffsetX)
            offsetY = newOffsetY.coerceIn(-maxOffsetY, maxOffsetY)
        }

    val backgroundModifier =
        when {
            backgroundBrush != null -> Modifier.background(brush = backgroundBrush)
            backgroundColor != null -> Modifier.background(color = backgroundColor)
            else -> Modifier
        }

    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .clipToBounds()
                .onSizeChanged { parentSize = it } // 부모 박스의 크기를 저장
                .transformable(
                    state = transformableState,
                ).graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offsetX,
                    translationY = offsetY,
                ),
        contentAlignment = Alignment.Center,
    ) {
        when (frameType) {
            FrameType.OneByOne -> {
                // 1x1 그리드 레이아웃
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth(0.6f)
                            .wrapContentHeight()
                            .capturable(captureController),
                    contentAlignment = Alignment.Center,
                ) {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(345.dp)
                                .onSizeChanged { contentSize = it },
                    ) {
                        Column(
                            modifier =
                                Modifier
                                    .fillMaxWidth(),
                        ) {
                            Box(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .height(5.dp)
                                        .then(backgroundModifier),
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
                                            .height(280.dp)
                                            .then(backgroundModifier),
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Box(
                                    modifier =
                                        Modifier
                                            .width(5.dp)
                                            .height(280.dp)
                                            .then(backgroundModifier),
                                )
                            }
                            Box(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .height(60.dp)
                                        .then(backgroundModifier),
                            )
                        }
                    }
                    Box(modifier = Modifier.height(345.dp)) {
                        if (overlayAssets.isNotEmpty()) {
                            ShowAssets(
                                overlayAssets = overlayAssets,
                                onDeleteAsset = onDeleteAsset,
                            )
                        }
                    }
                }
            }

            FrameType.TwoByTwo -> {
                // 2x2 그리드 레이아웃
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth(0.6f)
                            .wrapContentHeight()
                            .capturable(captureController),
                    contentAlignment = Alignment.Center,
                ) {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(344.dp)
                                .onSizeChanged { contentSize = it },
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Box(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .height(5.dp)
                                        .then(backgroundModifier),
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
                                            .height(137.dp)
                                            .then(backgroundModifier),
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Box(
                                    modifier =
                                        Modifier
                                            .width(5.dp)
                                            .height(137.dp)
                                            .then(backgroundModifier),
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Box(
                                    modifier =
                                        Modifier
                                            .width(5.dp)
                                            .height(137.dp)
                                            .then(backgroundModifier),
                                )
                            }
                            Box(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .height(5.dp)
                                        .then(backgroundModifier),
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
                                            .height(137.dp)
                                            .then(backgroundModifier),
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Box(
                                    modifier =
                                        Modifier
                                            .width(5.dp)
                                            .height(137.dp)
                                            .then(backgroundModifier),
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Box(
                                    modifier =
                                        Modifier
                                            .width(5.dp)
                                            .height(137.dp)
                                            .then(backgroundModifier),
                                )
                            }
                            Box(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .height(60.dp)
                                        .then(backgroundModifier),
                            )
                        }
                    }
                    Box(modifier = Modifier.height(344.dp)) {
                        if (overlayAssets.isNotEmpty()) {
                            ShowAssets(
                                overlayAssets = overlayAssets,
                                onDeleteAsset = onDeleteAsset,
                            )
                        }
                    }
                }
            }

            FrameType.OneByFour -> {
                // 1x4 그리드 레이아웃
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth(0.3f)
                            .wrapContentHeight()
                            .capturable(captureController),
                    contentAlignment = Alignment.Center,
                ) {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(365.dp)
                                .onSizeChanged { contentSize = it },
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Box(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .height(5.dp)
                                        .then(backgroundModifier),
                            )
                            repeat(4) {
                                Row(modifier = Modifier.fillMaxWidth()) {
                                    Box(
                                        modifier =
                                            Modifier
                                                .width(5.dp)
                                                .height(75.dp)
                                                .then(backgroundModifier),
                                    )
                                    Spacer(modifier = Modifier.weight(1f))
                                    Box(
                                        modifier =
                                            Modifier
                                                .width(5.dp)
                                                .height(75.dp)
                                                .then(backgroundModifier),
                                    )
                                }
                                Box(
                                    modifier =
                                        Modifier
                                            .fillMaxWidth()
                                            .height(5.dp)
                                            .then(backgroundModifier),
                                )
                            }
                            Box(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .height(40.dp)
                                        .then(backgroundModifier),
                            )
                        }
                    }
                    Box(modifier = Modifier.height(365.dp)) {
                        if (overlayAssets.isNotEmpty()) {
                            ShowAssets(
                                overlayAssets = overlayAssets,
                                onDeleteAsset = onDeleteAsset,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ShowAssets(
    overlayAssets: SnapshotStateList<OverlayAsset>,
    onDeleteAsset: (OverlayAsset) -> Unit,
) {
    var selectedAssetId by remember { mutableStateOf<UUID?>(null) }
    val assetPositions = remember { mutableStateMapOf<UUID, AssetPosition>() }

    overlayAssets.forEach { asset ->
        var position by remember(asset.uuid) { mutableStateOf(assetPositions.getOrPut(asset.uuid) { AssetPosition() }) }

        val imageTransformableState = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
            position = position.copy(
                scale = (position.scale * zoomChange).coerceIn(0.1f, 5f),
                rotation = position.rotation + rotationChange,
                offsetX = position.offsetX + offsetChange.x * position.scale,
                offsetY = position.offsetY + offsetChange.y * position.scale
            )
            assetPositions[asset.uuid] = position
        }

        val isSelected = selectedAssetId == asset.uuid

        val assetModifier =
            Modifier
                .graphicsLayer(
                    scaleX = position.scale,
                    scaleY = position.scale,
                    translationX = position.offsetX,
                    translationY = position.offsetY,
                    rotationZ = position.rotation,
                ).transformable(state = imageTransformableState)
                .noRippleClickable {
                    selectedAssetId = if (selectedAssetId == asset.uuid) null else asset.uuid
                }

        // 선택된 에셋에만 border와 버튼 표시
        if (isSelected) {
            Box(
                modifier =
                    assetModifier
                        .wrapContentSize()
                        .aspectRatio(1f)
                        .border(width = 2.dp, color = GunMetal),
            ) {
                GlideImage(
                    modifier =
                        Modifier
                            .wrapContentSize()
                            .aspectRatio(1f),
                    imageModel = asset.imageUrl,
                    contentScale = ContentScale.Fit,
                    loading = {
                        ImageLoadingProgress(modifier = Modifier)
                    },
                )
                // 삭제 버튼 (오른쪽 상단)
                Box(
                    modifier =
                        Modifier
                            .size(40.dp)
                            .align(Alignment.TopEnd)
                            .background(WhiteGray, shape = CircleShape)
                            .noRippleClickable {
                                onDeleteAsset(asset)
                                selectedAssetId = null
                                assetPositions.remove(asset.uuid)
                            },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Delete Asset",
                        tint = GunMetal,
                    )
                }
            }
        } else {
            GlideImage(
                modifier =
                    assetModifier
                        .wrapContentSize()
                        .aspectRatio(1f),
                imageModel = asset.imageUrl,
                contentScale = ContentScale.Fit,
                loading = {
                    ImageLoadingProgress(modifier = Modifier)
                },
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun FrameScreenPreview() {
    SemonemoTheme {
        FrameScreen()
    }
}
