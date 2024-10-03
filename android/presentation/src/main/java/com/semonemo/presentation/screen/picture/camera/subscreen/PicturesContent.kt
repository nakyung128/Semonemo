package com.semonemo.presentation.screen.picture.camera.subscreen

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp

@Composable
fun PicturesContent(
    modifier: Modifier,
    bitmaps: List<Bitmap>,
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        contentPadding = PaddingValues(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        content = {
            items(count = bitmaps.size) { index ->
                Image(
                    modifier =
                        Modifier
                            .width(75.dp)
                            .height(150.dp)
                            .clip(RoundedCornerShape(10.dp)),
                    bitmap = bitmaps[index].asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                )
            }
        },
    )
}
