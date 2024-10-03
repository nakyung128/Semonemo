package com.semonemo.domain.model.myFrame

import com.semonemo.domain.model.FrameInfo

data class MyFrame(
    val nftId: Long = 0,
    val owner: Long = 0,
    val creator: Long = 0,
    val isOpen: Boolean = true,
    val isOnSale: Boolean = false,
    val nftInfo: FrameInfo = FrameInfo(),
    val tags: List<String> = listOf(),
    val frameType: Int = 0,
)
