package com.semonemo.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.semonemo.presentation.R
import com.semonemo.presentation.theme.GunMetal
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.theme.WhiteGray
import com.semonemo.presentation.util.noRippleClickable
import kotlinx.coroutines.delay

/**
 * 스토어 탭에서 보여지는 FloatingActionButton
 * @param modifier
 */
@Composable
fun CustomStoreFAB(
    modifier: Modifier = Modifier,
    navigateToFrameSale: () -> Unit = {},
    navigateToAssetSale: () -> Unit = {},
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.End,
            modifier = modifier.padding(16.dp),
        ) {
            CustomStoreSubFAB(
                visible = expanded,
                delayMillis = 100,
                textLabel = stringResource(id = R.string.fab_label_sell_asset),
                imgRes = R.drawable.ic_fab_asset,
                onClick = navigateToAssetSale,
            )
            CustomStoreSubFAB(
                visible = expanded,
                delayMillis = 0,
                textLabel = stringResource(id = R.string.fab_label_sell_frame),
                imgRes = R.drawable.ic_fab_frame,
                onClick = navigateToFrameSale,
            )

            // 메인 FAB 버튼
            FloatingActionButton(
                onClick = { expanded = !expanded },
                containerColor = GunMetal,
                shape = CircleShape,
            ) {
                Icon(
                    painter =
                        painterResource(
                            // FAB 상태에 따라 아이콘 변경
                            if (expanded) {
                                R.drawable.ic_fab_sell_off
                            } else {
                                R.drawable.ic_fab_sell_on
                            },
                        ),
                    contentDescription = null,
                    tint = WhiteGray,
                )
            }
            Spacer(modifier = Modifier.fillMaxHeight(0.09f))
        }
    }
}

@Composable
fun CustomStoreSubFAB(
    modifier: Modifier = Modifier,
    visible: Boolean,
    delayMillis: Int,
    textLabel: String,
    imgRes: Int,
    onClick: () -> Unit = {},
) {
    var isVisible by remember { mutableStateOf(false) }
    // 애니메이션 지연
    LaunchedEffect(visible) {
        if (visible) {
            delay(delayMillis.toLong())
            isVisible = true
        } else {
            isVisible = false
        }
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
    ) {
        Row(
            modifier =
                modifier
                    .padding(end = 10.dp)
                    .noRippleClickable {
                        onClick()
                    },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = textLabel,
                color = WhiteGray,
                style = Typography.labelMedium,
                modifier =
                    modifier
                        .background(GunMetal, shape = RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier =
                    modifier
                        .size(32.dp)
                        .background(GunMetal, shape = CircleShape)
                        .noRippleClickable {
                            onClick()
                        },
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(id = imgRes),
                    contentDescription = null,
                    tint = WhiteGray,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CustomStoreFABPreview() {
    CustomStoreFAB()
}
