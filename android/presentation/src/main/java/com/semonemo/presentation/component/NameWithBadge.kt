package com.semonemo.presentation.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.semonemo.presentation.R
import com.semonemo.presentation.theme.GunMetal
import com.semonemo.presentation.theme.Typography

/**
 * 닉네임 + 뱃지 component
 * name : String (닉네임)
 * size : Int (폰트 사이즈)
 * style : TextStyle (폰트 스타일 종류)
 */

@Composable
fun NameWithBadge(
    modifier: Modifier = Modifier,
    name: String = "닉네임",
    size: Int = 14,
    style: TextStyle = Typography.bodySmall,
) {
    Row(
        modifier = modifier.wrapContentWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = name,
            style = style.copy(fontSize = size.sp),
            color = GunMetal,
        )
        Spacer(modifier = Modifier.width(2.dp))
        Icon(
            painter = painterResource(id = R.drawable.ic_user_certified),
            contentDescription = null,
            tint = Color.Unspecified,
        )
    }
}

@Composable
@Preview(showBackground = true)
fun NameWithBadgePreview() {
    NameWithBadge()
}
