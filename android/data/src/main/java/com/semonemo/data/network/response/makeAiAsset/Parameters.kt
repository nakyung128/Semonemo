package com.semonemo.data.network.response.makeAiAsset


import com.google.gson.annotations.SerializedName

data class Parameters(
    @SerializedName("alwayson_scripts")
    val alwaysonScripts: AlwaysonScripts?,
    @SerializedName("batch_size")
    val batchSize: Int?,
    @SerializedName("cfg_scale")
    val cfgScale: Double?,
    @SerializedName("comments")
    val comments: Any?,
    @SerializedName("denoising_strength")
    val denoisingStrength: Double?,
    @SerializedName("disable_extra_networks")
    val disableExtraNetworks: Boolean?,
    @SerializedName("do_not_save_grid")
    val doNotSaveGrid: Boolean?,
    @SerializedName("do_not_save_samples")
    val doNotSaveSamples: Boolean?,
    @SerializedName("enable_hr")
    val enableHr: Boolean?,
    @SerializedName("eta")
    val eta: Any?,
    @SerializedName("firstpass_image")
    val firstpassImage: Any?,
    @SerializedName("firstphase_height")
    val firstphaseHeight: Int?,
    @SerializedName("firstphase_width")
    val firstphaseWidth: Int?,
    @SerializedName("force_task_id")
    val forceTaskId: Any?,
    @SerializedName("height")
    val height: Int?,
    @SerializedName("hr_checkpoint_name")
    val hrCheckpointName: Any?,
    @SerializedName("hr_negative_prompt")
    val hrNegativePrompt: String?,
    @SerializedName("hr_prompt")
    val hrPrompt: String?,
    @SerializedName("hr_resize_x")
    val hrResizeX: Int?,
    @SerializedName("hr_resize_y")
    val hrResizeY: Int?,
    @SerializedName("hr_sampler_name")
    val hrSamplerName: Any?,
    @SerializedName("hr_scale")
    val hrScale: Double?,
    @SerializedName("hr_scheduler")
    val hrScheduler: Any?,
    @SerializedName("hr_second_pass_steps")
    val hrSecondPassSteps: Int?,
    @SerializedName("hr_upscaler")
    val hrUpscaler: String?,
    @SerializedName("infotext")
    val infotext: Any?,
    @SerializedName("n_iter")
    val nIter: Int?,
    @SerializedName("negative_prompt")
    val negativePrompt: String?,
    @SerializedName("override_settings")
    val overrideSettings: OverrideSettings?,
    @SerializedName("override_settings_restore_afterwards")
    val overrideSettingsRestoreAfterwards: Boolean?,
    @SerializedName("prompt")
    val prompt: String?,
    @SerializedName("refiner_checkpoint")
    val refinerCheckpoint: Any?,
    @SerializedName("refiner_switch_at")
    val refinerSwitchAt: Any?,
    @SerializedName("restore_faces")
    val restoreFaces: Any?,
    @SerializedName("s_churn")
    val sChurn: Any?,
    @SerializedName("s_min_uncond")
    val sMinUncond: Any?,
    @SerializedName("s_noise")
    val sNoise: Any?,
    @SerializedName("s_tmax")
    val sTmax: Any?,
    @SerializedName("s_tmin")
    val sTmin: Any?,
    @SerializedName("sampler_index")
    val samplerIndex: String?,
    @SerializedName("sampler_name")
    val samplerName: String?,
    @SerializedName("save_images")
    val saveImages: Boolean?,
    @SerializedName("scheduler")
    val scheduler: Any?,
    @SerializedName("script_args")
    val scriptArgs: List<Any?>?,
    @SerializedName("script_name")
    val scriptName: Any?,
    @SerializedName("seed")
    val seed: Int?,
    @SerializedName("seed_resize_from_h")
    val seedResizeFromH: Int?,
    @SerializedName("seed_resize_from_w")
    val seedResizeFromW: Int?,
    @SerializedName("send_images")
    val sendImages: Boolean?,
    @SerializedName("steps")
    val steps: Int?,
    @SerializedName("styles")
    val styles: Any?,
    @SerializedName("subseed")
    val subseed: Int?,
    @SerializedName("subseed_strength")
    val subseedStrength: Int?,
    @SerializedName("tiling")
    val tiling: Any?,
    @SerializedName("width")
    val width: Int?
)