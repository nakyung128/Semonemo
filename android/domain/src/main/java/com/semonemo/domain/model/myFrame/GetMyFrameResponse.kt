package com.semonemo.domain.model.myFrame

import com.semonemo.domain.model.Pageable
import com.semonemo.domain.model.Sort

data class GetMyFrameResponse(
    val content: List<MyFrame> = listOf(),
    val pageable: Pageable = Pageable(),
    val last: Boolean = false,
    val totalElements: Int = 0,
    val totalPages: Int = 0,
    val size: Int = 0,
    val number: Int = 0,
    val sort: Sort = Sort(),
    val first: Boolean = false,
    val numberOfElements: Int = 0,
    val empty: Boolean = false,
)
