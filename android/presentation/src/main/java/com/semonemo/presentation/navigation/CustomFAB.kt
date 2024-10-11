package com.semonemo.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.semonemo.presentation.R
import com.semonemo.presentation.theme.Main02

@Composable
fun CustomFAB(
    onClick: () -> Unit,
    icon: Int,
    iconColor: Color,
    labelColor: Color,
    labelStyle: TextStyle,
) {
    Column(
        modifier =
            Modifier
                .wrapContentSize()
                .offset(y = 70.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier =
                Modifier
                    .size(65.dp)
                    .background(
                        brush = Main02,
                        shape = CircleShape,
                    ),
            contentAlignment = Alignment.Center,
        ) {
            IconButton(
                onClick = onClick,
            ) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = stringResource(id = R.string.moment_title),
                    tint = iconColor,
                )
            }
        }
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = stringResource(id = R.string.moment_title),
            style = labelStyle,
            color = labelColor,
        )
    }
}
