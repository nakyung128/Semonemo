package com.semonemo.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val userId: Long = 0L,
    val address: String = "",
    val nickname: String = "",
    val profileImage: String = "",
) : Parcelable
