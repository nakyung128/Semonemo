package com.semonemo.presentation.screen.wallet.subscreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.semonemo.presentation.component.CustomTextField
import com.semonemo.presentation.theme.Gray01
import com.semonemo.presentation.theme.GunMetal
import com.semonemo.presentation.theme.SemonemoTheme
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.theme.White

@Composable
fun WalletDialog(
    title: String = "",
    onConfirmMessage: String = "",
    onDismissMessage: String = "",
    onConfirm: (Long) -> Unit = {},
    onDismiss: () -> Unit = {},
    focusManager: FocusManager = LocalFocusManager.current,
) {
    var price =
        remember {
            mutableLongStateOf(0L)
        }
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
        content = {
            Surface(
                modifier =
                    Modifier
                        .width(323.dp)
                        .height(204.dp)
                        .padding(10.dp),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(width = 1.dp, color = Gray01),
                color = Color.White,
            ) {
                Column(
                    modifier = Modifier.padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = title, style = Typography.labelLarge.copy(fontSize = 17.sp))
                    Spacer(modifier = Modifier.weight(1f))
                    CustomTextField(
                        modifier = Modifier.height(50.dp),
                        placeholder = "0",
                        input = if (price.longValue != 0L) price.longValue.toString() else "",
                        onValueChange = {
                            price.longValue =
                                if (it.isEmpty()) {
                                    0L
                                } else {
                                    it.toLong()
                                }
                        },
                        focusManager = focusManager,
                        keyboardType = KeyboardType.Number,
                        onClearPressed = {
                            price.longValue = 0
                        },
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Row {
                        Button(
                            modifier =
                                Modifier
                                    .width(130.dp)
                                    .height(40.dp),
                            border = BorderStroke(width = (1.5).dp, color = Gray01),
                            shape = RoundedCornerShape(10.dp),
                            onClick = onDismiss,
                            colors =
                                ButtonDefaults.buttonColors(
                                    contentColor = GunMetal,
                                    containerColor = White,
                                ),
                        ) {
                            Text(
                                text = onDismissMessage,
                                style = Typography.bodySmall.copy(fontSize = 14.sp),
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Button(
                            modifier =
                                Modifier
                                    .width(130.dp)
                                    .height(40.dp),
                            onClick = { onConfirm(price.value) },
                            shape = RoundedCornerShape(10.dp),
                            colors =
                                ButtonDefaults.buttonColors(
                                    contentColor = White,
                                    containerColor = Gray01,
                                ),
                        ) {
                            Text(
                                text = onConfirmMessage,
                                style = Typography.bodySmall.copy(fontSize = 14.sp),
                            )
                        }
                    }
                }
            }
        },
    )
}

@Composable
@Preview(showBackground = true)
fun WalletDialogPreview() {
    SemonemoTheme {
        WalletDialog(
            title = "보유 코인을 환전하시겠습니까?",
            onDismissMessage = "취소",
            onConfirmMessage = "변경",
        )
    }
}
