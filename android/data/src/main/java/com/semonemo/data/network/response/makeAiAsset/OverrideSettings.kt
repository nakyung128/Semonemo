package com.semonemo.data.network.response.makeAiAsset


import com.google.gson.annotations.SerializedName

data class OverrideSettings(
    @SerializedName("CLIP_stop_at_last_layers")
    val cLIPStopAtLastLayers: Int?,
    @SerializedName("sd_model_checkpoint")
    val sdModelCheckpoint: String?
)