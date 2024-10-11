package com.semonemo.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.semonemo.presentation.theme.Gray02
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.theme.WhiteGray

@Composable
fun LongUnableButton(
    modifier: Modifier = Modifier,
    text: String,
) {
    Surface(
        modifier =
            modifier
                .fillMaxWidth(0.88f)
                .height(50.dp),
        shape = RoundedCornerShape(14.dp), // 모서리 반경 10dp
        color = WhiteGray,
    ) {
        Box(
            modifier = Modifier.padding(16.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                modifier = Modifier.wrapContentSize(),
                text = text,
                style = Typography.bodySmall.copy(fontSize = 15.sp),
                textAlign = TextAlign.Center,
                color = Gray02,
            )
        }
    }
}

@Preview
@Composable
fun PreviewLongUnableButton() {
    LongUnableButton(text = "판매 등록")
}
