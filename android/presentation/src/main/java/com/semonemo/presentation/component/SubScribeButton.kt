package com.semonemo.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

/**
 * TODO
 *
 * @param isSubscribed : boolean 값
 * @param onToggleSubscription : boolean 변화 람다식
 */
@Composable
fun SubScribeButton(
    isSubscribed: Boolean,
    onToggleSubscription: () -> Unit,
) {
    val (text, img) =
        if (isSubscribed) {
            Pair("팔로잉", R.drawable.ic_toggle_subscription_on)
        } else {
            Pair("팔로우", R.drawable.ic_toggle_subscription_off)
        }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            tint = Color.Unspecified,
            modifier = Modifier.noRippleClickable { onToggleSubscription() },
            painter =
                if (isSubscribed) {
                    painterResource(id = img)
                } else {
                    painterResource(id = img)
                },
            contentDescription = if (isSubscribed) "팔로잉" else "팔로우",
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = text, style = Typography.labelSmall.copy(fontSize = 10.sp))
    }
}

@Composable
@Preview(showBackground = true)
fun SubScribeButtonPreview() {
    var isSubscribed by remember { mutableStateOf(false) }

    SemonemoTheme {
        SubScribeButton(
            isSubscribed = isSubscribed,
            onToggleSubscription = {
                isSubscribed = !isSubscribed
            },
        )
    }
}
