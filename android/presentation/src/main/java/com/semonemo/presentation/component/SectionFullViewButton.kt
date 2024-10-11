package com.semonemo.presentation.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.semonemo.presentation.R
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.util.noRippleClickable

@Composable
fun SectionFullViewButton(
    modifier: Modifier = Modifier,
    onClick: (Boolean) -> Unit = {},
) {
    Row(
        modifier =
            modifier
                .wrapContentWidth()
                .padding(4.dp)
                .noRippleClickable{onClick(true)},
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(id = R.string.section_full_view),
            style = Typography.bodyMedium,
            fontSize = 12.sp,
        )
        Icon(
            modifier = Modifier.size(12.dp),
            painter = painterResource(R.drawable.ic_arrow_right),
            contentDescription = "ArrowRight",
        )
    }
}
