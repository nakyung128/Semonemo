package com.semonemo.domain.request.makeAiAsset

import com.google.gson.annotations.SerializedName

data class MakeAiAssetRequest(
    @SerializedName("alwayson_scripts")
    val alwaysonScripts: AlwaysonScripts? = null,
    @SerializedName("denoising_strength")
    val denoisingStrength: Double = 0.7,
    @SerializedName("enable_hr")
    val enableHr: Boolean = true,
    @SerializedName("height")
    val height: Int = 512,
    @SerializedName("hr_scale")
    val hrScale: Int = 1,
    @SerializedName("hr_second_pass_steps")
    val hrSecondPassSteps: Int = 15,
    @SerializedName("hr_upscaler")
    val hrUpscaler: String = "Latent",
    @SerializedName("negative_prompt")
    val negativePrompt: String,
    @SerializedName("override_settings")
    val overrideSettings: OverrideSettings = OverrideSettings(),
    @SerializedName("override_settings_restore_afterwards")
    val overrideSettingsRestoreAfterwards: Boolean = true,
    @SerializedName("prompt")
    val prompt: String,
    @SerializedName("sampler_name")
    val samplerName: String = "DPM++ 2M",
    @SerializedName("scheduler")
    val scheduler: String = "karras",
    @SerializedName("steps")
    val steps: Int = 30,
    @SerializedName("width")
    val width: Int = 512,
)
