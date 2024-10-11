package com.semonemo.presentation.screen.wallet.subscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.semonemo.presentation.R
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.theme.White
import com.semonemo.presentation.util.noRippleClickable
import java.util.Locale

@Composable
fun WalletCoinBox(
    modifier: Modifier = Modifier,
    coinPrice: Double,
    changePercent: Double,
    changePrice: Double,
    navigateToCoinDetail: () -> Unit,
) {
    Card(
        modifier =
            modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .noRippleClickable {
                    navigateToCoinDetail()
                },
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(2.dp),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(White)
                    .padding(horizontal = 30.dp, vertical = 20.dp),
            horizontalArrangement =
                Arrangement
                    .SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier =
                    Modifier
                        .wrapContentSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    modifier = Modifier.size(50.dp),
                    painter = painterResource(id = R.drawable.ic_color_sene_coin),
                    contentDescription = "",
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.coin_name),
                    style = Typography.bodyLarge.copy(fontSize = 15.sp),
                )
            }
            Image(
                modifier = Modifier.size(50.dp),
                painter = painterResource(id = R.drawable.img_graph),
                contentDescription = null,
            )
            Row(
                modifier = Modifier.wrapContentSize(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(3.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = stringResource(R.string.current_price),
                        style = Typography.labelLarge,
                    )
                    Text(text = String.format(Locale.KOREAN, "%,.0f", coinPrice))
                }
                Spacer(modifier = Modifier.width(24.dp))
                Column(
                    verticalArrangement = Arrangement.spacedBy(3.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = stringResource(R.string.fluctuation_rate),
                        style = Typography.labelLarge,
                    )
                    Text(
                        text =
                            if (changePrice > 0) {
                                "+$changePercent%"
                            } else {
                                "$changePercent%"
                            },
                        color = if (changePercent > 0) Color.Red else Color.Blue,
                        style = Typography.bodyMedium,
                    )
                    Text(
                        text = String.format(Locale.KOREAN, "%,.0f", changePrice),
                        color = if (changePercent > 0) Color.Red else Color.Blue,
                        style = Typography.labelSmall,
                    )
                }
            }
        }
    }
}
