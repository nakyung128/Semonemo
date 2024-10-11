package com.semonemo.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.semonemo.presentation.R
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.theme.White
import com.semonemo.presentation.util.noRippleClickable

@Composable
fun MomentBox(
    modifier: Modifier,
    title: String,
    subTitle: String,
    icon: Int,
    onClick: () -> Unit = {},
) {
    Card(
        modifier =
            modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .noRippleClickable { onClick() },
        shape = RoundedCornerShape(15.dp),
        elevation = CardDefaults.cardElevation(2.dp),
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(color = White)
                    .padding(10.dp),
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
            ) {
                Text(
                    modifier = Modifier.padding(start = 5.dp, top = 5.dp),
                    text = title,
                    style = Typography.bodyLarge,
                    fontSize = 24.sp,
                )
                Text(
                    modifier = Modifier.padding(start = 5.dp),
                    text = subTitle,
                    style = Typography.labelLarge,
                    fontSize = 20.sp,
                )
                Spacer(modifier = Modifier.height(20.dp))
                Box(modifier = Modifier.align(Alignment.End)) {
                    Image(
                        modifier = Modifier.size(85.dp),
                        painter = painterResource(id = icon),
                        contentDescription = null,
                    )
                }
            }
        }
    }
}

@Composable
fun MomentLongBox(
    modifier: Modifier,
    title: String,
    subTitle: String,
    icon: Int,
    onClick: () -> Unit = {},
) {
    Card(
        modifier =
            modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .noRippleClickable { onClick() },
        shape = RoundedCornerShape(15.dp),
        elevation = CardDefaults.cardElevation(2.dp),
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(color = White)
                    .padding(10.dp),
        ) {
            Row {
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .weight(1f),
                ) {
                    Text(
                        modifier = Modifier.padding(start = 5.dp, top = 5.dp),
                        text = title,
                        style = Typography.bodyLarge,
                        fontSize = 24.sp,
                    )
                    Text(
                        modifier = Modifier.padding(start = 5.dp),
                        text = subTitle,
                        style = Typography.labelLarge,
                        fontSize = 20.sp,
                    )
                }
                Image(
                    modifier = Modifier.size(120.dp),
                    painter = painterResource(id = icon),
                    contentDescription = null,
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun MomentBoxPreview() {
    MomentLongBox(
        modifier = Modifier,
        title = "나만의 프레임 NFT",
        subTitle = "만들어 보기",
        icon = R.drawable.img_sparkles,
    )
}
