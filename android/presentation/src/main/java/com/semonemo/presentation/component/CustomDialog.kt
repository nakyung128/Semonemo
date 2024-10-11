package com.semonemo.presentation.component

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.semonemo.presentation.theme.Gray01
import com.semonemo.presentation.theme.GunMetal
import com.semonemo.presentation.theme.SemonemoTheme
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.theme.White

/**
 * TODO
 *
 * @param content  : 내용
 * @param title   : 제목
 * @param onConfirmMessage : 확인 버튼 메시지
 * @param onDismissMessage : 취소 버튼 메시지
 * @param onConfirm : 확인 람다식
 * @param onDismiss : 취소 람다식
 * @param titleKeywords : 제목 강조 키워드
 * @param titleBrushFlag : 제목 브러시 여부
 * @param contentKeywords : 내용 강조 키워드
 * @param contentBrushFlag : 내용 브러시 여부
 */
@Composable
fun CustomDialog(
    content: String = "",
    title: String = "",
    onConfirmMessage: String = "",
    onDismissMessage: String = "",
    onConfirm: () -> Unit = {},
    onDismiss: () -> Unit = {},
    titleKeywords: List<String> = listOf(),
    titleBrushFlag: List<Boolean> = listOf(),
    contentKeywords: List<String> = listOf(),
    contentBrushFlag: List<Boolean> = listOf(),
) {
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
                    BoldTextWithKeywords(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        fullText = title,
                        keywords = titleKeywords,
                        brushFlag = titleBrushFlag,
                        boldStyle = Typography.bodyMedium.copy(fontSize = 18.sp),
                        normalStyle = Typography.labelMedium.copy(fontSize = 18.sp),
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    BoldTextWithKeywords(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        fullText = content,
                        keywords = contentKeywords,
                        brushFlag = contentBrushFlag,
                        boldStyle = Typography.bodyMedium.copy(fontSize = 14.sp),
                        normalStyle = Typography.labelMedium.copy(fontSize = 14.sp),
                        alignStyle = TextAlign.Center,
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
                            onClick = onConfirm,
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
fun CustomDialogPreview() {
    SemonemoTheme {
        CustomDialog(
            title = "해당 NFT를 공개로 변경하시겠습니까?",
            content =
                "NFT 공개 변경 시 사용자들에게 판매할 수 있으며,\n" +
                    "원할 때 언제든 다시 비공개로 변경할 수 있어요.",
            onDismissMessage = "취소",
            onConfirmMessage = "변경",
            contentKeywords = listOf("판매", "변경"),
            contentBrushFlag = listOf(false, false),
            titleKeywords = listOf("공개로", "변경"),
            titleBrushFlag = listOf(false, false),
        )
    }
}
