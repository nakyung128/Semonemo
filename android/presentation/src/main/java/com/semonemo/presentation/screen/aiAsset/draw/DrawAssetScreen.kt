package com.semonemo.presentation.screen.aiAsset.draw

import android.graphics.Bitmap
import android.net.Uri
import android.util.Base64
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.semonemo.presentation.R
import com.semonemo.presentation.component.AssetButton
import com.semonemo.presentation.component.ColorPalette
import com.semonemo.presentation.component.CustomTextField
import com.semonemo.presentation.component.LoadingDialog
import com.semonemo.presentation.component.LongBlackButton
import com.semonemo.presentation.component.PenPalette
import com.semonemo.presentation.component.TopAppBar
import com.semonemo.presentation.theme.Gray02
import com.semonemo.presentation.theme.GunMetal
import com.semonemo.presentation.theme.Orange
import com.semonemo.presentation.theme.SemonemoTheme
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.theme.White
import dev.shreyaspatil.capturable.capturable
import dev.shreyaspatil.capturable.controller.rememberCaptureController
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

// 펜 스타일 데이터 클래스
data class PathStyle(
    var color: Color = Color.Black,
    var width: Float = 4.dp.value,
)

// PathStyle 넘겨 주면 매핑해 주는 함수 선언
internal fun DrawScope.drawPath(
    path: Path,
    style: PathStyle,
) {
    drawPath(
        path = path,
        color = style.color,
        style = Stroke(width = style.width),
    )
}

