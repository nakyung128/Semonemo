package com.semonemo.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.semonemo.presentation.theme.Gray03
import com.semonemo.presentation.theme.Red
import com.semonemo.presentation.theme.SemonemoTheme
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.theme.White
import com.semonemo.presentation.theme.WhiteGray

@Composable
fun CustomNoRippleButton(
    modifier: Modifier = Modifier,
    text: String = "",
    enabled: Boolean = true,
    backgroundColor: Color = Red,
    contentColor: Color = White,
    onClick: () -> Unit,
) {
    val background = if (enabled) backgroundColor else WhiteGray
    val content = if (enabled) contentColor else Gray03

    Box(
        contentAlignment = Alignment.Center,
        modifier =
            modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(color = background, shape = RoundedCornerShape(10.dp))
                .clickable(
                    enabled = enabled,
                    onClick = onClick,
                    indication = null, // 리플 효과 제거
                    interactionSource = remember { MutableInteractionSource() },
                ).padding(16.dp),
    ) {
        Text(
            text = text,
            style = Typography.bodyMedium,
            color = content,
        )
    }
}

@Composable
fun ButtonWithDynamicState() {
    var isEnabled by remember { mutableStateOf(true) }

    // 동적으로 enabled 값을 변경하는 예시
    CustomNoRippleButton(
        text = if (isEnabled) "활성화됨" else "비활성화됨",
        enabled = isEnabled,
        backgroundColor = Red,
        contentColor = White,
        onClick = {
            // 버튼 클릭 시 상태 변경
            isEnabled = !isEnabled
        },
    )
}

@Composable
@Preview
fun ButtonWithDynamicStatePreview() {
    SemonemoTheme {
        ButtonWithDynamicState()
    }
}
