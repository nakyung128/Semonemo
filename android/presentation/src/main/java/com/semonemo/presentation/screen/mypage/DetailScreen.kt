package com.semonemo.presentation.screen.mypage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.semonemo.presentation.R
import com.semonemo.presentation.component.CustomDialog
import com.semonemo.presentation.component.HashTag
import com.semonemo.presentation.component.LongWhiteButton
import com.semonemo.presentation.component.PrivateTag
import com.semonemo.presentation.theme.Main01
import com.semonemo.presentation.theme.SemonemoTheme
import com.semonemo.presentation.theme.Typography

// 다이얼로그 타입
enum class DialogType {
    PUBLIC,
    SALE,
}

/**
 * TODO
 *
 * @param isPrivate : 비공개 - true, 공개 - false | 비공개일 경우, 판매 불가능 (자동으로 비판매)
 * @param isNotSale : 비판매 - true, 판매 가능 - false
 * @param onPublicClicked : 공개 NFT로 변경하기 버튼 클릭 시
 * @param onSaleClicked : 판매 가능한 NFT로 변경하기 버튼 클릭 시
 */
@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    nftId: Long? = null,
    onPublicClicked: () -> Unit = {},
    onSaleClicked: () -> Unit = {},
) {
    val scrollState = rememberScrollState()
    var showDialog by remember { mutableStateOf<DialogType?>(null) }

    val isPrivate = false
    val isNotSale = true

    // 더미 데이터
    val tags = listOf("윈터", "연예인", "에스파", "에스파윈터", "연예인프레임")

    @Composable
    fun showCustomDialog(
        title: String,
        content: String,
        onConfirmClicked: () -> Unit,
        titleKeywords: List<String> = emptyList(),
        contentKeywords: List<String> = emptyList(),
    ) {
        CustomDialog(
            title = title,
            content = content,
            onConfirmMessage = stringResource(R.string.mypage_change_title),
            onDismissMessage = stringResource(R.string.mypage_cancel_tag),
            titleKeywords = titleKeywords,
            contentKeywords = contentKeywords,
            titleBrushFlag = titleKeywords.map { false },
            contentBrushFlag = contentKeywords.map { false },
            onConfirm = {
                showDialog = null // 다이얼로그 닫기
                onConfirmClicked()
            },
            onDismiss = {
                showDialog = null
            },
        )
    }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(brush = Main01)
                .verticalScroll(state = scrollState)
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 25.dp, vertical = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "윈터와 함께~ ❤️",
                style = Typography.bodyLarge,
            )
            if (isPrivate) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd,
                ) {
                    PrivateTag(title = stringResource(R.string.private_tag))
                }
            } else {
                if (isNotSale) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterEnd,
                    ) {
                        PrivateTag(title = stringResource(R.string.not_sale_tag))
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
        ) {
            Image(
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillWidth,
                painter = painterResource(id = R.drawable.img_example3),
                contentDescription = "frame_img",
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                modifier =
                    Modifier
                        .size(25.dp)
                        .clip(CircleShape),
                painter = painterResource(id = R.drawable.img_example),
                contentDescription = "img_profile",
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "나갱갱",
                style = Typography.bodySmall.copy(fontSize = 15.sp),
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterStart,
        ) {
            Text(
                text = "윈터와 함께 사진을 찍고 싶을 때 사용하는 프레임! >\"<",
                style = Typography.labelMedium.copy(fontSize = 18.sp),
            )
        }
        Spacer(modifier = Modifier.height(13.dp))
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            content = {
                items(count = tags.size) { index ->
                    HashTag(keyword = tags[index])
                }
            },
        )
        Spacer(modifier = Modifier.height(55.dp))
        if (isPrivate) {
            LongWhiteButton(
                modifier = Modifier.fillMaxWidth(),
                icon = null,
                text = stringResource(R.string.change_to_public_nft),
                onClick = { showDialog = DialogType.PUBLIC },
            )
        } else {
            if (isNotSale) {
                LongWhiteButton(
                    modifier = Modifier.fillMaxWidth(),
                    icon = null,
                    text = stringResource(R.string.change_to_private_nft),
                    onClick = { showDialog = DialogType.PUBLIC },
                )
                Spacer(modifier = Modifier.height(8.dp))
                LongWhiteButton(
                    modifier = Modifier.fillMaxWidth(),
                    icon = null,
                    text = stringResource(R.string.change_to_sale_nft),
                    onClick = { showDialog = DialogType.SALE },
                )
            } else {
                LongWhiteButton(
                    modifier = Modifier.fillMaxWidth(),
                    icon = null,
                    text = stringResource(id = R.string.change_to_private_nft),
                    onClick = { showDialog = DialogType.PUBLIC },
                )
                Spacer(modifier = Modifier.height(8.dp))
                LongWhiteButton(
                    modifier = Modifier.fillMaxWidth(),
                    icon = null,
                    text = stringResource(R.string.change_to_not_sale_nft),
                    onClick = { showDialog = DialogType.SALE },
                )
            }
        }
    }
    when (showDialog) {
        DialogType.PUBLIC -> {
            val (title, content) =
                if (isPrivate) {
                    stringResource(R.string.public_dialog_title) to stringResource(R.string.public_dialog_content)
                } else {
                    stringResource(R.string.private_dialog_title) to stringResource(R.string.private_dialog_content)
                }

            val (titleKeywords, contentKeywords) =
                if (isPrivate) {
                    listOf("공개로 변경") to listOf("판매", "비공개로 변경")
                } else {
                    listOf("비공개로 변경") to listOf("볼 수 없으며", "공개로 변경")
                }

            showCustomDialog(
                title = title,
                content = content,
                onConfirmClicked = onPublicClicked,
                titleKeywords = titleKeywords,
                contentKeywords = contentKeywords,
            )
        }

        DialogType.SALE -> {
            val (title, content) =
                if (isNotSale) {
                    stringResource(R.string.sale_dialog_title) to stringResource(R.string.sale_dialog_content)
                } else {
                    stringResource(R.string.not_sale_dialog_title) to stringResource(R.string.not_sale_dialog_content)
                }

            val (titleKeywords, contentKeywords) =
                if (isNotSale) {
                    listOf("판매") to listOf("구매할 수 있게", "비판매로 변경")
                } else {
                    listOf("비판매") to listOf("구매할 수 없게", "판매로 변경")
                }

            showCustomDialog(
                title = title,
                content = content,
                onConfirmClicked = onSaleClicked,
                titleKeywords = titleKeywords,
                contentKeywords = contentKeywords,
            )
        }

        null -> {}
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun DetailScreenPreview() {
    SemonemoTheme {
        DetailScreen()
    }
}
