package com.semonemo.presentation.screen.frame

enum class FrameType(
    val idx: Int,
    val amount: Int,
) {
    OneByOne(1, 1),
    TwoByTwo(2, 4),
    OneByFour(3, 4),
    ;

    companion object {
        fun fromIdx(idx: Int): FrameType? = values().find { it.idx == idx }
    }
}
