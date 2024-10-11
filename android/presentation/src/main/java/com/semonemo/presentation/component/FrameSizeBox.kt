package com.semonemo.presentation.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.semonemo.presentation.R
import com.semonemo.presentation.theme.GunMetal
import com.semonemo.presentation.theme.SemonemoTheme
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.theme.White

/**
 * TODO
 *
 * @param modifier
 * @param title : 프레임 사이즈 타이틀
 * @param script : 프레임 사이즈 설명
 * @param frameImg : 프레임 예시 이미지
 * @param onClick : 박스 클릭 시 동작
 */
@Composable
fun FrameSizeBox(
    modifier: Modifier = Modifier,
    title: String,
    script: String,
    @DrawableRes frameImg: Int,
    onClick: () -> Unit = {},
) {
    Card(
        modifier =
            modifier
                .fillMaxWidth()
                .wrapContentHeight(),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(3.dp),
        onClick = onClick,
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(color = White),
        ) {
            Row(
                modifier = Modifier.wrapContentHeight(),
            ) {
                Column(
                    modifier =
                        Modifier
                            .wrapContentSize()
                            .padding(horizontal = 20.dp, vertical = 15.dp),
                    horizontalAlignment = Alignment.Start,
                ) {
                    Row(
                        modifier = Modifier.wrapContentSize(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = title,
                            style = Typography.bodyLarge.copy(fontSize = 24.sp),
                            color = GunMetal,
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.ic_navigate_next),
                            contentDescription = "ic_navigate_next",
                            tint = Color.Unspecified,
                        )
                    }
                    Text(
                        text = script,
                        style = Typography.labelMedium.copy(fontSize = 15.sp),
                        color = GunMetal,
                    )
                }
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd,
                ) {
                    Image(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 20.dp),
                        painter = painterResource(id = frameImg),
                        contentDescription = "frame_img",
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FrameSizeBoxPreview() {
    SemonemoTheme {
        FrameSizeBox(
            modifier = Modifier.padding(10.dp),
            title = "2x2 사이즈",
            script = "조금 더 크게 찍고 싶을 때",
            frameImg = R.drawable.img_frame_size_two_by_two,
        )
    }
}
