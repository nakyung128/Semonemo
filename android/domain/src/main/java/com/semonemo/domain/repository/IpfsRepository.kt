package com.semonemo.domain.repository

import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.model.IpfsResponse
import com.semonemo.domain.model.Transaction
import com.semonemo.domain.request.TransferRequest
import com.semonemo.domain.request.UploadFrameRequest
import kotlinx.coroutines.flow.Flow
import java.io.File

interface IpfsRepository {
    suspend fun transfer(request: TransferRequest): Flow<Transaction?>

    suspend fun uploadImage(image: File): Flow<ApiResponse<IpfsResponse>>

    suspend fun uploadFrame(request: UploadFrameRequest): Flow<ApiResponse<IpfsResponse>>

    suspend fun pin(arg: String): Flow<ApiResponse<List<String>>>
}
