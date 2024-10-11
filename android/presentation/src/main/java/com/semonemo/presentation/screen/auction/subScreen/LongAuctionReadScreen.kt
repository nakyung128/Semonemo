package com.semonemo.presentation.screen.auction.subScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items // 이 import를 추가하세요
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.semonemo.presentation.component.AuctionCard

@Preview(showBackground = true, name = "AuctionItemGrid")
@Composable
fun LongAuctionReadScreen(modifier: Modifier = Modifier) {
    val auctionDataList = getSampleAuctionItems()

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp, 0.dp, 8.dp, 0.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(auctionDataList) { auctionItem ->
            // 여기를 수정했습니다
            AuctionCard(
                title = auctionItem.title,
                author = auctionItem.author,
                imageUrl = auctionItem.imageUrl,
                remainingTime = auctionItem.remainingTime,
                price = auctionItem.price,
                isLiked = auctionItem.isLiked,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .aspectRatio(0.6f),
            )
        }
    }
}

data class AuctionItem(
    val id: Int,
    val title: String,
    val author: String,
    val imageUrl: String,
    val remainingTime: String,
    val price: Int,
    val isLiked: Boolean,
)

fun getSampleAuctionItems(): List<AuctionItem> =
    listOf(
        AuctionItem(
            id = 1,
            title = "인사이드 아웃",
            author = "짜이한",
            imageUrl = "https://scontent-ssn1-1.cdninstagram.com/v/t51.29350-15/448780513_809743754453333_3780583223191906549_n.webp?stp=dst-jpg_e35&efg=eyJ2ZW5jb2RlX3RhZyI6ImltYWdlX3VybGdlbi4xMDgweDEwODAuc2RyLmYyOTM1MC5kZWZhdWx0X2ltYWdlIn0&_nc_ht=scontent-ssn1-1.cdninstagram.com&_nc_cat=105&_nc_ohc=kgQBTulWQGQQ7kNvgFuq9Ot&edm=APs17CUBAAAA&ccb=7-5&ig_cache_key=MzM5NTEwODMzMDUzOTQ0MDA5OQ%3D%3D.3-ccb7-5&oh=00_AYDs0zG4oWG9jpF51Rxlsuv4Jx1ZxZXEICSxS22TdseO1Q&oe=66F17B5A&_nc_sid=10d13b",
            remainingTime = "06:22:41:03",
            price = 20,
            isLiked = false,
        ),
        AuctionItem(
            id = 2,
            title = "루피와 함께",
            author = "짜이한",
            imageUrl = "https://s.alicdn.com/@sc04/kf/H38dbbd028b774ecdb0ecd671758ffc51o.jpg?avif=close",
            remainingTime = "00:00:26:59",
            price = 70,
            isLiked = true,
        ),
        // 추가 아이템들...
        AuctionItem(
            id = 3,
            title = "내 동료가 돼라",
            author = "짜이한",
            imageUrl = "https://scontent-ssn1-1.xx.fbcdn.net/v/t39.30808-6/254997745_4331068217005234_2131835778037010067_n.png?_nc_cat=105&ccb=1-7&_nc_sid=127cfc&_nc_ohc=E0MqxCtrYwIQ7kNvgFwswrZ&_nc_ht=scontent-ssn1-1.xx&_nc_gid=Aq-1g11r_QZgrx1KoYWB2L3&oh=00_AYDDK4uVUWEIRxVYdb4g8PunaYb75UIvmXVwBsA3KZqJ3A&oe=66F17C72",
            remainingTime = "02:14:35:22",
            price = 50,
            isLiked = false,
        ),
        AuctionItem(
            id = 4,
            title = "잔망 루피",
            author = "짜이한",
            imageUrl = "https://cdn.imweb.me/upload/S20201217ff49dfd1962cf/c5c775a7ce003.png",
            remainingTime = "01:08:15:40",
            price = 80,
            isLiked = true,
        ),
    )
