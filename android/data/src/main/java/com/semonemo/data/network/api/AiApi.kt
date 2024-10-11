package com.semonemo.data.network.api

import com.semonemo.data.network.response.RemoveBgResponse
import com.semonemo.data.network.response.makeAiAsset.MakeAiAssetResponse
import com.semonemo.domain.request.RemoveBgRequest
import com.semonemo.domain.request.makeAiAsset.MakeAiAssetRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AiApi {
    @POST("sdapi/v1/txt2img")
    suspend fun makeAiAsset(
        @Body request: MakeAiAssetRequest,
    ): MakeAiAssetResponse

    @POST("rembg")
    suspend fun removeBg(
        @Body request: RemoveBgRequest,
    ): RemoveBgResponse
}
