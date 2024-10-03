package com.semonemo.presentation.screen.picture

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.semonemo.presentation.R
import com.semonemo.presentation.component.BoldTextWithKeywords
import com.semonemo.presentation.component.FrameSizeBox
import com.semonemo.presentation.screen.frame.FrameType
import com.semonemo.presentation.theme.Main01
import com.semonemo.presentation.theme.SemonemoTheme
import com.semonemo.presentation.theme.Typography

@Composable
fun PictureMainRoute(
    modifier: Modifier = Modifier,
    navigateToCamera: (Int) -> Unit,
) {
    PictureMainScreen(
        modifier = modifier,
        navigateToCamera = navigateToCamera,
    )
}

@Composable
fun PictureMainScreen(
    modifier: Modifier = Modifier,
    navigateToCamera: (Int) -> Unit = { },
) {
    val scrollState = rememberScrollState()

    Box(
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
                    .navigationBarsPadding()
                    .verticalScroll(state = scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(35.dp))
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp),
                contentAlignment = Alignment.CenterStart,
            ) {
                BoldTextWithKeywords(
                    fullText = stringResource(R.string.picture_main_title),
                    keywords = listOf("사이즈", "사진을 촬영"),
                    brushFlag = listOf(true, true),
                    boldStyle = Typography.bodyLarge.copy(fontSize = 24.sp),
                    normalStyle = Typography.labelLarge.copy(fontSize = 24.sp),
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Box(modifier = Modifier.weight(1f)) {
                Column(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(bottom = 165.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    FrameSizeBox(
                        modifier =
                            Modifier
                                .weight(1f)
                                .padding(horizontal = 15.dp, vertical = 7.dp),
                        title = stringResource(R.string.frame_size1_title),
                        script = stringResource(R.string.frame_size1_script),
                        frameImg = R.drawable.img_frame_size_one_by_four,
                        onClick = { navigateToCamera(FrameType.OneByFour.idx) },
                    )
                    FrameSizeBox(
                        modifier =
                            Modifier
                                .weight(1f)
                                .padding(horizontal = 15.dp, vertical = 7.dp),
                        title = stringResource(R.string.frame_size2_title),
                        script = stringResource(R.string.frame_size2_script),
                        frameImg = R.drawable.img_frame_size_two_by_two,
                        onClick = { navigateToCamera(FrameType.TwoByTwo.idx) },
                    )
                    FrameSizeBox(
                        modifier =
                            Modifier
                                .weight(1f)
                                .padding(horizontal = 15.dp, vertical = 7.dp),
                        title = stringResource(R.string.frame_size3_title),
                        script = stringResource(R.string.frame_size3_script),
                        frameImg = R.drawable.img_frame_size_one_by_one,
                        onClick = { navigateToCamera(FrameType.OneByOne.idx) },
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PictureMainScreenPreview() {
    SemonemoTheme {
        PictureMainScreen()
    }
}
