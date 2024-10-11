package com.semonemo.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.semonemo.presentation.R
import com.semonemo.presentation.theme.Gray03
import com.semonemo.presentation.theme.Red
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.util.noRippleClickable

@Preview(showBackground = true)
@Composable
fun CustomTextButton(
    modifier: Modifier = Modifier,
    text: String = "버튼",
    isPossible: Boolean = true,
    onClick: () -> Unit = {},
) {
    var color = if (isPossible) Red else Gray03
    Box(modifier = modifier.padding(4.dp).noRippleClickable { if (isPossible) onClick() }) {
        Text(
            text = stringResource(id = R.string.auction_bid_give_up_button),
            color = color,
            style = Typography.bodyMedium,
            fontSize = 12.sp,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CustomTextButtonTruePreview() {
    CustomTextButton(text = "버튼", isPossible = true)
}

@Preview(showBackground = true)
@Composable
fun CustomTextButtonFalsePreview() {
    CustomTextButton(text = "버튼", isPossible = false)
}
