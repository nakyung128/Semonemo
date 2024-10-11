package com.semonemo.domain.repository

interface TokenRepository {
    suspend fun saveJwtToken(
        accessToken: String,
        refreshToken: String,
    ): Unit

    suspend fun getAccessToken(): String?

    suspend fun getRefreshToken(): String?

    suspend fun getJwtToken(): Pair<String, String>

    suspend fun saveWalletAddress(address: String): Unit

    suspend fun getWalletAddress(): String?

    suspend fun savePassword(password: String)

    suspend fun getPassword(password: String): String?
}
