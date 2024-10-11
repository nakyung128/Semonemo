package com.semonemo.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.semonemo.presentation.R
import com.semonemo.presentation.theme.Gray02
import com.semonemo.presentation.theme.GunMetal
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.theme.WhiteGray

@Preview(showBackground = true)
@Composable
fun AuctionBidPriceInput(
    modifier: Modifier = Modifier,
    currentPrice: Long = 1000,
    adjustedAmount: Long = 0,
    adjustedPercentage: Int = 10,
    onClear: () -> Unit = {},
) {
    val finalPrice = currentPrice + adjustedAmount

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(WhiteGray, RoundedCornerShape(8.dp))
                .padding(horizontal = 8.dp, vertical = 0.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        IconButton(onClick = { onClear() }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_search_cancel),
                contentDescription = "Clear",
                tint = Gray02,
            )
        }

        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = String.format("( +%,d%% )", adjustedPercentage),
                fontSize = 16.sp,
                style =
                    Typography.bodyMedium.copy(
                        fontFeatureSettings = "tnum",
                        color = Gray02,
                    ),
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = String.format("%,d ${stringResource(id = R.string.coin_price_unit)}", finalPrice),
                fontSize = 24.sp,
                style =
                    Typography.bodyMedium.copy(
                        fontFeatureSettings = "tnum",
                        color = GunMetal,
                    ),
            )
        }
    }
}
