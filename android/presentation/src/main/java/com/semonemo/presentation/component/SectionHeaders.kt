package com.semonemo.presentation.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.semonemo.presentation.theme.GunMetal
import com.semonemo.presentation.theme.Typography

/**
 * 섹션을 구분하는 타이틀.
 * @param modifier
 * @param text 타이틀
 */
@Composable
fun SectionHeader(
    modifier: Modifier = Modifier,
    text: String,
) {
    Text(
        modifier = modifier.padding(5.dp),
        text = text,
        style = Typography.titleMedium.copy(color = GunMetal, fontSize = 18.sp),
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewSectionHeader() {
    SectionHeader(text = "텍스트 라벨")
}
