package com.semonemo.presentation.screen.store.subScreen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.semonemo.domain.model.AssetDetail
import com.semonemo.domain.model.FrameDetail
import com.semonemo.presentation.component.StoreItemCard
import com.semonemo.presentation.util.urlToIpfs

@Preview(showBackground = true)
@Composable
fun StoreSubScreen(
    modifier: Modifier = Modifier,
    isFrame: Boolean = true,
    saleFrames: List<FrameDetail> = listOf(),
    saleAssets: List<AssetDetail> = listOf(),
) {
    val frameDataList = getSampleFrameData(5)
    LazyHorizontalGrid(
        modifier = modifier,
        rows = GridCells.Fixed(if (isFrame) 1 else 2),
        contentPadding = PaddingValues(8.dp, 0.dp, 8.dp, 0.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        if (isFrame) {
            items(saleFrames) { storeItem ->
                StoreItemCard(
                    modifier =
                        modifier
                            .fillMaxWidth()
                            .aspectRatio(if (isFrame) 0.6f else 0.8f),
                    title = storeItem.nftInfo.data.title,
                    author = storeItem.nftInfo.data.title, // 실제로는 만든 사람이 들어가야하는데 데이터가 없음
                    imgUrl =
                        storeItem.nftInfo.data.image
                            .urlToIpfs(),
                    price = storeItem.price.toInt(),
                    isLiked = storeItem.isLiked,
                )
            }
        } else {
            items(frameDataList) { storeItem ->
                StoreItemCard(
                    modifier =
                        modifier
                            .fillMaxWidth()
                            .aspectRatio(if (isFrame) 0.6f else 0.8f),
                    title = storeItem.title,
                    author = storeItem.author,
                    imgUrl = storeItem.imgUrl,
                    price = storeItem.price,
                    isLiked = storeItem.isLiked,
                )
            }
        }
    }
}

data class StoreItem(
    val id: Int,
    val title: String,
    val author: String,
    val imgUrl: String,
    val price: Int,
    val isLiked: Boolean,
)

fun getSampleFrameData(repeatCount: Int = 1): List<StoreItem> {
    val frameItems =
        listOf(
            StoreItem(
                id = 1,
                title = "기쁨이",
                author = "Joy",
                imgUrl = "https://i.namu.wiki/i/09iVxwLpv2_ZI2Biduew-71s5nmvJfTY7PTbHOVYk14G9P_mTK4WGmyL4Upn0IjxZeilzUNKNdjid3JVFSjpTW2nNDD7qZAQp1uP-jiNibqF7QGFAMkCYRNLXLAlFGGZQJeKShndHtafLR0M63JStw.webp",
                price = 1000,
                isLiked = true,
            ),
            StoreItem(
                id = 1,
                title = "슬픔이",
                author = "Sadness",
                imgUrl = "https://i.namu.wiki/i/e7YvbUcT4kPGKVZW4tldwoE70kBVONbgPx9jkLh43F1a34xCzIczDTU7KBktvfvju_4HXvJ0dqEsw9vHhdz5Jz6rM5p919MBMhNbVDrChGctg_smLaXSQfc8niAanGHUfPDCO2AD4fNGaPPFzMJnvA.webp",
                price = 2000,
                isLiked = true,
            ),
            StoreItem(
                id = 1,
                title = "버럭이",
                author = "Anger",
                imgUrl = "https://i.namu.wiki/i/fKJMumBnXPNNZxcqFYA9KbuajFX4R11WOr5as_wepxN6SHqSgUe5jg7ikamOXrppoy-nwpQPTIrGzRoeZqFGAWDcuN-NaiL36-4GmTRWXbdFylbEXSqkyziWomNtuVRCmLxmJmCV4cUGPuShmKazXw.webp",
                price = 3000,
                isLiked = true,
            ),
            StoreItem(
                id = 1,
                title = "까칠이",
                author = "Disgust",
                imgUrl = "https://i.namu.wiki/i/kvwois2K-NSS27h9ZXn-kJQw8nPBPwEbZO9dRHCC1PrIh3aW0bX7tyFh3fhRc2AN2meSW6vWwi4CBiHGbjG-dngypmUOSRNiNGPajwknr-U9BMM6Ucxb0GQuxBRIpBwt_wPhBeGbKDBoDy6eQwCxbA.webp",
                price = 4000,
                isLiked = true,
            ),
            StoreItem(
                id = 1,
                title = "소심이",
                author = "Fear",
                imgUrl = "https://i.namu.wiki/i/o9TroKLH0FAm1Y9YMkdgawJG5VwIcCrJsAIk_al--zFnN9TrXc3YukXkxZbIzlrZhHJH0Mo-VJQSyXinVuK29MFls0a671pX-VJXAyB7iX8ijQ9t0SDj_KiCtL0doj72tPNo604Mui2SWRxyXiGNfg.webp",
                price = 5000,
                isLiked = true,
            ),
            StoreItem(
                id = 1,
                title = "불안이",
                author = "Anxiety",
                imgUrl = "https://i.namu.wiki/i/8HryogxlixbdTohi_zXzquNzPR1boi65WeeGzRdC9Oz40L-X6LA3vQb5Uxtwf-KwFOgkV5nDExSIcvApwLjJdsc8r2rMlwFAbGLT0t3Z6PBG6cm_-yZBIGlPj7CUbWSTskWHtfj4QMbUVi1lM22WkA.webp",
                price = 6000,
                isLiked = true,
            ),
            StoreItem(
                id = 1,
                title = "부럽이",
                author = "Envy",
                imgUrl = "https://i.namu.wiki/i/RMYvuytsAzg3wUwdzzPMn_jSLuChdR4SxCQQ0Jau4GsLtBxzX0cs9kOro19ldbjkfer7TNLdvcO-O4AiyF6BBUvq2Wp7aJDrRVm848q_0TbecAgHPRg0x8MTWx-wfywnn3ThXaO3QfhXxQn_SJTMxA.webp",
                price = 7000,
                isLiked = true,
            ),
            StoreItem(
                id = 1,
                title = "따분이",
                author = "Ennui",
                imgUrl = "https://i.namu.wiki/i/A05ub5cLal6b41tDHQSbOvEyyyoquh59egSAgGc6YNeBJvMrGBVAGI6H53siTNm5Ol22xluIavMjnFHBDiTZBvOFXrvgogWRR44t5auGogjslcwqDVTgPyF_C-qEwMWCkTrB1MwC1C9IAkQRBmkFdw.webp",
                price = 8000,
                isLiked = true,
            ),
            StoreItem(
                id = 1,
                title = "당황이",
                author = "Embarrassment",
                imgUrl = "https://i.namu.wiki/i/v_jL7UE8_d5Cmw9aEY3yeuSSevNpcLjQVrlhr2pa5qqU6VKYHkOpbzBpb4A738G2FbcmF3ZB7_a3ZIkBe4QIG-Kbb67dDZJfw2yZZQI_5_1YVjiR5Vlj21_C8UYXMMQ7JdP7z1mDGDpu0I3BRQFFzw.webp",
                price = 9000,
                isLiked = true,
            ),
        )
    val repeatedItems = mutableListOf<StoreItem>()
    repeat(repeatCount) {
        repeatedItems.addAll(frameItems)
    }
    return repeatedItems
}
