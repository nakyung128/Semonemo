package com.semonemo.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.semonemo.presentation.theme.Gray02
import com.semonemo.presentation.theme.GunMetal
import com.semonemo.presentation.theme.White

/**
 * TODO
 *
 * @param sizes : 펜 굵기 사이즈 리스트
 * @param selectedSize : 선택된 사이즈
 * @param onSizeSelected : 사이즈 선택했을 때
 */
@Composable
fun PenPalette(
    sizes: List<Dp>,
    selectedSize: Dp,
    onSizeSelected: (Dp) -> Unit,
) {
    Row(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            sizes.forEach { size ->
                PenCircle(
                    onClick = { onSizeSelected(size) },
                    isSelected = size == selectedSize,
                    size = size,
                )
            }
        }
    }
}

/**
 * TODO
 *
 * @param onClick : 클릭했을 때
 * @param isSelected : 선택되었을 때
 */
@Composable
fun PenCircle(
    onClick: () -> Unit,
    isSelected: Boolean,
    size: Dp,
) {
    val borderColor = if (isSelected) Color.Red else Gray02

    Box(
        modifier =
            Modifier
                .size(30.dp)
                .clip(CircleShape)
                .background(White)
                .border(
                    width = 1.dp,
                    color = borderColor,
                    shape = CircleShape,
                ).clickable { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier =
                Modifier
                    .size(size)
                    .clip(CircleShape)
                    .background(GunMetal),
        )
    }
}
