package com.semonemo.domain.request.makeAiAsset

import com.google.gson.annotations.SerializedName

data class Arg(
    @SerializedName("control_mode")
    val controlMode: String = "Balanced",
    val enabled: Boolean = true,
    @SerializedName("guidance_end")
    val guidanceEnd: Double = 1.0,
    @SerializedName("guidance_start")
    val guidanceStart: Double = 0.0,
    val image: String,
    @SerializedName("lowvram")
    val lowVram: Boolean = false,
    val model: String = "controlnet11Models_canny [b18e0966]",
    val module: String = "canny",
    @SerializedName("pixel_perfect")
    val pixelPerfect: Boolean = false,
    @SerializedName("process_res")
    val processorRes: Int = 512,
    @SerializedName("resize_mode")
    val resizeMode: String = "Resize and Fill",
    @SerializedName("threshold_a")
    val thresholdA: Int = 70,
    @SerializedName("threshold_b")
    val thresholdB: Int = 170,
)
