package com.semonemo.domain.model

import com.google.gson.annotations.SerializedName

data class IpfsResponse(
    @SerializedName("Name")
    val name: String = "",
    @SerializedName("Hash")
    val hash: String = "",
    @SerializedName("Size")
    val size: String = "",
)
