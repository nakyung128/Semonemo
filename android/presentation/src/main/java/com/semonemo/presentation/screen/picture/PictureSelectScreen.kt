package com.semonemo.presentation.screen.picture

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.semonemo.presentation.R
import com.semonemo.presentation.component.TopAppBar
import com.semonemo.presentation.screen.camera.CameraViewModel
import com.semonemo.presentation.theme.GunMetal
import com.semonemo.presentation.theme.Main01
import com.semonemo.presentation.theme.SemonemoTheme
import com.semonemo.presentation.theme.Typography

@Composable
fun PictureSelectRoute(
    modifier: Modifier,
    popUpBackStack: () -> Unit,
    viewModel: CameraViewModel = hiltViewModel(),
    onShowSnackBar: (String) -> Unit = {},
) {
    PictureSelectContent(
        modifier = modifier,
        popUpBackStack = popUpBackStack,
    )
}

@Composable
fun PictureSelectContent(
    modifier: Modifier,
    popUpBackStack: () -> Unit,
) {
    PictureSelectScreen(
        modifier = modifier,
        popUpBackStack = popUpBackStack,
    )
}

@Composable
fun PictureSelectScreen(
    modifier: Modifier = Modifier,
    popUpBackStack: () -> Unit = {},
) {
    var check =
        remember {
            mutableStateOf(false)
        }
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
            Spacer(modifier = Modifier.height(20.dp))
            TopAppBar(modifier = Modifier, onNavigationClick = popUpBackStack)
            Spacer(modifier = Modifier.height(10.dp))
            Image(
                modifier =
                    Modifier
                        .fillMaxWidth(0.8f)
                        .fillMaxHeight(0.4f),
                painter = painterResource(id = R.drawable.img_frame_size_one_by_four),
                contentDescription = null,
            )
            Spacer(modifier = Modifier.height(20.dp))
            LazyRow(
                modifier =
                    modifier
                        .align(Alignment.Start)
                        .padding(start = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                content = {
                    items(count = 4) { index ->
                        Image(
                            modifier =
                                Modifier
                                    .clip(RoundedCornerShape((10.dp)))
                                    .width(70.dp)
                                    .height(70.dp),
                            painter = painterResource(id = R.drawable.img_example),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                        )
                    }
                },
            )
            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 2.dp,
                color = GunMetal,
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.align(Alignment.Start),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Checkbox(checked = check.value, onCheckedChange = { check.value = !check.value })
                Text(text = stringResource(R.string.picture_date), style = Typography.bodySmall)
            }

            Text(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp)
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
                    items(count = 4) { index ->
                        Image(
                            modifier =
                                Modifier
                                    .clip(RoundedCornerShape((10.dp)))
                                    .height(200.dp)
                                    .aspectRatio(1f),
                            painter = painterResource(id = R.drawable.img_example),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
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
