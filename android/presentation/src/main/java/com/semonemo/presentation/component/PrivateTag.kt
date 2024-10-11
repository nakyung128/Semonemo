package com.semonemo.presentation.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
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
import com.semonemo.presentation.theme.Gray01
import com.semonemo.presentation.theme.Gray03
import com.semonemo.presentation.theme.SemonemoTheme
import com.semonemo.presentation.theme.Typography

/**
 * TODO
 *
 * @param title : 태그 타이틀 ex) 비공개, 비판매
 */

@Composable
fun PrivateTag(
    modifier: Modifier = Modifier,
    title: String,
) {
    Surface(
        modifier = modifier.wrapContentSize(),
        shape = RoundedCornerShape(7.dp),
        color = Gray03,
    ) {
        Row(
            modifier =
                Modifier
                    .wrapContentSize()
                    .padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier.size(10.dp),
                painter = painterResource(id = R.drawable.ic_private),
                contentDescription = "ic_private",
                tint = Color.Unspecified,
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
                text = title,
                style = Typography.labelSmall.copy(fontSize = 13.sp),
                color = Gray01,
            )
        }
    }
}

@Preview
@Composable
fun PrivateTagPreview() {
    SemonemoTheme {
        PrivateTag(
            title = "비공개",
        )
    }
}
