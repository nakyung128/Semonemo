package com.semonemo.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.semonemo.presentation.R
import com.semonemo.presentation.theme.GunMetal
import com.semonemo.presentation.theme.Red
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.theme.White
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun StoreItemCard(
    modifier: Modifier = Modifier,
    author: String,
    imgUrl: String,
    price: Int,
    isLiked: Boolean = false,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = White),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = author, style = Typography.labelMedium)
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    painter = painterResource(id = R.drawable.ic_user_certified),
                    contentDescription = "Verified",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(16.dp),
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            GlideImage(
                imageModel = imgUrl,
                contentScale = ContentScale.Fit,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .weight(1f),
                loading = {
                    ImageLoadingProgress(
                        modifier = Modifier,
                    )
                },
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_color_sene_coin),
                        contentDescription = "Price",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(16.dp),
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text =
                        String.format(
                            "%,d ${stringResource(id = R.string.coin_price_unit)}",
                            price,
                        ),
                        fontWeight = FontWeight.Bold,
                        style = Typography.bodyMedium.copy(
                            fontFeatureSettings = "tnum",
                        ),
                        fontSize = 12.sp,
                    )
                }
                Icon(
                    painter = painterResource(id = if (isLiked) R.drawable.ic_toggle_heart_on else R.drawable.ic_toggle_heart_off),
                    contentDescription = "Like",
                    tint = if (isLiked) Red else GunMetal,
                    modifier = Modifier.size(16.dp),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StoreItemCardPreview() {
    StoreItemCard(
        author = "Sample Author",
        imgUrl = "https://example.com/sample_image.jpg",
        price = 1000,
        isLiked = true,
    )
}
