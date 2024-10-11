package com.semonemo.presentation.screen.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.semonemo.presentation.theme.Gray03
import com.semonemo.presentation.theme.SemonemoTheme
import com.semonemo.presentation.theme.Typography

/**
 * TODO
 *
 * @param modifier
 * @param number : 순위
 * @param keyword : 키워드
 */
@Composable
fun HotKeyWord(
    modifier: Modifier = Modifier,
    number: Int,
    keyword: String,
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.padding(start = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = number.toString(),
                style = Typography.bodyMedium,
            )
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                text = keyword,
                style = Typography.labelLarge,
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 10.dp),
            color = Gray03,
            thickness = 1.dp,
        )
    }
}

@Composable
@Preview(showSystemUi = true)
fun HotKeyWordPreview() {
    SemonemoTheme {
        HotKeyWord(number = 1, keyword = "짜이한")
    }
}
