package com.semonemo.presentation.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.semonemo.presentation.R
import com.semonemo.presentation.theme.Gray02
import com.semonemo.presentation.theme.Red
import com.semonemo.presentation.theme.SemonemoTheme
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.theme.WhiteGray

@Composable
fun CustomPasswordTextField(
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    input: String = "",
    focusManager: FocusManager,
    errorMessage: String = "",
    onClearPressed: () -> Unit = {},
    containColor: Color = WhiteGray,
    borderColor: Color = Color.Transparent,
    roundDp: Int = 10,
) {
    val (passwordVisible, setPasswordVisible) =
        remember {
            mutableStateOf(false)
        }
    Column(modifier = modifier) {
        OutlinedTextField(
            value = input,
            onValueChange = {
                onValueChange(it)
            },
            placeholder = {
                if (input.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = Typography.labelSmall,
                    )
                }
            },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .border(2.dp, borderColor, RoundedCornerShape(roundDp.dp)),
            shape = RoundedCornerShape(roundDp.dp),
            singleLine = true,
            keyboardActions =
                KeyboardActions(onDone = {
                    focusManager.clearFocus()
                }),
            textStyle = Typography.labelSmall,
            trailingIcon = {
                IconButton(
                    modifier = Modifier.testTag("clear_button"),
                    onClick = onClearPressed,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_search_cancel),
                        contentDescription = "clear_button",
                        tint = Color.Unspecified,
                    )
                }
            },
            isError = errorMessage != "",
            colors =
                OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = borderColor,
                    unfocusedBorderColor = borderColor,
                    focusedContainerColor = containColor,
                    unfocusedContainerColor = containColor,
                    errorContainerColor = containColor,
                    focusedTextColor = Gray02,
                    unfocusedTextColor = Gray02,
                    unfocusedPlaceholderColor = Gray02,
                    focusedPlaceholderColor = Gray02,
                ),
            visualTransformation =
                if (passwordVisible.not()) {
                    PasswordVisualTransformation()
                } else {
                    VisualTransformation.None
                },
            leadingIcon = {
                IconButton(onClick = { setPasswordVisible(passwordVisible.not()) }) {
                    Icon(
                        painter =
                            painterResource(
                                id = if (passwordVisible) R.drawable.ic_visibility_on else R.drawable.ic_visibility_off,
                            ),
                        contentDescription = null,
                    )
                }
            },
        )
        Text(
            text = errorMessage,
            color = Red,
            style = Typography.labelMedium,
            modifier = Modifier.padding(top = 4.dp), // 간격 조절
        )
    }
}

@Composable
@Preview()
fun CustomPasswordTextFieldPreview() {
    SemonemoTheme {
        CustomPasswordTextField(
            modifier = Modifier.fillMaxWidth(),
            focusManager = LocalFocusManager.current,
            onValueChange = {},
        )
    }
}
