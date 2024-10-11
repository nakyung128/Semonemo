package com.semonemo.data.network.response.makeAiAsset


import com.google.gson.annotations.SerializedName

data class Arg(
    @SerializedName("control_mode")
    val controlMode: String?,
    @SerializedName("enabled")
    val enabled: Boolean?,
    @SerializedName("guidance_end")
    val guidanceEnd: Double?,
    @SerializedName("guidance_start")
    val guidanceStart: Double?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("lowvram")
    val lowvram: Boolean?,
    @SerializedName("model")
    val model: String?,
    @SerializedName("module")
    val module: String?,
    @SerializedName("pixel_perfect")
    val pixelPerfect: Boolean?,
    @SerializedName("processor_res")
    val processorRes: Int?,
    @SerializedName("resize_mode")
    val resizeMode: String?,
    @SerializedName("threshold_a")
    val thresholdA: Int?,
    @SerializedName("threshold_b")
    val thresholdB: Int?
)