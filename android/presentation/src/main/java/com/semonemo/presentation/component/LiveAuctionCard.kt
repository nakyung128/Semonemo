package com.semonemo.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.semonemo.presentation.BuildConfig
import com.semonemo.presentation.R
import com.semonemo.presentation.animation.LiveAnimation
import com.semonemo.presentation.screen.auction.AuctionStatus
import com.semonemo.presentation.theme.GunMetal
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.theme.White
import com.semonemo.presentation.util.noRippleClickable
import com.skydoves.landscapist.glide.GlideImage
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private const val TAG = "LiveAuctionCard"

@Composable
fun LiveAuctionCard(
    modifier: Modifier = Modifier,
    id: Long,
    status: String,
    nftId: Long,
    nftImageUrl: String,
    participants: Int,
    startPrice: Long,
    currentBid: Long,
    finalPrice: Long,
    startTime: String?,
    endTime: String?,
    onClick: (Long) -> Unit = {},
) {
    val auctionStatus = AuctionStatus.valueOf(status)
    val ipfsUrl = BuildConfig.IPFS_READ_URL
    val imgUrl = ipfsUrl + "ipfs/" + nftImageUrl
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier =
            when (auctionStatus) {
                AuctionStatus.READY, AuctionStatus.PROGRESS -> modifier.noRippleClickable { onClick(id) }
                else -> modifier
            },
    ) {
        // Overlay content
        Box(modifier = Modifier.wrapContentSize()) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth(),
            ) {
                Spacer(modifier = Modifier.height(4.dp))
                // Top row
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(40.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    // LiveAnimation
                    val dateTimFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    val deadLine : LocalDateTime
                    if(startTime != null) {
                        deadLine = LocalDateTime.parse(startTime, dateTimFormat)
                    } else {
                        deadLine = LocalDateTime.now().plusDays(1)
                    }


                    if (auctionStatus == AuctionStatus.PROGRESS) {
                        LiveAnimation()
                    } else if (auctionStatus == AuctionStatus.READY) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(4.dp),
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_auction_time),
                                contentDescription = "auction_time",
                                tint = Color.Unspecified,
                                modifier = Modifier.size(20.dp),
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            CustomCountdownTimer(deadline = deadLine)
                        }
                    } else {
                        Spacer(
                            modifier =
                                Modifier.height(
                                    4.dp,
                                ),
                        )
                    }

                    // Viewer count
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(4.dp),
                    ) {
                        if (auctionStatus == AuctionStatus.READY || auctionStatus == AuctionStatus.PROGRESS) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_auction_human),
                                contentDescription = "Viewers",
                                tint = Color.Unspecified,
                                modifier = Modifier.size(20.dp),
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("$participants", color = GunMetal)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                GlideImage(
                    imageModel = imgUrl,
                    contentScale = ContentScale.Fit,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .weight(1f),
                    loading = {
                        ImageLoadingProgress(
                            modifier = Modifier.fillMaxSize(),
                        )
                    },
                )

                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(40.dp),
                    horizontalArrangement = Arrangement.End,
                ) {
                    // Price
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(4.dp),
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_color_sene_coin),
                            contentDescription = "Likes",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(20.dp),
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        val previewPrice =
                            when (auctionStatus) {
                                AuctionStatus.READY, AuctionStatus.CANCEL -> startPrice
                                AuctionStatus.PROGRESS -> currentBid
                                AuctionStatus.END -> finalPrice
                            }
                        Text(
                            text =
                                String.format(
                                    "%,d ${stringResource(id = R.string.coin_price_unit)}",
                                    previewPrice,
                                ),
                            color = GunMetal,
                            fontSize = 18.sp,
                            style =
                                Typography.bodyMedium.copy(
                                    fontFeatureSettings = "tnum",
                                ),
                            textAlign = TextAlign.Center,
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
            if (auctionStatus == AuctionStatus.END || auctionStatus == AuctionStatus.CANCEL) {
                Box(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .background(GunMetal.copy(alpha = 0.5f)), // 반투명 회색
                    contentAlignment = Alignment.Center,
                ) {
                    val text =
                        if (auctionStatus == AuctionStatus.END) {
                            String.format(
                                "%,d ${stringResource(id = R.string.coin_price_unit)}\n낙찰",
                                finalPrice,
                            )
                        } else {
                            "경매 유찰"
                        }
                    Text(
                        text = text,
                        color = White,
                        fontSize = 18.sp,
                        style =
                            Typography.titleMedium.copy(
                                fontFeatureSettings = "tnum",
                            ),
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}
