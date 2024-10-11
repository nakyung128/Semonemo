package com.semonemo.presentation.screen.auction.subScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.semonemo.presentation.R
import com.semonemo.presentation.component.LongBlackButton
import com.semonemo.presentation.screen.auction.AuctionDetailViewModel
import com.semonemo.presentation.theme.Typography

@Preview(showBackground = true)
@Composable
fun AuctionCancelScreen(
    modifier: Modifier = Modifier,
    viewModel: AuctionDetailViewModel = hiltViewModel(),
    popUpBackStack: () -> Unit = {},
) {
    val auctionCancelAnimation by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.auction_cancel))

    Surface(modifier = modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            LottieAnimation(
                composition = auctionCancelAnimation,
                restartOnPlay = true,
                iterations = LottieConstants.IterateForever,
                contentScale = ContentScale.Fit,
                modifier =
                    Modifier
                        .fillMaxHeight(0.5f)
                        .scale(1.2f),
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "경매 유찰",
                fontSize = 24.sp,
                style = Typography.bodyMedium,
            )
            Spacer(modifier = Modifier.height(10.dp))

            LongBlackButton(
                modifier =
                    Modifier
                        .fillMaxWidth(),
                text = stringResource(R.string.auction_leave_button_title),
                icon = null,
                onClick = {
                    popUpBackStack()
                },
            )
        }
    }
}
