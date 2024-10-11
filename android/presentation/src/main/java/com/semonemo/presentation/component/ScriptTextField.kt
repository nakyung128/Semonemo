package com.semonemo.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.semonemo.presentation.theme.Gray02
import com.semonemo.presentation.theme.GunMetal
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.theme.WhiteGray

/**
 * TODO
 *
 * @param modifier
 * @param placeholder : 플레이스홀더 텍스트
 * @param height : 텍스트필드의 최소 높이
 * @param fontSize : 폰트 사이즈
 * @param value : 입력된 텍스트
 * @param onValueChange : 입력 후 변화
 * @param focusManager : 포커스매니저
 */
@Composable
fun ScriptTextField(
    modifier: Modifier = Modifier,
    placeholder: String,
    height: Int,
    fontSize: Int = 13,
    value: String,
    onValueChange: (String) -> Unit,
    focusManager: FocusManager,
) {
    Box(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            modifier =
            Modifier
                .fillMaxWidth()
                .heightIn(min = height.dp),
            value = value,
            onValueChange = {
                onValueChange(it)
            },
            textStyle = Typography.labelSmall.copy(fontSize = fontSize.sp),
            placeholder = {
                Text(
                    modifier = Modifier.align(Alignment.CenterStart),
                    text = placeholder,
                    style = Typography.labelSmall.copy(fontSize = fontSize.sp),
                    color = Gray02,
                )
            },
            keyboardOptions =
                KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                ),
            keyboardActions =
                KeyboardActions(onDone = {
                    focusManager.clearFocus()
                }),
            shape = RoundedCornerShape(10.dp),
            colors =
                OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = GunMetal,
                    unfocusedTextColor = GunMetal,
                    focusedContainerColor = WhiteGray,
                    unfocusedContainerColor = WhiteGray,
                ),
        )
    }
}
