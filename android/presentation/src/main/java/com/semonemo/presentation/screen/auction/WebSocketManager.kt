package com.semonemo.presentation.screen.auction

import android.util.Log
import com.semonemo.domain.model.AuctionBidLog
import com.semonemo.presentation.BuildConfig
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.StompSession
import org.hildan.krossbow.stomp.frame.FrameBody
import org.hildan.krossbow.stomp.frame.StompFrame
import org.hildan.krossbow.stomp.headers.StompSendHeaders
import org.hildan.krossbow.stomp.headers.StompSubscribeHeaders
import org.hildan.krossbow.websocket.okhttp.OkHttpWebSocketClient
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private const val TAG = "WebSocketManager"

class WebSocketManager {
    private val webSocketClient = OkHttpWebSocketClient()
    private val stompClient = StompClient(webSocketClient)

    private val moshi =
        Moshi
            .Builder()
            .add(KotlinJsonAdapterFactory())
            .add(LocalDateTimeAdapter())
            .build()

    // AuctionUpdate 어댑터 생성
    private val startMessageJsonAdapter = moshi.adapter(StartMessage::class.java).lenient()
    private val bidMessageJsonAdapter = moshi.adapter(BidMessage::class.java).lenient()
    private val endMessageJsonAdapter = moshi.adapter(EndMessage::class.java).lenient()

    // 현재 연결된 세션을 저장하기 위한 변수
    private var currentSession: StompSession? = null

    suspend fun connectToAuction(): StompSession {
        val url = "${BuildConfig.SEVER_URL}ws-stomp"
        currentSession = stompClient.connect(url)
        return stompClient.connect(url)
    }

    suspend fun sendBid(
        stompSession: StompSession,
        auctionId: Long,
        bidRequest: BidMessage,
    ) {
        Log.d(TAG, "sendBid: 보내기!!")
        // BidRequest를 JSON 문자열로 변환
        val bidRequestAdapter = moshi.adapter(BidMessage::class.java).lenient()
        val bidJson = bidRequestAdapter.toJson(bidRequest)
        val headers = StompSendHeaders("/app/auction/$auctionId/bid")
        // FrameBody로 감싸서 전송
        val frameBody = FrameBody.Text(bidJson)
        Log.d(TAG, "sendBid: frameBody ${frameBody.text}")

        // 변환된 JSON 문자열을 Stomp 메시지로 전송

        stompSession.send(headers, frameBody)
    }

    suspend fun subscribeToAuction(
        stompSession: StompSession,
        headersUri: String,
        onAuctionStart: (StartMessage) -> Unit = {},
        onBidUpdate: (BidMessage) -> Unit = {},
        onAuctionEnd: (EndMessage) -> Unit = {},
        onParticipants: (Int) -> Unit = {},
    ) {
        // StompSubscribeHeaders를 사용해 헤더 생성
        val headers = StompSubscribeHeaders(headersUri)
        Log.d(TAG, "구독 headersUri : $headersUri")

        // 구독하고 메시지를 처리
        stompSession.subscribe(headers).collect { message ->
            processMessage(
                message,
                onAuctionStart,
                onBidUpdate,
                onAuctionEnd,
                onParticipants,
            )
        }
    }

    private fun processMessage(
        message: StompFrame.Message,
        onAuctionStart: (StartMessage) -> Unit = {},
        onBidUpdate: (BidMessage) -> Unit = {},
        onAuctionEnd: (EndMessage) -> Unit = {},
        onParticipants: (Int) -> Unit = {},
    ) {
        val msgHeaders = message.headers
        val msgBody = message.body

        if (msgBody is FrameBody.Text) {
            val textHeaders = msgHeaders.destination
            val textBody = msgBody.text
            Log.d(TAG, "textHeaders: $textHeaders")
            Log.d(TAG, "textBody: $textBody")
            if (textHeaders.contains("start") || textBody.contains("start")) {
                runCatching {
                    startMessageJsonAdapter.fromJson(textBody)
                }.onSuccess { startMessage ->
                    if (startMessage != null) {
                        onAuctionStart(startMessage)
                    } else {
                        Log.e("AuctionProcess", "파싱 실패: startMessage")
                    }
                }.onFailure { exception ->
                    Log.e("AuctionProcess", "JSON 파싱 중 오류 발생", exception)
                }
            } else if (textHeaders.contains("end")) {
                runCatching {
                    endMessageJsonAdapter.fromJson(textBody)
                }.onSuccess { endMessage ->
                    if (endMessage != null) {
                        onAuctionEnd(endMessage)
                    } else {
                        Log.e("AuctionProcess", "파싱 실패: endMessage")
                    }
                }.onFailure { exception ->
                    Log.e("AuctionProcess", "JSON 파싱 중 오류 발생", exception)
                }
            } else if (textHeaders.contains("participants")) {
                Log.d(TAG, "참가자 수: $textBody")
                onParticipants(textBody.toInt())
            } else {
                runCatching {
                    Log.d(TAG, "processMessage: $textBody")
                    bidMessageJsonAdapter.fromJson(textBody)
                }.onSuccess { bidMessage ->
                    if (bidMessage != null) {
                        onBidUpdate(bidMessage)
                    } else {
                        Log.e("AuctionProcess", "파싱 실패: endMessage")
                    }
                }.onFailure { exception ->
                    Log.e("AuctionProcess", "JSON 파싱 중 오류 발생", exception)
                }
            }
        } else if (msgBody is FrameBody.Binary) {
            Log.e(TAG, "Binary frame received, expected text")
        }
    }
}

enum class MessageType {
    BID,
    ENTRANCE,
    AUCTION_STATUS,
}

fun BidMessage.toAuctionBidLog(): AuctionBidLog =
    AuctionBidLog(
        userId = this.userId,
        anonym = this.anonym,
        bidAmount = this.bidAmount,
        bidTime = this.bidTime,
        endTime = this.endTime,
    )

@JsonClass(generateAdapter = true)
data class StartMessage(
    val id: Long,
    val startPrice: Long,
    val currentBid: Long,
    val currentBidder: Long?,
    val startTime: LocalDateTime = LocalDateTime.now(),
    val endTime: LocalDateTime = LocalDateTime.now(),
)

@JsonClass(generateAdapter = true)
data class BidMessage(
    val userId: Long,
    val anonym: Int,
    val bidAmount: Long,
    val bidTime: LocalDateTime = LocalDateTime.now(),
    val endTime: LocalDateTime = LocalDateTime.now(),
)

@JsonClass(generateAdapter = true)
data class EndMessage(
    val auctionId: Long,
    val finalPrice: Long,
    val winner: Long?,
    val anonym: Int?,
    val endTime: LocalDateTime,
)

class LocalDateTimeAdapter {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    @ToJson
    fun toJson(value: LocalDateTime): String = value.format(formatter)

    @FromJson
    fun fromJson(value: String): LocalDateTime = LocalDateTime.parse(value, formatter)
}
