package com.semonemo.data.network.response.makeAiAsset


import com.google.gson.annotations.SerializedName

data class AlwaysonScripts(
    @SerializedName("controlnet")
    val controlnet: Controlnet?
)