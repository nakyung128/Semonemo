package com.semonemo.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.semonemo.presentation.R
import com.semonemo.presentation.theme.Gray03
import com.semonemo.presentation.theme.SemonemoTheme
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.theme.White

@Composable
fun BidPriceUnitButton(
    modifier: Modifier = Modifier,
    bidPricePercentage: Int = 10,
    bidPriceUnit: Long = 1000,
    adjustButton: Triple<Boolean, Boolean, Boolean> = Triple(true, true, true),
    onClick: () -> Unit = {},
) {
    val flag =
        when (bidPricePercentage) {
            100 -> {
                adjustButton.first
            }
            50 -> {
                adjustButton.second
            }
            else -> {
                adjustButton.third
            }
        }
    val color = if (flag) White else Gray03
    Surface(
        modifier = modifier.fillMaxWidth().padding(2.dp),
        onClick =
            if (flag) {
                onClick
            } else {
                {}
            },
        color = color,
        shape = RoundedCornerShape(10.dp),
        shadowElevation = 2.dp,
    ) {
        Box(
            modifier = Modifier.padding(8.dp),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                modifier = Modifier.wrapContentSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "$bidPricePercentage%",
                    style = Typography.bodySmall,
                )

                Text(
                    text =
                        String.format(
                            "%,d ${stringResource(id = R.string.coin_price_unit)}",
                            bidPriceUnit,
                        ),
                    style =
                        Typography.labelMedium.copy(
                            fontFeatureSettings = "tnum",
                            fontSize = 10.sp,
                        ),
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun BidPriceUnitButtonPreview() {
    SemonemoTheme {
        BidPriceUnitButton()
    }
}
