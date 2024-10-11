package com.semonemo.data.network.response

import com.google.gson.annotations.SerializedName

data class FramePinResponse(
    @SerializedName("Pins")
    val pins: List<String> = listOf(),
)
