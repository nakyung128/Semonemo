package com.semonemo.domain.model

sealed class ApiResponse<out D> {
    data class Success<out D>(
        val data: D,
    ) : ApiResponse<D>()

    sealed class Error(
        open val errorCode: String = "",
        open val errorMessage: String = "",
    ) : ApiResponse<Nothing>() {
        data class ServerError(
            override val errorCode: String,
            override val errorMessage: String,
        ) : Error(errorCode, errorMessage)

        data class TokenError(
            override val errorCode: String,
            override val errorMessage: String,
        ) : Error(errorCode, errorMessage)

        data class NetworkError(
            override val errorMessage: String,
        ) : Error(errorMessage = errorMessage)

        data class UnknownError(
            override val errorMessage: String,
        ) : Error(errorMessage = errorMessage)
    }
}
