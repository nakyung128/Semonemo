package com.semonemo.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.semonemo.presentation.theme.Gray01
import com.semonemo.presentation.theme.GunMetal
import com.semonemo.presentation.theme.SemonemoTheme
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.theme.WhiteGray
import com.semonemo.presentation.util.noRippleClickable

/**
 * TODO
 * @param keyword -> tag 안에 들어갈 내용
 * @param onTagClicked -> tag 클릭했을 때 람다식
 * @param isEdit -> 편집 모드 인지, 아닌지
 * @param onCloseClicked -> x 버튼 누를 경우
 */

@Composable
fun HashTag(
    modifier: Modifier = Modifier,
    keyword: String,
    onTagClicked: (String) -> Unit = {},
    isEdit: Boolean = false,
    onCloseClicked: (String) -> Unit = {},
) {
    Surface(
        modifier =
            modifier
                .wrapContentSize()
                .clickable(
                    onClick = { onTagClicked(keyword) },
                ),
        shape = RoundedCornerShape(8.dp),
        color = WhiteGray,
    ) {
        Row(
            modifier =
                Modifier
                    .wrapContentSize()
                    .padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "# $keyword",
                style = Typography.labelMedium,
                color = GunMetal,
            )
            if (isEdit) {
                Spacer(modifier = Modifier.width(3.dp))
                Icon(
                    modifier =
                        Modifier
                            .size(12.dp)
                            .noRippleClickable { onCloseClicked(keyword) },
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    tint = Gray01,
                )
            }
        }
    }
}

@Composable
@Preview()
fun HashTagEditPreview() {
    SemonemoTheme {
        HashTag(keyword = "이나경", isEdit = true)
    }
}

@Composable
@Preview()
fun HashTagPreview() {
    SemonemoTheme {
        HashTag(keyword = "짜이한")
    }
}
