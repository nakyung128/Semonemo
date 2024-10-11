package com.semonemo.presentation.screen.imgAsset

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.semonemo.presentation.R
import com.semonemo.presentation.component.BoldTextWithKeywords
import com.semonemo.presentation.component.LongWhiteButton
import com.semonemo.presentation.theme.Main01
import com.semonemo.presentation.theme.Main02
import com.semonemo.presentation.theme.SemonemoTheme
import com.semonemo.presentation.theme.Typography

@Composable
fun ImageAssetScreen(
    modifier: Modifier = Modifier,
    popUpBackStack: () -> Unit = {},
    navigateToSelect: (String) -> Unit = {},
) {
    val photoFromAlbumLauncher =
        rememberLauncherForActivityResult(
            contract =
                ActivityResultContracts.PickVisualMedia(),
            onResult = { uri ->
                uri?.let {
                    val encodeUri = Uri.encode(it.toString())
                    navigateToSelect(encodeUri)
                }
            },
        )

    Box(
        modifier =
            modifier
                .fillMaxSize()
                .background(brush = Main01),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .navigationBarsPadding()
                    .statusBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.fillMaxHeight(0.15f))
            Text(
                text = stringResource(R.string.img_asset_title1),
                style = Typography.labelLarge.copy(fontSize = 24.sp),
            )
            Spacer(modifier = Modifier.height(7.dp))
            Text(
                text = stringResource(R.string.img_asset_title2),
                style = Typography.titleLarge.copy(brush = Main02, fontSize = 40.sp),
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.1f))
            Row {
                Image(
                    painter = painterResource(id = R.drawable.img_framed_picture),
                    contentDescription = null,
                )
                Image(
                    painter = painterResource(id = R.drawable.img_clapping_hands),
                    contentDescription = null,
                )
            }
            Spacer(modifier = Modifier.fillMaxHeight(0.18f))
            Text(
                text = stringResource(R.string.img_asset_script1),
                style = Typography.labelLarge.copy(fontSize = 20.sp),
            )
            Spacer(modifier = Modifier.height(4.dp))
            BoldTextWithKeywords(
                fullText = stringResource(id = R.string.img_asset_script2),
                keywords = listOf("배경을 제거"),
                brushFlag = listOf(false),
                boldStyle = Typography.titleLarge.copy(fontSize = 20.sp),
                normalStyle = Typography.labelLarge.copy(fontSize = 20.sp),
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.3f))
            LongWhiteButton(
                icon = null,
                text = stringResource(R.string.img_asset_btn_title),
                onClick = {
                    photoFromAlbumLauncher.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly,
                        ),
                    )
                },
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun ImageAssetPreview() {
    SemonemoTheme {
        ImageAssetScreen()
    }
}
