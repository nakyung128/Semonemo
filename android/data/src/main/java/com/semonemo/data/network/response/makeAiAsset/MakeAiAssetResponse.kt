package com.semonemo.data.network.response.makeAiAsset

import com.google.gson.annotations.SerializedName

data class MakeAiAssetResponse(
    @SerializedName("images")
    val images: List<String?>? = null,
    @SerializedName("info")
    val info: String? = null,
    @SerializedName("parameters")
    val parameters: Parameters? = null,
)
