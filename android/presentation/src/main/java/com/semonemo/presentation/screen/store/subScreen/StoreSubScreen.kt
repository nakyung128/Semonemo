package com.semonemo.presentation.screen.store.subScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.semonemo.domain.model.FrameDetail
import com.semonemo.domain.model.SellAssetDetail
import com.semonemo.presentation.component.StoreItemCard
import com.semonemo.presentation.util.noRippleClickable
import com.semonemo.presentation.util.urlToIpfs

@Preview(showBackground = true)
@Composable
fun StoreSubScreen(
    modifier: Modifier = Modifier,
    isFrame: Boolean = true,
    saleFrames: List<FrameDetail> = listOf(),
    saleAssets: List<SellAssetDetail> = listOf(),
    navigateToFrameDetail: (Long) -> Unit = {},
    navigateToAssetDetail: (Long) -> Unit = {},
) {
    LazyHorizontalGrid(
        modifier = modifier.wrapContentHeight(),
        rows = GridCells.Fixed(if (isFrame) 1 else 2),
        contentPadding = PaddingValues(8.dp, 0.dp, 8.dp, 0.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        if (isFrame) {
            items(saleFrames) { storeItem ->
                StoreItemCard(
                    modifier =
                        Modifier
                            .fillMaxHeight()
                            .aspectRatio(3f / 4f)
                            .clickable { navigateToFrameDetail(storeItem.marketId) },
                    author = storeItem.seller.nickname,
                    imgUrl =
                        storeItem.nftInfo.data.image
                            .urlToIpfs(),
                    price = storeItem.price.toInt(),
                    isLiked = storeItem.isLiked,
                )
            }
        } else {
            items(saleAssets) { storeItem ->
                StoreItemCard(
                    modifier =
                        Modifier
                            .fillMaxHeight()
                            .aspectRatio(1f)
                            .noRippleClickable {
                                navigateToAssetDetail(storeItem.assetSellId)
                            },
                    author = storeItem.creator.nickname,
                    imgUrl = storeItem.imageUrl,
                    price = storeItem.price.toInt(),
                    isLiked = storeItem.isLiked,
                )
            }
        }
    }
}