@Composable
fun DrawAssetRoute(
    modifier: Modifier,
    navigateToDone: (String) -> Unit,
    viewModel: DrawAssetViewModel = hiltViewModel(),
    popUpToBackStack: () -> Unit,
    onErrorSnackBar: (String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DrawAssetContent(
        modifier = modifier,
        navigateToDone = navigateToDone,
        popUpBackStack = popUpToBackStack,
        onErrorSnackBar = onErrorSnackBar,
        uiEvent = viewModel.uiEvent,
        isLoading = uiState.isLoading,
        makeDrawAsset = viewModel::makeDrawAsset,
        updateStyle = viewModel::updateStyle,
    )
}

@Composable
fun DrawAssetContent(
    modifier: Modifier,
    navigateToDone: (String) -> Unit,
    popUpBackStack: () -> Unit,
    onErrorSnackBar: (String) -> Unit,
    uiEvent: SharedFlow<DrawAssetUiEvent>,
    isLoading: Boolean,
    makeDrawAsset: (String, String) -> Unit,
    updateStyle: (String, String) -> Unit,
) {
    LaunchedEffect(uiEvent) {
        uiEvent.collectLatest { event ->
            when (event) {
                is DrawAssetUiEvent.Error -> onErrorSnackBar(event.errorMessage)
                is DrawAssetUiEvent.NavigateTo -> navigateToDone(event.imageUrl)
            }
        }
    }

    DrawAssetScreen(
        modifier = modifier,
        popUpBackStack = popUpBackStack,
        onErrorSnackBar = { onErrorSnackBar(it) },
        makeDrawAsset = makeDrawAsset,
        updateStyle = updateStyle,
        navigateToDone = navigateToDone,
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

@OptIn(ExperimentalComposeUiApi::class, ExperimentalComposeApi::class)
@Composable
fun DrawAssetScreen(
    modifier: Modifier = Modifier,
    popUpBackStack: () -> Unit = {},
    onErrorSnackBar: (String) -> Unit = {},
    makeDrawAsset: (String, String) -> Unit = { _, _ -> },
    updateStyle: (String, String) -> Unit = { _, _ -> },
    navigateToDone: (String) -> Unit = {},
) {
    val captureController = rememberCaptureController()
    val scope = rememberCoroutineScope()

    // 색상 팔레트
    val colors =
        listOf(
            Color.Black,
            Color.Red,
            Orange,
            Color.Yellow,
            Color.Green,
            Color.Blue,
            Color.Magenta,
        )
    var selectedColor by remember { mutableStateOf(Color.Black) }

    // 펜 굵기 팔레트
    val sizes =
        listOf(
            4.dp,
            7.dp,
            10.dp,
            13.dp,
        )
    var selectedSize by remember { mutableStateOf(4.dp) }

    val titles =
        listOf(
            "없음",
            "실사",
            "카툰",
            "애니메이션",
        )

    var styles =
        listOf(
            "사람",
            "동물",
        )

    val genders =
        listOf("남자", "여자")

    var selectedType by remember { mutableIntStateOf(0) }
    var selectedWhat by remember { mutableIntStateOf(0) }
    var selectedGender by remember { mutableIntStateOf(0) }
    var animal by remember { mutableStateOf("") }

    LaunchedEffect(selectedType) {
        selectedWhat = 0
    }

    LaunchedEffect(selectedType, selectedWhat) {
        updateStyle(titles[selectedType], styles[selectedWhat])
    }

    var point by remember { mutableStateOf(Offset.Zero) } // point 위치 추척 state
    val points = remember { mutableListOf<Offset>() } // 새로 그려지는 path 표시하기 위한 points State

    var path by remember { mutableStateOf(Path()) }
    val paths = remember { mutableStateListOf<Pair<Path, PathStyle>>() } // 다 그려진 획 리스트
    val removedPaths = remember { mutableStateListOf<Pair<Path, PathStyle>>() } // undo, redo 위한 리스트
    val pathStyle by remember { mutableStateOf(PathStyle()) }

    val state = rememberScrollState()

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(color = White)
                .statusBarsPadding()
                .navigationBarsPadding()
                .verticalScroll(state),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        TopAppBar(
            modifier = Modifier.fillMaxWidth(),
            title = {
                Text(
                    text = stringResource(R.string.canvas_script),
                    style = Typography.labelMedium,
                    color = Gray02,
                )
            },
            onNavigationClick = popUpBackStack,
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(18.dp))
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .background(color = White)
                        .padding(horizontal = 17.dp)
                        .border(width = 1.dp, color = Gray02, shape = RoundedCornerShape(15.dp))
                        .capturable(
                            controller = captureController,
                        ),
            ) {
                Canvas(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .pointerInput(Unit) {
                                detectDragGestures(
                                    onDragStart = { offset ->
                                        point = offset
                                        points.add(point)
                                    },
                                    onDrag = { _, dragAmount ->
                                        val newPoint = point + dragAmount // 새로운 포인트 위치 계산
                                        val canvasBounds = size // 범위 내인지 확인

                                        if (newPoint.x in 0f..canvasBounds.width.toFloat() &&
                                            newPoint.y in 0f..canvasBounds.height.toFloat()
                                        ) {
                                            point = newPoint
                                            points.add(point)

                                            path = Path()

                                            points.forEachIndexed { index, point ->
                                                if (index == 0) {
                                                    path.moveTo(point.x, point.y)
                                                } else {
                                                    path.lineTo(point.x, point.y)
                                                }
                                            }
                                        }
                                    },
                                    onDragEnd = {
                                        paths.add(Pair(path, pathStyle.copy()))
                                        points.clear()
                                        path = Path()
                                    },
                                )
                            },
                ) {
//                    drawRect(color = White, size = size)

                    // 이미 완성된 획들
                    paths.forEach { pair ->
                        drawPath(
                            path = pair.first,
                            style = pair.second,
                        )
                    }

                    // 현재 그려지고 있는 획
                    drawPath(
                        path = path,
                        style = pathStyle,
                    )
                }
            }
            Spacer(modifier = Modifier.height(5.dp))
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 17.dp),
                contentAlignment = Alignment.CenterEnd,
            ) {
                DrawNavigateBar(
                    onUndoClicked = {
                        if (paths.isNotEmpty()) {
                            val lastPath = paths.removeLast()
                            removedPaths.add(lastPath)
                        }
                    },
                    onRedoClicked = {
                        if (removedPaths.isNotEmpty()) {
                            val lastRemovedPath = removedPaths.removeLast()
                            paths.add(lastRemovedPath)
                        }
                    },
                    onClearClicked = {
                        paths.clear()
                        removedPaths.clear()
                    },
                )
            }
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 17.dp),
                contentAlignment = Alignment.CenterStart,
            ) {
                Text(
                    text = "색상",
                    style = Typography.bodySmall.copy(color = GunMetal, fontSize = 15.sp),
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(35.dp)
                        .padding(horizontal = 17.dp),
                contentAlignment = Alignment.CenterStart,
            ) {
                ColorPalette(
                    colors = colors,
                    selectedColor = selectedColor,
                    onColorSelected = {
                        selectedColor = it
                        pathStyle.color = it
                    },
                )
            }
            Spacer(modifier = Modifier.height(25.dp))
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 17.dp),
                contentAlignment = Alignment.CenterStart,
            ) {
                Text(
                    text = "펜 굵기",
                    style = Typography.bodySmall.copy(color = GunMetal, fontSize = 15.sp),
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(35.dp)
                        .padding(horizontal = 17.dp),
                contentAlignment = Alignment.CenterStart,
            ) {
                PenPalette(
                    sizes = sizes,
                    selectedSize = selectedSize,
                    onSizeSelected = {
                        selectedSize = it
                        pathStyle.width = it.value
                    },
                )
            }
            Spacer(modifier = Modifier.height(25.dp))
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 17.dp),
                contentAlignment = Alignment.CenterStart,
            ) {
                Text(
                    text = "화풍",
                    style = Typography.bodySmall.copy(color = GunMetal, fontSize = 15.sp),
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 17.dp),
                contentAlignment = Alignment.CenterStart,
            ) {
                AssetButtonList(
                    titles = titles,
                    selectedBtn = titles[selectedType],
                    onBtnSelected = {
                        selectedType = it
                    },
                )
            }
            if (selectedType != 0) {
                Spacer(modifier = Modifier.height(25.dp))
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 17.dp),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    Text(
                        text = "종류",
                        style = Typography.bodySmall.copy(color = GunMetal, fontSize = 15.sp),
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 17.dp),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    AssetButtonList(
                        titles = styles,
                        selectedBtn = styles[selectedWhat],
                        onBtnSelected = {
                            selectedWhat = it
                        },
                    )
                }
                Spacer(modifier = Modifier.height(25.dp))
                if (styles[selectedWhat] == "사람") {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 17.dp),
                        contentAlignment = Alignment.CenterStart,
                    ) {
                        Text(
                            text = "성별",
                            style = Typography.bodySmall.copy(color = GunMetal, fontSize = 15.sp),
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 17.dp),
                        contentAlignment = Alignment.CenterStart,
                    ) {
                        AssetButtonList(
                            titles = genders,
                            selectedBtn = genders[selectedGender],
                            onBtnSelected = {
                                selectedGender = it
                            },
                        )
                    }
                }
                if (styles[selectedWhat] == "동물") {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 17.dp),
                        contentAlignment = Alignment.CenterStart,
                    ) {
                        Text(
                            text = "어떤 동물인지 영어로 작성해 주세요",
                            style = Typography.bodySmall.copy(color = GunMetal, fontSize = 15.sp),
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 17.dp),
                        contentAlignment = Alignment.CenterStart,
                    ) {
                        CustomTextField(
                            placeholder = "ex) cat",
                            input = animal,
                            onValueChange = {
                                animal = it
                            },
                            onClearPressed = {
                                animal = ""
                            },
                            focusManager = LocalFocusManager.current,
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(30.dp))
            LongBlackButton(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp),
                icon = null,
                text = stringResource(R.string.draw_done),
                onClick = {
                    scope.launch {
                        try {
                            val bitmapAsync = captureController.captureAsync()
                            val bitmap = bitmapAsync.await().asAndroidBitmap()
                            val base64 = bitmapToBase64(bitmap)

                            if (selectedType == 0) {
                                val uri = "data:image/png;base64,$base64"
                                navigateToDone(Uri.encode(uri))
                            } else {
                                if (styles[selectedWhat] == "사람") {
                                    makeDrawAsset(base64, genders[selectedGender])
                                } else {
                                    makeDrawAsset(base64, animal)
                                }
                            }
                        } catch (error: Throwable) {
                            onErrorSnackBar(error.message ?: "")
                        }
                    }
                },
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

fun bitmapToBase64(bitmap: Bitmap): String {
    val byteArrayOutputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
    val byteArray = byteArrayOutputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}

// 뒤로가기, 앞으로가기, 초기화하기
@Composable
fun DrawNavigateBar(
    onUndoClicked: () -> Unit,
    onRedoClicked: () -> Unit,
    onClearClicked: () -> Unit,
) {
    Row(
        modifier = Modifier.wrapContentWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.img_undo),
            contentDescription = null,
            modifier =
                Modifier
                    .size(30.dp)
                    .clickable { onUndoClicked() },
            tint = Color.Unspecified,
        )
        Icon(
            painter = painterResource(id = R.drawable.img_redo),
            contentDescription = null,
            modifier =
                Modifier
                    .size(30.dp)
                    .clickable { onRedoClicked() },
            tint = Color.Unspecified,
        )
        Icon(
            painter = painterResource(id = R.drawable.img_reset),
            contentDescription = null,
            modifier =
                Modifier
                    .size(30.dp)
                    .clickable { onClearClicked() },
            tint = Color.Unspecified,
        )
    }
}

// 화풍 버튼 리스트
@Composable
fun AssetButtonList(
    titles: List<String>,
    selectedBtn: String,
    onBtnSelected: (Int) -> Unit,
) {
    Row(
        modifier = Modifier.wrapContentSize(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        titles.forEachIndexed { index, title ->
            AssetButton(
                onClick = { onBtnSelected(index) },
                isSelected = title == selectedBtn,
                title = title,
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DrawAssetPreview() {
    SemonemoTheme {
        DrawAssetScreen()
    }
}
