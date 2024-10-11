package com.semonemo.domain.request

import com.google.gson.annotations.SerializedName

data class RemoveBgRequest(
    @SerializedName("input_image")
    val inputImage: String,
    @SerializedName("model")
    val model: String = "u2net",
    @SerializedName("return_mask")
    val returnMask: Boolean = false,
    @SerializedName("alpha_matting")
    val alphaMatting: Boolean = false,
    @SerializedName("alpha_matting_foreground_threshold")
    val alphaMattingForeGroundThreshold: Int = 240,
    @SerializedName("alpha_matting_background_threshold")
    val alphaMattingBackGroundThreshold: Int = 10,
    @SerializedName("alpha_matting_erode_size")
    val alphaMattingErodeSize: Int = 10,
)
