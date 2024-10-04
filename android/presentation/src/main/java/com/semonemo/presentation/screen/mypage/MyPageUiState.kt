package com.semonemo.presentation.screen.mypage

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.semonemo.domain.model.Asset
import com.semonemo.domain.model.FrameDetail
import com.semonemo.domain.model.User
import com.semonemo.domain.model.myFrame.MyFrame

@Stable
sealed interface MyPageUiState {
    @Immutable
    data object Loading : MyPageUiState

    @Immutable
    data class Success(
        val userId: Long = 0,
        val nickname: String = "",
        val profileImageUrl: String = "",
        val following: List<User> = listOf(),
        val follower: List<User> = listOf(),
        val volume: Int = 0,
        val amount: Int = 0,
        val isFollow: Boolean? = null,
        val frameList: List<MyFrame> = listOf(),
        val sellFrameList: List<FrameDetail> = listOf(),
        val assetList: List<Asset> = listOf(),
    ) : MyPageUiState
}
