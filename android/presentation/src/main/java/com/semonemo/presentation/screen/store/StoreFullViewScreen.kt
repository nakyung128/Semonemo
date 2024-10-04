package com.semonemo.presentation.screen.store

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.semonemo.presentation.screen.store.subScreen.StoreSubFullViewScreen

@Composable
fun StoreFullViewScreen(
    modifier: Modifier = Modifier,
    isFrame: Boolean,
) {
    Column(
        modifier =
            modifier
                .navigationBarsPadding()
                .statusBarsPadding(),
    ) {
        StoreSubFullViewScreen(
            modifier = Modifier,
            isFrame = isFrame,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun StoreFullViewScreenPreview() {
    StoreFullViewScreen(isFrame = true)
}
