package com.semonemo.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.semonemo.presentation.R
import com.semonemo.presentation.theme.Gray02
import com.semonemo.presentation.theme.GunMetal
import com.semonemo.presentation.theme.SemonemoTheme
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.theme.WhiteGray
import com.semonemo.presentation.util.noRippleClickable

/**
 * TODO
 *
 * @param modifier
 * @param keyword : 기본값
 * @param onValueChanged : 값 변화할 때 람다식 -> 검색어 자동 반영
 * @param onSearchAction : 검색 버튼 누를 경우
 * @param onClearPressed : x 버튼 누를 경우
 * @param placeHolder : 초기 텍스트 값
 * @param focusManager : 포커스 매니저
 */
@Composable
fun SearchTextField(
    modifier: Modifier = Modifier,
    keyword: String,
    onValueChanged: (String) -> Unit,
    onSearchAction: (String) -> Unit = {},
    onClearPressed: () -> Unit,
    placeHolder: String = stringResource(R.string.search_placeholder),
    focusManager: FocusManager,
) {
    OutlinedTextField(
        value = keyword,
        onValueChange = {
            onValueChanged(it)
        },
        placeholder = {
            if (keyword.isEmpty()) {
                Text(
                    text = placeHolder,
                    style = Typography.labelSmall,
                )
            }
        },
        modifier =
            modifier
                .height(45.dp),
        shape = RoundedCornerShape(10.dp),
        singleLine = true,
        keyboardOptions =
            KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search,
            ),
        keyboardActions =
            KeyboardActions(onSearch = {
                focusManager.clearFocus()
                onSearchAction(keyword)
            }),
        textStyle = Typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
        trailingIcon = {
            if (keyword.isEmpty().not()) {
                IconButton(
                    onClick = onClearPressed,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_search_cancel),
                        contentDescription = "clear_button",
                        tint = Color.Unspecified,
                    )
                }
            } else {
                Icon(
                    modifier = Modifier.noRippleClickable { },
                    painter = painterResource(id = R.drawable.ic_search_magnifier),
                    contentDescription = "search_button",
                    tint = Color.Unspecified,
                )
            }
        },
        colors =
            OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedContainerColor = WhiteGray,
                unfocusedContainerColor = WhiteGray,
                errorContainerColor = Color.Red,
                focusedTextColor = GunMetal,
                unfocusedTextColor = GunMetal,
                unfocusedPlaceholderColor = Gray02,
                focusedPlaceholderColor = Gray02,
            ),
    )
}

@Composable
@Preview
fun SearchTextFieldPreview() {
    SemonemoTheme {
        SearchTextField(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 10.dp),
            keyword = "",
            focusManager = LocalFocusManager.current,
            onClearPressed = {},
            onValueChanged = {}
        )
    }
}
