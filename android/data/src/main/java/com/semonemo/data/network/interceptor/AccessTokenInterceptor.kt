package com.semonemo.data.network.interceptor

import com.semonemo.domain.datasource.TokenDataSource
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AccessTokenInterceptor
    @Inject
    constructor(
        private val tokenDataSource: TokenDataSource,
    ) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest = chain.request()
            val requestBuilder = originalRequest.newBuilder()
            val accessToken: String =
                runBlocking {
                    val token: String =
                        tokenDataSource.getAccessToken() ?: run {
                            ""
                        }
                    token
                }
            val request =
                requestBuilder
                    .header(
                        "Authorization",
                        accessToken,
                    ).build()
            return chain.proceed(request)
        }
    }
