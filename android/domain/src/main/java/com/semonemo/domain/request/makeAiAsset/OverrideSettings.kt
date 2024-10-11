package com.semonemo.domain.request.makeAiAsset

import com.google.gson.annotations.SerializedName

data class OverrideSettings(
    @SerializedName("CLIP_stop_at_last_layers")
    val clipStopAtLastLayers: Int = 2,
    @SerializedName("sd_model_checkpoint")
    val sdModelCheckpoint: String = "majicmixRealistic_v7.safetensors",
)

sealed class ModelCheckPoint(
    open val model: String = "",
) {
    data object RealisticPeople :
        ModelCheckPoint(model = "majicmixRealistic_v7.safetensors [7c819b6d13]")

    data object Animal : ModelCheckPoint(model = "dreamshaper_8.safetensors [879db523c3]")

    data object Animation : ModelCheckPoint(model = "cetusMix_v4.safetensors [b42b09ff12]")

    data object Cartoon : ModelCheckPoint(model = "toonyou_beta6.safetensors [e8d456c42e]")

    /*
    data class Pixel(
        override val model: String,
    ) : ModelCheckPoint(model)
     */

    data object Etc : ModelCheckPoint(model = "dreamshaper_8.safetensors [879db523c3]")
}
