package com.semonemo.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.semonemo.presentation.R
import com.semonemo.presentation.theme.Gray01
import com.semonemo.presentation.theme.Gray03
import com.semonemo.presentation.theme.GunMetal
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.theme.White
import com.semonemo.presentation.theme.WhiteGray

/**
 * CustomDropDownMenu
 * tabList에 메뉴 이름들을 List 형태로 넣어 주면 됨.
 * 사용 예시는 프리뷰에
 */

// style
data class CustomDropdownMenuStyles(
    val height: Dp = 35.dp,
    val mainColor: Color = WhiteGray,
    val strokeColor: Color = Gray03,
    val containerColor: Color = White,
    val cornerRadius: Dp = 10.dp,
    val verticalPadding: Dp = 8.dp,
    val horizontalPadding: Dp = 10.dp,
    val borderStroke: BorderStroke = BorderStroke(1.dp, strokeColor),
    val textStyle: TextStyle = Typography.bodySmall.copy(fontSize = 10.sp, color = GunMetal),
    val menuItemTextStyle: TextStyle = Typography.labelSmall.copy(fontSize = 10.sp, color = Gray01),
    val menuItemHeight: Dp = 20.dp,
    val iconColor: Color = GunMetal,
    val iconDrawableId: Int = R.drawable.ic_arrow_down,
    val expandedIconDrawableId: Int = R.drawable.ic_arrow_up,
)

fun CustomDropdownMenuStyles.getIconDrawable(expanded: Boolean): Int = if (expanded) expandedIconDrawableId else iconDrawableId

@Composable
fun CustomDropdownMenu(
    modifier: Modifier = Modifier,
    menuItems: List<Pair<String, () -> Unit>>,
    styles: CustomDropdownMenuStyles = CustomDropdownMenuStyles(),
) {
    val expanded = remember { mutableStateOf(false) }
    val selectedMenu = remember { mutableStateOf(menuItems[0].first) }

    Column(modifier = modifier) {
        OutlinedDropdownButton(
            buttonText = selectedMenu.value,
            iconDrawableId = styles.getIconDrawable(expanded.value),
            styles = styles,
        ) {
            expanded.value = true
        }
        Spacer(modifier = Modifier.height(4.dp))
        DropdownMenu(
            modifier =
                Modifier
                    .wrapContentWidth()
                    .background(color = Color.Unspecified),
            shape = RoundedCornerShape(styles.cornerRadius),
            containerColor = Color.White,
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false },
        ) {
            menuItems.forEach { (label, onClick) ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = label,
                            style = if (label != selectedMenu.value) styles.menuItemTextStyle else styles.textStyle,
                        )
                    },
                    onClick = {
                        expanded.value = false
                        selectedMenu.value = label
                        onClick()
                    },
                    modifier =
                        Modifier
                            .wrapContentWidth()
                            .height(styles.menuItemHeight),
                )
            }
        }
    }
}

@Composable
fun OutlinedDropdownButton(
    buttonText: String,
    iconDrawableId: Int?,
    styles: CustomDropdownMenuStyles,
    onClick: () -> Unit,
) {
    OutlinedButton(
        onClick = onClick,
        modifier =
            Modifier
                .height(styles.height)
                .wrapContentWidth(),
        border = styles.borderStroke,
        shape = RoundedCornerShape(styles.cornerRadius),
        colors =
            ButtonDefaults.outlinedButtonColors(
                containerColor = styles.containerColor,
            ),
        contentPadding =
            PaddingValues(
                horizontal = styles.horizontalPadding,
                vertical = styles.verticalPadding,
            ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = buttonText, style = styles.textStyle)
            iconDrawableId?.let {
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    painter = painterResource(id = it),
                    contentDescription = null,
                    tint = styles.iconColor,
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CustomDropdownMenuPreview() {
    CustomDropdownMenu(
        menuItems =
            listOf(
                "보유중" to {},
                "판매중" to {},
            ),
        styles = CustomDropdownMenuStyles(),
    )
}
