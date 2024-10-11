package com.semonemo.presentation.screen.search

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.semonemo.domain.model.AssetDetail
import com.semonemo.domain.model.FrameDetail
import com.semonemo.domain.model.HotKeyword
import com.semonemo.domain.model.Profile
import com.semonemo.domain.model.UserInfoResponse

@Stable
sealed interface SearchState {
    @Immutable
    data object Loading : SearchState

    @Immutable
    data class Init(
        val recentList: List<String> = listOf(),
        val hotList: List<HotKeyword> = listOf(),
    ) : SearchState

    @Immutable
    data class Success(
        val userList: List<UserInfoResponse> = listOf(),
        val frameList: List<FrameDetail> = listOf(),
        val assetList: List<AssetDetail> = listOf(),
    ) : SearchState

    @Immutable
    data class Error(
        val message: String = "",
    ) : SearchState
}
