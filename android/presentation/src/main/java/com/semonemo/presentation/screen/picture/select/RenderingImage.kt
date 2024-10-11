package com.semonemo.presentation.screen.picture.select

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp

@Composable
fun RenderImageRow(
    modifier: Modifier = Modifier,
    startIndex: Int,
    selectedPictures: List<Bitmap>,
) {
    Row(
        modifier =
            modifier
                .padding(horizontal = 5.dp)
                .padding(top = 5.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        for (i in 0..1) {
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .weight(1f),
            ) {
                val index = startIndex + i
                if (selectedPictures.size > index) {
                    Image(
                        bitmap = selectedPictures[index].asImageBitmap(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }
        }
    }
}

@Composable
fun RenderImage(
    modifier: Modifier,
    index: Int,
    selectedPictures: List<Bitmap>,
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth(),
    ) {
        if (selectedPictures.size > index) {
            Image(
                bitmap = selectedPictures[index].asImageBitmap(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
    }
}
