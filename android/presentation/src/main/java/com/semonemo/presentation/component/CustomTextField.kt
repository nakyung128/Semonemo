package com.semonemo.presentation.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.semonemo.presentation.R
import com.semonemo.presentation.theme.Gray02
import com.semonemo.presentation.theme.GunMetal
import com.semonemo.presentation.theme.Red
import com.semonemo.presentation.theme.SemonemoTheme
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.theme.WhiteGray

@Composable
fun CustomTextField(
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
    fontSize: Int = 13,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
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
                        style = Typography.labelSmall.copy(fontSize = fontSize.sp),
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
            textStyle = Typography.labelSmall.copy(fontSize = fontSize.sp),
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
            keyboardOptions =
                KeyboardOptions(
                    keyboardType = keyboardType,
                ),
            isError = errorMessage != "",
            colors =
                OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = borderColor,
                    unfocusedBorderColor = borderColor,
                    focusedContainerColor = containColor,
                    unfocusedContainerColor = containColor,
                    errorContainerColor = containColor,
                    focusedTextColor = GunMetal,
                    unfocusedTextColor = GunMetal,
                    unfocusedPlaceholderColor = Gray02,
                    focusedPlaceholderColor = Gray02,
                ),
        )
        Text(
            text = errorMessage,
            color = Red,
            style = Typography.labelMedium.copy(fontSize = fontSize.sp),
            modifier = Modifier.padding(top = 4.dp),
        )
    }
}

@Composable
@Preview()
fun CustomTextFieldPreview() {
    SemonemoTheme {
        CustomTextField(
            modifier = Modifier.fillMaxWidth(),
            focusManager = LocalFocusManager.current,
            onValueChange = {},
        )
    }
}
