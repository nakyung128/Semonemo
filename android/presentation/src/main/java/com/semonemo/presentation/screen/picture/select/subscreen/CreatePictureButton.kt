package com.semonemo.presentation.screen.picture.select.subscreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.semonemo.presentation.R
import com.semonemo.presentation.theme.SemonemoTheme
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.util.noRippleClickable

@Composable
fun CreatePictureButton(
    modifier: Modifier = Modifier,
    isEnabled: Boolean = false,
    onClick: () -> Unit = {},
) {
    val (text, img) =
        if (isEnabled) {
            Pair("제작하기", R.drawable.ic_image_down)
        } else {
            Pair("", R.drawable.ic_image_x)
        }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            tint = Color.Unspecified,
            modifier =
                modifier.noRippleClickable {
                    onClick()
                },
            painter =
                if (isEnabled) {
                    painterResource(id = img)
                } else {
                    painterResource(id = img)
                },
            contentDescription = "image download",
        )
        Spacer(modifier = modifier.height(2.dp))
        Text(text = text, style = Typography.labelSmall.copy(fontSize = 10.sp))
    }
}

@Composable
@Preview(showBackground = true)
fun CreatePictureButtonPreview() {
    SemonemoTheme {
        CreatePictureButton()
    }
}
