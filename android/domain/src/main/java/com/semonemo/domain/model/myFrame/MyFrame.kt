package com.semonemo.domain.model.myFrame

import com.semonemo.domain.model.FrameInfo
import com.semonemo.domain.model.User

data class MyFrame(
    val nftId: Long = 0,
    val owner: User = User(),
    val creator: User = User(),
    val isOpen: Boolean = true,
    val isOnSale: Boolean = false,
    val nftInfo: FrameInfo = FrameInfo(),
    val tags: List<String> = listOf(),
    val frameType: Int = 0,
)
