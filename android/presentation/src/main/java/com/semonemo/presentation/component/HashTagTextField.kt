package com.semonemo.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.semonemo.presentation.R
import com.semonemo.presentation.theme.Gray02
import com.semonemo.presentation.theme.GunMetal
import com.semonemo.presentation.theme.SemonemoTheme
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.theme.WhiteGray

/**
 * TODO
 *
 * @param modifier
 * @param onTagAddAction : 키보드 Done 누르거나, 화면 바깥 누른 경우
 * @param placeHolder : 초기 텍스트 값
 * @param focusManager : 포커스 매니저
 */
@Composable
fun HashTagTextField(
    modifier: Modifier = Modifier,
    onTagAddAction: (String) -> Unit = {},
    placeHolder: String = stringResource(R.string.tag_placeholder),
    focusManager: FocusManager,
) {
    val (keyword, setKeyword) = remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    OutlinedTextField(
        value = keyword,
        onValueChange = {
            setKeyword(it)
        },
        placeholder = {
            if (keyword.isEmpty()) {
                Text(
                    text = placeHolder,
                    style = Typography.labelSmall.copy(fontSize = 14.sp),
                )
            }
        },
        modifier =
            modifier
                .fillMaxWidth()
                .height(48.dp)
                .focusRequester(focusRequester = focusRequester),
        shape = RoundedCornerShape(10.dp),
        singleLine = true,
        keyboardOptions =
            KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
            ),
        keyboardActions =
            KeyboardActions(onDone = {
                focusManager.clearFocus()
                onTagAddAction(keyword)
                setKeyword("")
            }),
        textStyle = Typography.labelSmall.copy(fontSize = 14.sp, fontWeight = FontWeight.SemiBold),
        leadingIcon = {
            Icon(
                modifier = Modifier.size(25.dp),
                painter = painterResource(id = R.drawable.ic_hashtag),
                contentDescription = "tag_icon",
                tint = Color.Unspecified,
            )
        },
        trailingIcon = {
            if (keyword.isEmpty().not()) {
                IconButton(
                    onClick = { setKeyword("") },
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_search_cancel),
                        contentDescription = "clear_button",
                        tint = Color.Unspecified,
                    )
                }
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
fun HashTagTextFieldPreview() {
    val (keyword, setKeyword) = remember { mutableStateOf("") }

    SemonemoTheme {
        HashTagTextField(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 10.dp),
            focusManager = LocalFocusManager.current,
        )
    }
}
