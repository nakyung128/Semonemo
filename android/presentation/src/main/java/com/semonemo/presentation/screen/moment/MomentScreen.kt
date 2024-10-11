package com.semonemo.presentation.screen.moment

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.semonemo.presentation.R
import com.semonemo.presentation.component.BoldTextWithKeywords
import com.semonemo.presentation.component.MomentBox
import com.semonemo.presentation.component.MomentLongBox
import com.semonemo.presentation.theme.Main01
import com.semonemo.presentation.theme.SemonemoTheme
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.theme.White
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MomentRoute(
    modifier: Modifier = Modifier,
    viewModel: MomentViewModel = hiltViewModel(),
    navigateToAiAsset: () -> Unit = {},
    navigateToImageAsset: () -> Unit = {},
    navigateToFrame: () -> Unit = {},
    navigateToPicture: () -> Unit = {},
    onShowSnackBar: (String) -> Unit = {},
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    MomentScreen(
        modifier = modifier,
        navigateToAiAsset = navigateToAiAsset,
        navigateToImageAsset = navigateToImageAsset,
        navigateToFrame = navigateToFrame,
        navigateToPicture = navigateToPicture,
        uiState = uiState.value,
        viewModel = viewModel,
        onShowSnackBar = onShowSnackBar,
    )
}

@Composable
fun MomentScreen(
    modifier: Modifier = Modifier,
    navigateToAiAsset: () -> Unit = {},
    navigateToImageAsset: () -> Unit = {},
    navigateToFrame: () -> Unit = {},
    navigateToPicture: () -> Unit = {},
    uiState: MomentUiState = MomentUiState(),
    viewModel: MomentViewModel = hiltViewModel(),
    onShowSnackBar: (String) -> Unit = {},
) {
    val context = LocalContext.current
    val activity = context as? Activity
    // 스낵바 표시 여부를 관찰
    val isShowBackPressSnackBar by viewModel.isShowBackPressSnackBar.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }
    // finishEvent를 수집하여 앱 종료 처리
    LaunchedEffect(Unit) {
        viewModel.finishEvent.collectLatest {
            activity?.finishAffinity()
        }
    }

    // BackHandler에서 ViewModel로 이벤트 전달
    BackHandler {
        viewModel.onBackPressed()
    }
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
    ) { _ ->
        Box(
            modifier =
                modifier
                    .fillMaxSize()
                    .background(brush = Main01),
        ) {
            Column(
                modifier =
                    Modifier
                        .wrapContentHeight()
                        .statusBarsPadding()
                        .navigationBarsPadding()
                        .padding(horizontal = 17.dp, vertical = 20.dp),
            ) {
                Column(
                    modifier =
                        Modifier
                            .wrapContentSize()
                            .padding(start = 7.dp),
                ) {
                    BoldTextWithKeywords(
                        fullText = "${uiState.nickname} 님,\n추억을 나만의 프레임에 담아 보세요",
                        keywords = arrayListOf(uiState.nickname, "나만의 프레임"),
                        brushFlag = arrayListOf(true, true),
                        boldStyle = Typography.titleSmall.copy(fontSize = 24.sp),
                        normalStyle = Typography.labelLarge.copy(fontSize = 24.sp),
                    )
                }
                Spacer(modifier = Modifier.fillMaxHeight(0.04f))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    MomentBox(
                        modifier = Modifier.weight(1f),
                        title = stringResource(R.string.ai_asset_label),
                        subTitle = stringResource(R.string.make_title),
                        icon = R.drawable.img_robot,
                        onClick = navigateToAiAsset,
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    MomentBox(
                        modifier = Modifier.weight(1f),
                        title = stringResource(R.string.img_asset_label),
                        subTitle = stringResource(R.string.make_title),
                        icon = R.drawable.img_disk,
                        onClick = navigateToImageAsset,
                    )
                }
                Spacer(modifier = Modifier.height(18.dp))
                MomentLongBox(
                    modifier = Modifier.fillMaxWidth(),
                    title = stringResource(R.string.my_frame_label),
                    subTitle = stringResource(R.string.frame_label_2),
                    icon = R.drawable.img_sparkles,
                    onClick = navigateToFrame,
                )
                Spacer(modifier = Modifier.height(18.dp))
                MomentLongBox(
                    modifier = Modifier.fillMaxWidth(),
                    title = stringResource(R.string.picture_label),
                    subTitle = stringResource(R.string.picture_label2),
                    icon = R.drawable.img_camera,
                    onClick = navigateToPicture,
                )
            }

            // 스낵바 표시
            AnimatedVisibility(
                modifier = Modifier.align(Alignment.BottomCenter),
                visible = isShowBackPressSnackBar,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                Snackbar(
                    modifier = modifier.padding(start = 17.dp, bottom = 120.dp, end = 17.dp),
                ) {
                    Text(
                        text = "뒤로가기를 한번 더 누르면 종료됩니다.",
                        color = White,
                        fontSize = 20.sp,
                        style = Typography.bodyMedium,
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun MomentScreenPreview() {
    SemonemoTheme {
        MomentScreen()
    }
}
