package com.semonemo.domain.datasource

import com.semonemo.domain.model.JwtToken

interface TokenDataSource {
    suspend fun saveJwtToken(jwtToken: JwtToken): Unit

    suspend fun getAccessToken(): String?

    suspend fun getRefreshToken(): String?

    suspend fun getJwtToken(): Pair<String, String>

    suspend fun deleteJwtToken(): Unit
}
