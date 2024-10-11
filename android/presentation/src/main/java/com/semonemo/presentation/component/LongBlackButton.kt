package com.semonemo.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.semonemo.presentation.R
import com.semonemo.presentation.theme.Main02
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.theme.White

/*
@Param  icon : 버튼에 들어갈 아이콘. 없다면 null
        text : 버튼에 들어갈 텍스트
 */

@Composable
fun LongBlackButton(
    modifier: Modifier = Modifier,
    icon: Int?,
    text: String,
    onClick: () -> Unit = {},
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        modifier =
            modifier
                .fillMaxWidth(0.88f)
                .height(50.dp),
    ) {
        Box(
            modifier =
                Modifier
                    .background(brush = Main02)
                    .padding(16.dp),
            contentAlignment = Alignment.Center,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                icon?.let {
                    Image(
                        modifier = Modifier.wrapContentSize(),
                        painter = painterResource(id = it),
                        contentDescription = "",
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                }
                Text(
                    modifier = Modifier.wrapContentSize(),
                    text = text,
                    style = Typography.bodySmall.copy(fontSize = 15.sp, color = White),
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun PreviewLongBlackButton() {
    LongBlackButton(icon = R.drawable.img_fire, text = "검정 버튼입니다.")
}
