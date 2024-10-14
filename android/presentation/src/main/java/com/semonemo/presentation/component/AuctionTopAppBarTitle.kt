package com.semonemo.presentation.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.semonemo.presentation.screen.auction.AuctionDetailViewModel
import com.semonemo.presentation.screen.auction.AuctionStatus
import com.semonemo.presentation.screen.auction.UserStatus
import com.semonemo.presentation.theme.Typography

@Composable
fun AuctionTopAppBarTitle(
    modifier: Modifier = Modifier,
    viewModel: AuctionDetailViewModel = hiltViewModel(),
) {
    var fullText = ""
    var keywords = listOf("")
    when (viewModel.auctionStatus.value) {
        AuctionStatus.READY -> {
            if (viewModel.userStatus.value == UserStatus.NOT_READY) {
                fullText = "준비를 완료해 주세요."
            }
            if (viewModel.userStatus.value == UserStatus.READY) {
                fullText = String.format("%03d번", viewModel.userAnonym) + "님, 경매가 곧 시작됩니다."
                keywords = listOf(String.format("%03d번", viewModel.userAnonym))
            }
        }

        AuctionStatus.PROGRESS -> {
            if (viewModel.userStatus.value == UserStatus.NOT_READY) {
                fullText = "경매가 진행중 입니다."
            }
            if (viewModel.userStatus.value == UserStatus.IN_PROGRESS) {
                fullText = String.format("%03d번", viewModel.userAnonym) + "님, 경매를 참여합니다."
                keywords = listOf(String.format("%03d번", viewModel.userAnonym))
            }
        }
        AuctionStatus.END -> {
            if (viewModel.result.value?.winner == viewModel.userId) {
                fullText = "축하합니다. 프레임을 낙찰했습니다!"
                keywords = listOf("축하", "프레임", "낙찰")
            } else {
                fullText = "경매가 종료되었습니다."
                keywords = listOf("종료")
            }
        }

        AuctionStatus.CANCEL -> {
            fullText = "경매가 유찰되었습니다!"
            keywords = listOf("유찰")
        }
    }

    BoldTextWithKeywords(
        modifier = modifier,
        fullText = fullText,
        keywords = keywords,
        brushFlag = listOf(false, false, false),
        boldStyle = Typography.titleMedium.copy(fontSize = 16.sp),
        normalStyle = Typography.bodyMedium.copy(fontSize = 16.sp),
    )
}
