package com.semonemo.domain.request

data class SignUpRequest(
    val address: String,
    val password: String,
    val nickname: String,
)
