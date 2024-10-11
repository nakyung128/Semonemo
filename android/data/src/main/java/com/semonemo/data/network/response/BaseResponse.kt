package com.semonemo.data.network.response

import com.semonemo.data.exception.ApiException
import com.semonemo.data.exception.RefreshTokenExpiredException
import com.semonemo.domain.model.ApiResponse
import java.io.IOException

data class BaseResponse<T>(
    val code: String,
    val data: T?,
    val message: String,
)

suspend fun <T> emitApiResponse(
    apiResponse: suspend () -> BaseResponse<T>,
    default: T,
): ApiResponse<T> =
    runCatching {
        apiResponse()
    }.fold(
        onSuccess = { result ->
            ApiResponse.Success(data = result.data ?: default)
        },
        onFailure = { e ->
            when (e) {
                is ApiException ->
                    ApiResponse.Error.ServerError(
                        errorCode = e.error.errorCode,
                        errorMessage = e.error.message,
                    )

                is RefreshTokenExpiredException -> {
                    ApiResponse.Error.TokenError(
                        errorMessage = e.error.message,
                        errorCode = e.error.errorCode,
                    )
                }

                is IOException ->
                    ApiResponse.Error.NetworkError(
                        errorMessage = e.message ?: "",
                    )

                else ->
                    ApiResponse.Error.UnknownError(
                        errorMessage = e.message ?: "",
                    )
            }
        },
    )
