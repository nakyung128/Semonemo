package com.semonemo.presentation.component

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.semonemo.presentation.R
import com.semonemo.presentation.theme.SemonemoTheme


/**
 * TODO
 * @param popUpBackStack : 뒤로 가기 이벤트 람다
 */

@Composable
fun BackButton(popUpBackStack: () -> Unit = {}) {
    IconButton(onClick = popUpBackStack) {
        Icon(
            painter = painterResource(id = R.drawable.ic_btn_back),
            contentDescription = null,
        )
    }
}

@Composable
@Preview()
fun BackButtonPreview() {
    SemonemoTheme {
        BackButton()
    }
}
