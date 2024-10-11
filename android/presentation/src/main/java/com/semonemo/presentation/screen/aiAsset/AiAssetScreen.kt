package com.semonemo.presentation.screen.aiAsset

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
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
fun AiAssetScreen(
    modifier: Modifier = Modifier,
    navigateToDraw: () -> Unit = {},
    navigateToPrompt: () -> Unit = {},
) {
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
                    .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.fillMaxHeight(0.1f))
            Text(
                text = stringResource(R.string.ai_asset_title1),
                style = Typography.labelLarge.copy(fontSize = 24.sp),
            )
            Spacer(modifier = Modifier.height(7.dp))
            Text(
                text = stringResource(R.string.ai_asset_title2),
                style = Typography.titleLarge.copy(brush = Main02, fontSize = 40.sp),
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.1f))
            Row {
                Image(
                    painter = painterResource(id = R.drawable.img_m_artist),
                    contentDescription = null,
                )
                Image(
                    painter = painterResource(id = R.drawable.img_fm_artist),
                    contentDescription = null,
                )
            }
            Spacer(modifier = Modifier.fillMaxHeight(0.15f))
            Text(
                text = stringResource(R.string.ai_asset_script1),
                style = Typography.labelLarge.copy(fontSize = 20.sp),
            )
            Spacer(modifier = Modifier.height(4.dp))
            BoldTextWithKeywords(
                fullText = stringResource(R.string.ai_asset_script2),
                keywords = listOf("에셋으로 변환"),
                brushFlag = listOf(false),
                boldStyle = Typography.titleLarge.copy(fontSize = 20.sp),
                normalStyle = Typography.labelLarge.copy(fontSize = 20.sp),
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.25f))
            LongWhiteButton(
                icon = null,
                text = stringResource(R.string.ai_asset_draw_btn),
                onClick = navigateToDraw,
            )
            Spacer(modifier = Modifier.height(8.dp))
            LongWhiteButton(
                icon = null,
                text = stringResource(id = R.string.ai_asset_prompt_btn),
                onClick = navigateToPrompt,
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AiAssetPreview() {
    SemonemoTheme {
        AiAssetScreen()
    }
}
