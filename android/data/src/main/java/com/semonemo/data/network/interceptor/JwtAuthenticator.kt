package com.semonemo.data.network.interceptor

import com.semonemo.data.exception.ApiException
import com.semonemo.data.exception.RefreshTokenExpiredException
import com.semonemo.data.network.api.AuthApi
import com.semonemo.domain.datasource.TokenDataSource
import com.semonemo.domain.model.JwtToken
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import java.io.IOException
import javax.inject.Inject

private const val TAG = "JwtAuthenticator"

class JwtAuthenticator
    @Inject
    constructor(
        private val tokenDataSource: TokenDataSource,
        private val authApi: AuthApi,
    ) : Authenticator {
        override fun authenticate(
            route: Route?,
            response: Response,
        ): Request? {
            val request = response.request
            if (request.header("Authorization").isNullOrEmpty()) {
                return null
            }
            val refreshToken =
                runBlocking {
                    tokenDataSource.getRefreshToken()
                }
            return try {
                val newJwtToken: JwtToken? =
                    runBlocking {
                        val result =
                            authApi.getNewToken(
                                refreshToken ?: "",
                            )
                        result.data
                    }
                if (newJwtToken == null) return null
                runBlocking { tokenDataSource.saveJwtToken(newJwtToken) }

                request
                    .newBuilder()
                    .removeHeader("Authorization")
                    .addHeader("Authorization", newJwtToken.accessToken)
                    .build()
            } catch (e: Throwable) {
                when (e) {
                    is ApiException -> throw e
                    is RefreshTokenExpiredException -> throw e
                    is IOException -> throw IOException(e)

                    else -> throw e
                }
            }
        }
    }
