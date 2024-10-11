package com.semonemo.presentation.screen.wallet

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.semonemo.presentation.BuildConfig
import kr.co.bootpay.android.Bootpay
import kr.co.bootpay.android.events.BootpayEventListener
import kr.co.bootpay.android.models.BootExtra
import kr.co.bootpay.android.models.BootUser
import kr.co.bootpay.android.models.Payload
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random

@Composable
fun BootPaymentScreen(
    coinAmount: Long,
    price: Long,
    onSuccess: () -> Unit,
    onClose: () -> Unit,
    onError: (String) -> Unit,
) {
    val context = LocalContext.current
    val date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
    val randomNum = Random.nextInt(100000, 999999)
    val orderId = "$date$randomNum"
    val payload = Payload()
    val extra =
        BootExtra()
            .setCardQuota("0,2,3")
    payload
        .setApplicationId(BuildConfig.BOOTPAY_API_KEY)
        .setOrderName("Aho 코인${coinAmount}개 구매하기")
        .setOrderId(orderId)
        .setPrice(price.toDouble())
        .setUser(getBootUser())
        .setExtra(extra)
    val bootpay = Bootpay.init(context as Activity, context)

    bootpay
        .setPayload(payload)
        .setEventListener(
            object : BootpayEventListener {
                override fun onCancel(data: String?) {
                    Bootpay.removePaymentWindow()
                    Bootpay.removePaymentWindow()
                }

                override fun onError(data: String?) {
                    Bootpay.removePaymentWindow()
                    onError(data.toString())
                }

                override fun onClose() {
                    Bootpay.removePaymentWindow()
                    onClose()
                }

                override fun onIssued(data: String?) {
                    Bootpay.removePaymentWindow()
                }

                override fun onConfirm(data: String?): Boolean = true

                override fun onDone(data: String?) {
                    onSuccess()
                }
            },
        ).requestPayment()
}

fun getBootUser(): BootUser {
    val user = BootUser()
    user.id = BuildConfig.USER_EMAIL
    user.area = "대한민국"
    user.addr = "대한민국"
    user.email = BuildConfig.USER_EMAIL
    user.phone = BuildConfig.USER_PHONE
    user.username = BuildConfig.USER_NAME
    return user
}
