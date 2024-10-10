package com.semonemo.presentation.screen.auction

import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.semonemo.domain.datasource.AuthDataSource
import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.model.AuctionBidLog
import com.semonemo.domain.repository.AuctionRepository
import com.semonemo.domain.repository.CoinRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.hildan.krossbow.stomp.StompSession
import java.time.LocalDateTime
import javax.inject.Inject
import kotlin.math.log10
import kotlin.math.pow

private const val TAG = "AuctionDetailViewModel"

@HiltViewModel
class AuctionDetailViewModel
    @Inject
    constructor(
        private val auctionRepository: AuctionRepository,
        private val coinRepository: CoinRepository,
        private val saveStateHandle: SavedStateHandle,
        private val authDataSource: AuthDataSource,
    ) : ViewModel() {
        // 경매 상태 관리
        var auctionStatus = mutableStateOf(AuctionStatus.READY)
            private set

        // 유저 상태 관리
        var userStatus = mutableStateOf(UserStatus.NOT_READY)
            private set

        // 항상 새로운 메세지 관리
        var bidMessageJob: Job? = null

        // 입찰 관찰자
        var observedBidMessage = mutableStateOf(false)
            private set

        // 유저 입찰 여부
        var observedBidSubmit = mutableStateOf(false)
            private set
        var webSocketManager = mutableStateOf<WebSocketManager?>(null)
            private set
        var stompSession = mutableStateOf<StompSession?>(null)
            private set
        var auctionId = saveStateHandle["auctionId"] ?: -1L // 경매 번호
            private set

        var validateUserBid = mutableStateOf(true)

        var userId: Long = -1L // 유저 ID
        var registerId: Long = 0L // 경매 등록자 ID
        var userAnonym: Int = 0 // 유저 익명 번호
            private set
        var userCoinBalance = mutableLongStateOf(0L) // 유저 코인 잔액
            private set
        var nftImageUrl = mutableStateOf("") // 프레임 이미지
        var anonym = mutableIntStateOf(1) // 익명 번호
            private set
        var participant = mutableIntStateOf(0) // 참여자 수
            private set
        var auctionBidLog = mutableStateOf<List<AuctionBidLog>>(listOf()) // 경매 로그
            private set
        var bidPriceUnit = mutableLongStateOf(1L) // 경매 단가 (10%)
            private set
        var topPrice = mutableLongStateOf(0L) // 상위 입찰가
            private set
        var topUserId = mutableLongStateOf(-1L) // 상위 입찰자
            private set

        var myPercentage = mutableIntStateOf(0) // 입력 퍼센트
            private set
        var myBidPrice = mutableLongStateOf(0L) // 입력 입찰 금액
            private set
        var endTime = mutableStateOf(LocalDateTime.now().plusSeconds(15)) // 종료 시간
            private set
        var bid = mutableStateOf<BidMessage?>(null) // 입찰
            private set
        var result = mutableStateOf<EndMessage?>(null) // 경매 결과 (종료 시)
            private set
        private val _uiEvent = MutableSharedFlow<AuctionUiEvent>()
        val uiEvent = _uiEvent.asSharedFlow()

        init {
            viewModelScope.launch {
                userId = authDataSource.getUserId()?.toLong() ?: -1L
                Log.d(TAG, "userId: $userId")
            }
            initWebSocketManager()
            initStompSession()
            loadAuctionDetail()
            loadUserCoinBalance()
        }

        fun initWebSocketManager() {
            webSocketManager.value = WebSocketManager()
        }

        fun initStompSession() {
            viewModelScope.launch {
                if (stompSession.value != null) {
                    stompSession.value = webSocketManager.value?.connectToAuction()
                }
            }
        }

        private fun loadAuctionDetail() {
            if (auctionId == -1L) {
                viewModelScope.launch {
                    _uiEvent.emit(AuctionUiEvent.Error("경매가 종료되었습니다."))
                }
                return
            }
            viewModelScope.launch {
                auctionRepository.getAuction(auctionId).collectLatest { response ->
                    when (response) {
                        is ApiResponse.Error -> {
                            _uiEvent.emit(AuctionUiEvent.Error(response.errorMessage))
                        }

                        is ApiResponse.Success -> {
                            nftImageUrl.value = response.data.nftImageUrl
                            participant.intValue = response.data.participants
                            registerId = response.data.registerId
                            auctionStatus.value = AuctionStatus.valueOf(response.data.status)
                            Log.d(TAG, "loadAuctionDetail: registerId : $registerId")
                        }
                    }
                }
            }
        }

        fun loadUserCoinBalance() {
            viewModelScope.launch {
                coinRepository.getBalance().collectLatest { response ->
                    when (response) {
                        is ApiResponse.Error -> {
                            _uiEvent.emit(AuctionUiEvent.Error(response.errorMessage))
                        }

                        is ApiResponse.Success -> {
                            userCoinBalance.longValue = response.data.payableBalance
                        }
                    }
                }
            }
        }

        /**
         * 경매를 시작
         */
        fun startAuction() {
            if (auctionId == -1L) {
                viewModelScope.launch {
                    _uiEvent.emit(AuctionUiEvent.Error("존재하지 않는 경매입니다."))
                }
                return
            }
            viewModelScope.launch {
                auctionRepository.startAuction(auctionId).collectLatest { response ->
                    when (response) {
                        is ApiResponse.Error -> {
                            _uiEvent.emit(AuctionUiEvent.Error(response.errorMessage))
                        }

                        is ApiResponse.Success -> {
                            auctionStatus.value = AuctionStatus.PROGRESS
                        }
                    }
                }
            }
        }

        /** 해당 경매에 참여 */
        fun joinAuction() {
            if (auctionId == -1L) {
                viewModelScope.launch {
                    _uiEvent.emit(AuctionUiEvent.Error("존재하지 않는 경매입니다."))
                }
                return
            }
            viewModelScope.launch {
                auctionRepository.joinAuction(auctionId).collectLatest { response ->
                    when (response) {
                        is ApiResponse.Error -> {
                            _uiEvent.emit(AuctionUiEvent.Error(response.errorMessage))
                        }

                        is ApiResponse.Success -> {
                            userAnonym = response.data.anonym
                            auctionBidLog.value = response.data.bidLogs
                            if (auctionStatus.value == AuctionStatus.READY) {
                                userStatus.value = UserStatus.READY
                            } else if (auctionStatus.value == AuctionStatus.PROGRESS) {
                                userStatus.value = UserStatus.IN_PROGRESS
                            }
                        }
                    }
                }
            }
        }

        /** 해당 경매를 떠남 */
        fun leaveAuction() {
            if (auctionId == -1L) {
                viewModelScope.launch {
                    _uiEvent.emit(AuctionUiEvent.Error("존재하지 않는 경매입니다."))
                }
                return
            }
            viewModelScope.launch {
                auctionRepository.leaveAuction(auctionId).collectLatest { response ->
                    when (response) {
                        is ApiResponse.Error -> {
                            _uiEvent.emit(AuctionUiEvent.Error(response.errorMessage))
                        }

                        is ApiResponse.Success -> {
                            auctionBidLog.value = listOf()
                        }
                    }
                }
            }
        }

        /** 경매 시작 메세지 */
        fun updateStartMessage(startMessage: StartMessage) {
            topPrice.longValue = startMessage.currentBid
            endTime.value = startMessage.endTime
            auctionStatus.value = AuctionStatus.PROGRESS
        }

        fun updateParticipantsMessage(participantsMessage: Int) {
            participant.intValue = participantsMessage
        }

        /** log10을 통해 10%의 입찰 단가를 계산 */
        fun updateBidPriceUnit() {
            val unit = maxOf(log10(topPrice.longValue.toDouble()).toInt() - 1, 0)
            bidPriceUnit.longValue = 10.0.pow(unit).toLong()
        }

        /** 경매 종료 메세지 */
        fun updateEndMessage(endMessage: EndMessage) {
            result.value = endMessage
            if (endMessage.winner == null) {
                auctionStatus.value = AuctionStatus.CANCEL
            } else {
                auctionStatus.value = AuctionStatus.END
            }
        }

        /** user가 입찰했으면 메세지 출력 */
        fun observeBidMessage(bidMessage: BidMessage) {
            bid.value = bidMessage
            topUserId.longValue = bidMessage.userId
            observedBidSubmit.value = bidMessage.userId == userId
            bidMessageJob?.cancel()
            bidMessageJob =
                viewModelScope.launch {
                    observedBidMessage.value = true
                    delay(2000L)
                    observedBidMessage.value = false
                }
        }

        /**
         * 입찰 로그 리스트를 갱신
         * @param bidLog 입찰 로그
         */
        fun updateBidLog(bidLog: AuctionBidLog) {
            auctionBidLog.value += bidLog
            topPrice.longValue = bidLog.bidAmount
            endTime.value = bidLog.endTime
        }

        /** 사용자가 입력하는 입찰가를 초기화 */
        fun adjustClear() {
            myBidPrice.longValue = 0
            myPercentage.intValue = 0
        }

        /**
         * 사용자가 입력하는 입찰가 조정
         * @param price 상위가에 추가되는 금액
         * @param percentage 입력하는 퍼센트 합
         */
        fun adjustBidPrice(
            price: Long,
            percentage: Int,
        ) {
            myBidPrice.longValue += price
            myPercentage.intValue += percentage
        }

        fun validateUserBid() {
            validateUserBid.value = registerId != userId && topUserId.longValue != userId
        }
    }
