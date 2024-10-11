package com.semonemo.data.network.api

import com.semonemo.data.network.response.FramePinResponse
import com.semonemo.domain.model.IpfsResponse
import com.semonemo.domain.model.Transaction
import com.semonemo.domain.request.TransferRequest
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface IpfsApi {
    @POST("bcapi/coin/transfer")
    suspend fun transfer(
        @Body request: TransferRequest,
    ): Transaction

    @Multipart
    @POST("api/v0/add")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part,
        @Query("pin") pin: Boolean = false,
    ): Response<IpfsResponse>

    @Multipart
    @POST("api/v0/add")
    suspend fun uploadFrame(
        @Part file: MultipartBody.Part,
        @Query("pin") pin: Boolean = false,
    ): Response<IpfsResponse>

    @POST("api/v0/pin/add")
    suspend fun pin(
        @Query("arg") arg: String,
    ): Response<FramePinResponse>
}
