package com.semonemo.presentation.screen.auction.subScreen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.semonemo.domain.model.Auction
import com.semonemo.presentation.component.LiveAuctionCard

private const val TAG = "AuctionReadScreen"

@Composable
fun AuctionReadScreen(
    modifier: Modifier = Modifier,
    auctionDataList: List<Auction> = listOf(),
    navigateToAuctionDetail: (Long) -> Unit = {},
) {
    Surface(
        modifier =
            modifier
                .fillMaxWidth()
                .wrapContentHeight(),
        color = Color.White,
    ) {
        LazyRow(
            modifier = Modifier.padding(vertical = 8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(auctionDataList) { data ->
                Log.d(TAG, "AuctionReadScreen: $data")
                LiveAuctionCard(
                    modifier =
                        Modifier
                            .width(160.dp)
                            .height(300.dp),
                    id = data.id,
                    status = data.status,
                    nftId = data.nftId,
                    nftImageUrl = data.nftImageUrl,
                    participants = data.participants,
                    startPrice = data.startPrice,
                    currentBid = data.currentBid,
                    finalPrice = data.finalPrice,
                    startTime = data.startTime,
                    endTime = data.endTime,
                    onClick = navigateToAuctionDetail,
                )
            }
        }
    }
}
