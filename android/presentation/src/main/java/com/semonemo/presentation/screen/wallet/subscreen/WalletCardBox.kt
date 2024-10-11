package com.semonemo.presentation.screen.wallet.subscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.semonemo.domain.model.Coin
import com.semonemo.presentation.R
import com.semonemo.presentation.component.BoldTextWithKeywords
import com.semonemo.presentation.screen.wallet.BootPaymentScreen
import com.semonemo.presentation.theme.Gray03
import com.semonemo.presentation.theme.Main01
import com.semonemo.presentation.theme.Main02
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.util.noRippleClickable
import java.util.Locale

@Composable
fun WalletCardBox(
    modifier: Modifier = Modifier,
    userName: String,
    userCoin: Coin,
    onShowSnackBar: (String) -> Unit,
    sendExchangePayableTransaction: (String) -> Unit,
    sendExchangeCoinTransaction: (String) -> Unit,
    buyCoin: (Long) -> Unit = {},
) {
    val (showExchangeCoinPayable, setShowExchangeCoinPayable) =
        remember {
            mutableStateOf(false)
        }
    val (showExchangePayableCoin, setShowExchangePayableCoin) =
        remember {
            mutableStateOf(false)
        }

    val (showBuyCoin, setShowBuyCoin) =
        remember {
            mutableStateOf(false)
        }
    val isPay =
        remember {
            mutableStateOf(false)
        }

    val amount =
        remember {
            mutableLongStateOf(0L)
        }

    val price =
        remember {
            mutableLongStateOf(100L)
        }

    if (isPay.value) {
        BootPaymentScreen(
            coinAmount = amount.value,
            price = price.value,
            onSuccess = {
                isPay.value = false
                buyCoin(amount.value)
            },
            onClose = {
                isPay.value = false
            },
        ) {
        }
    }

    if (showExchangeCoinPayable) { // coin -> payable
        WalletDialog(
            title = "보유 코인을 페이로 환전하시겠습니까?",
            onDismissMessage = "취소",
            onConfirmMessage = "환전",
            onConfirm = {
                if (userCoin.coinBalance < it) {
                    onShowSnackBar("잔여코인이 부족합니다")
                } else {
                    sendExchangePayableTransaction(it.toString())
                }
                setShowExchangeCoinPayable(false)
            },
            onDismiss = {
                setShowExchangeCoinPayable(false)
            },
        )
    }

    if (showExchangePayableCoin) { // payable -> coin
        WalletDialog(
            title = "보유 페이를 코인으로 환전하시겠습니까?",
            onDismissMessage = "취소",
            onConfirmMessage = "환전",
            onConfirm = { it ->
                if (userCoin.payableBalance < it) {
                    onShowSnackBar("잔여 페이가 부족합니다")
                } else {
                    sendExchangeCoinTransaction(it.toString())
                }
                setShowExchangePayableCoin(false)
            },
            onDismiss = {
                setShowExchangePayableCoin(false)
            },
        )
    }

    if (showBuyCoin) {
        WalletDialog(
            title = "코인을 충전하시겠습니까?",
            onDismissMessage = "취소",
            onConfirmMessage = "충전",
            onConfirm = {
                amount.value = it
                isPay.value = true
                setShowBuyCoin(false)
            },
            onDismiss = {
                setShowBuyCoin(false)
            },
        )
    }

    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(brush = Main02, shape = RoundedCornerShape(10.dp)),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .wrapContentSize()
                    .padding(vertical = 12.dp, horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterStart,
            ) {
                Row(
                    modifier = Modifier.wrapContentSize(),
                    verticalAlignment = Alignment.Top,
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(id = R.drawable.img_money),
                        contentDescription = "wallet_money",
                        tint = Color.Unspecified,
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Column(
                        modifier = Modifier.wrapContentSize(),
                        horizontalAlignment = Alignment.Start,
                    ) {
                        BoldTextWithKeywords(
                            fullText = "$userName 님의 지갑",
                            keywords = listOf(userName),
                            brushFlag = listOf(true),
                            boldStyle = Typography.titleSmall.copy(fontSize = 20.sp),
                            normalStyle =
                                Typography.labelLarge.copy(
                                    fontSize = 20.sp,
                                    color = Color.White,
                                ),
                            brushColor = Main01,
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(
                            modifier = Modifier.wrapContentSize(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "현재 환전 가능한 코인이",
                                style = Typography.labelSmall.copy(fontSize = 10.sp),
                                color = Gray03,
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text =
                                    String.format(
                                        Locale.KOREAN,
                                        "%,.0f",
                                        userCoin.coinBalance.toDouble(),
                                    ) + stringResource(R.string.coin_unit_name),
                                style = Typography.bodySmall.copy(fontSize = 10.sp),
                                color = Color.White,
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "있어요",
                                style = Typography.labelSmall.copy(fontSize = 10.sp),
                                color = Gray03,
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier.wrapContentSize(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text =
                        String.format(
                            Locale.KOREAN,
                            "%,.0f",
                            userCoin.payableBalance.toDouble(),
                        ),
                    style = Typography.titleSmall.copy(fontSize = 28.sp),
                    color = Color.White,
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "${stringResource(R.string.coin_unit_name)}(Pay)",
                    style = Typography.labelMedium,
                    color = Color.White,
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 35.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    modifier = Modifier.noRippleClickable { setShowExchangeCoinPayable(true) },
                    text = "페이로 환전",
                    style = Typography.labelSmall.copy(fontSize = 12.sp),
                    color = Color.White,
                )
                VerticalDivider(modifier = Modifier.height(11.dp))
                Text(
                    modifier = Modifier.noRippleClickable { setShowExchangePayableCoin(true) },
                    text = "코인으로 환전",
                    style = Typography.labelSmall.copy(fontSize = 12.sp),
                    color = Color.White,
                )
                VerticalDivider(modifier = Modifier.height(11.dp))
                Text(
                    modifier = Modifier.noRippleClickable { setShowBuyCoin(true) },
                    text = "충전하기",
                    style = Typography.labelSmall.copy(fontSize = 12.sp),
                    color = Color.White,
                )
            }
        }
    }
}

@Preview
@Composable
fun WalletPreview() {
    WalletCardBox(
        userName = "짜이한",
        userCoin = Coin(coinBalance = 1000, payableBalance = 100000),
        onShowSnackBar = {},
        sendExchangePayableTransaction = {},
        sendExchangeCoinTransaction = {},
    )
}
