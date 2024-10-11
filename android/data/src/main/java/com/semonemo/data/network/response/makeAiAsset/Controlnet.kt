package com.semonemo.data.network.response.makeAiAsset


import com.google.gson.annotations.SerializedName

data class Controlnet(
    @SerializedName("args")
    val args: List<Arg?>?
)