package com.semonemo.presentation.screen.store

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.semonemo.presentation.screen.store.subScreen.StoreSubFullViewRoute

@Composable
fun StoreFullViewScreen(
    modifier: Modifier = Modifier,
    isFrame: Boolean,
    popUpBackStack: () -> Unit,
    navigateToFrameDetail: (Long) -> Unit,
    navigateToAssetDetail: (Long) -> Unit,
    navigateToFrameSale: () -> Unit,
    navigateToAssetSale: () -> Unit,
    onShowErrorSnackBar: (String) -> Unit,
) {
    Column(
        modifier =
            modifier
                .navigationBarsPadding()
                .statusBarsPadding(),
    ) {
        StoreSubFullViewRoute(
            modifier = modifier,
            isFrame = isFrame,
            popUpBackStack = popUpBackStack,
            navigateToFrameDetail = navigateToFrameDetail,
            navigateToAssetDetail = navigateToAssetDetail,
            navigateToAssetSale = navigateToAssetSale,
            navigateToFrameSale = navigateToFrameSale,
            onShowErrorSnackBar = onShowErrorSnackBar,
        )
    }
}
