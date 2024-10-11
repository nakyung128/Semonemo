package com.semonemo.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.semonemo.presentation.theme.Gray01
import com.semonemo.presentation.theme.Main02
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.theme.White

@Composable
fun AssetButton(
    onClick: () -> Unit,
    isSelected: Boolean,
    title: String,
) {
    if (isSelected) {
        Box(
            modifier =
                Modifier
                    .clip(shape = RoundedCornerShape(20.dp))
                    .background(brush = Main02)
                    .clickable { onClick() },
        ) {
            Text(
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp),
                text = title,
                color = White,
                style = Typography.bodySmall.copy(fontSize = 14.sp),
            )
        }
    } else {
        Box(
            modifier =
                Modifier
                    .border(width = 1.dp, color = Gray01, shape = RoundedCornerShape(20.dp))
                    .clip(shape = RoundedCornerShape(20.dp))
                    .background(color = White)
                    .clickable { onClick() },
        ) {
            Text(
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp),
                text = title,
                style = Typography.labelMedium,
            )
        }
    }
}
