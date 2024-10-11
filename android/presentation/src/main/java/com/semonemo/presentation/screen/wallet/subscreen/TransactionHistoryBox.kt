package com.semonemo.presentation.screen.wallet.subscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.semonemo.presentation.R
import com.semonemo.presentation.screen.wallet.ProductInfo
import com.semonemo.presentation.theme.Blue1
import com.semonemo.presentation.theme.Blue2
import com.semonemo.presentation.theme.Blue3
import com.semonemo.presentation.theme.Blue4
import com.semonemo.presentation.theme.Gray02
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.theme.White
import java.util.Locale

@Composable
fun TransactionHistoryBox(
    modifier: Modifier = Modifier,
    isSell: Boolean = true,
    date: String = "2024.09.09",
    price: Double = +100000.0,
    product: String = "프레임",
    isAuction: Boolean = false,
) {
    val productInfo =
        if (product.contains("코인")) { //코인
            if (isSell) {
                ProductInfo(
                    stringResource(R.string.exchange),
                    R.drawable.ic_outline_coin,
                    color = Blue3,
                )
            } else {
                ProductInfo(
                    stringResource(R.string.recharge),
                    R.drawable.ic_outline_coin,
                    color = Blue3,
                )
            }
        } else if (product.contains("NFT")) { // 프레임
            if (isAuction) {
                ProductInfo(
                    stringResource(id = R.string.auction),
                    R.drawable.ic_fab_auction_click,
                    color = Blue4,
                )
            } else if (isSell) {
                ProductInfo(
                    stringResource(R.string.sell),
                    R.drawable.ic_fab_frame,
                    color = Blue2,
                )
            } else {
                ProductInfo(
                    stringResource(R.string.buy),
                    R.drawable.ic_fab_frame,
                    color = Blue2,
                )
            }
        } else { // 에셋
            if (isSell) {
                ProductInfo(
                    stringResource(R.string.sell),
                    R.drawable.ic_fab_asset,
                    color = Blue1,
                )
            } else {
                ProductInfo(
                    stringResource(R.string.buy),
                    R.drawable.ic_fab_asset,
                    color = Blue1,
                )
            }
        }

    Card(
        modifier =
            modifier
                .fillMaxWidth()
                .wrapContentHeight(),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(2.dp),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(White)
                    .padding(horizontal = 10.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier =
                    Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(productInfo.color),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = painterResource(id = productInfo.imageRes),
                    contentDescription = "",
                    modifier = Modifier.size(24.dp),
                )
            }
            Spacer(modifier = Modifier.fillMaxWidth(0.02f))
            Column(
                verticalArrangement = Arrangement.spacedBy(3.dp),
                horizontalAlignment = Alignment.Start,
            ) {
                Text(text = date, style = Typography.labelSmall.copy(color = Gray02))
                Text(text = "$product ${productInfo.message}", style = Typography.bodyLarge)
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text =
                    String.format(Locale.KOREAN, "%,.0f", price),
                color = if (isSell) Color.Red else Color.Blue,
                style = Typography.bodyLarge,
            )

            Text(text = stringResource(R.string.coin_unit_name), style = Typography.labelLarge)
            Spacer(modifier = Modifier.width(10.dp))
        }
    }
}
