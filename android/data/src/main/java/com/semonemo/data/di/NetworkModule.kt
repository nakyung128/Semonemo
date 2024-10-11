package com.semonemo.data.di

import com.google.gson.GsonBuilder
import com.semonemo.data.BuildConfig
import com.semonemo.data.network.api.AuthApi
import com.semonemo.data.network.interceptor.AccessTokenInterceptor
import com.semonemo.data.network.interceptor.ErrorHandlingInterceptor
import com.semonemo.data.network.interceptor.JwtAuthenticator
import com.semonemo.domain.datasource.TokenDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class AuthClient

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class BaseClient

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class NftUploadClient

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class NftReadClient

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class AiClient

    @BaseClient
    @Singleton
    @Provides
    fun provideOkHttpClient(
        accessTokenInterceptor: AccessTokenInterceptor,
        jwtAuthenticator: JwtAuthenticator,
    ) = OkHttpClient.Builder().run {
        addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        addInterceptor(ErrorHandlingInterceptor())
        addInterceptor(accessTokenInterceptor)
        authenticator(jwtAuthenticator)
        connectTimeout(30, TimeUnit.SECONDS)
        readTimeout(30, TimeUnit.SECONDS)
        writeTimeout(30, TimeUnit.SECONDS)
        build()
    }

    @AuthClient
    @Singleton
    @Provides
    fun provideAuthClient(accessTokenInterceptor: AccessTokenInterceptor) =
        OkHttpClient.Builder().run {
            addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            addInterceptor(accessTokenInterceptor)
            addInterceptor(ErrorHandlingInterceptor())
            connectTimeout(30, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(30, TimeUnit.SECONDS)
            build()
        }

    @NftUploadClient
    @Singleton
    @Provides
    fun provideNftUploadClient(
        accessTokenInterceptor: AccessTokenInterceptor,
        jwtAuthenticator: JwtAuthenticator,
    ) = OkHttpClient.Builder().run {
        addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        addInterceptor(ErrorHandlingInterceptor())
        addInterceptor(accessTokenInterceptor)
        authenticator(jwtAuthenticator)
        connectTimeout(30, TimeUnit.SECONDS)
        readTimeout(30, TimeUnit.SECONDS)
        writeTimeout(30, TimeUnit.SECONDS)
        build()
    }

    @NftReadClient
    @Singleton
    @Provides
    fun provideNftReadOkHttpClient(
        accessTokenInterceptor: AccessTokenInterceptor,
        jwtAuthenticator: JwtAuthenticator,
    ) = OkHttpClient.Builder().run {
        addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        addInterceptor(ErrorHandlingInterceptor())
        addInterceptor(accessTokenInterceptor)
        authenticator(jwtAuthenticator)
        connectTimeout(30, TimeUnit.SECONDS)
        readTimeout(30, TimeUnit.SECONDS)
        writeTimeout(30, TimeUnit.SECONDS)
        build()
    }

    @AiClient
    @Singleton
    @Provides
    fun provideAiOkHttpClient() =
        OkHttpClient.Builder().run {
            addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            addInterceptor(ErrorHandlingInterceptor())
            connectTimeout(5, TimeUnit.MINUTES)
            readTimeout(5, TimeUnit.MINUTES)
            writeTimeout(5, TimeUnit.MINUTES)
            build()
        }

    @Singleton
    @Provides
    @AuthClient
    fun provideAuthRetrofit(
        @AuthClient
        okHttpClient: OkHttpClient,
    ): Retrofit =
        Retrofit
            .Builder()
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .baseUrl(BuildConfig.SEVER_URL)
            .client(okHttpClient)
            .build()

    @Singleton
    @Provides
    @BaseClient
    fun provideRetrofit(
        @BaseClient
        okHttpClient: OkHttpClient,
    ): Retrofit =
        Retrofit
            .Builder()
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .baseUrl(BuildConfig.SEVER_URL)
            .client(okHttpClient)
            .build()

    @Singleton
    @Provides
    @NftUploadClient
    fun provideNftUploadRetrofit(
        @NftUploadClient
        okHttpClient: OkHttpClient,
    ): Retrofit =
        Retrofit
            .Builder()
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .baseUrl(BuildConfig.IPFS_UPLOAD_URL)
            .client(okHttpClient)
            .build()

    @Singleton
    @Provides
    @NftReadClient
    fun provideNFTReadRetrofit(
        @NftReadClient
        okHttpClient: OkHttpClient,
    ): Retrofit =
        Retrofit
            .Builder()
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .baseUrl(BuildConfig.IPFS_READ_URL)
            .client(okHttpClient)
            .build()

    @Singleton
    @Provides
    @AiClient
    fun provideAiRetrofit(
        @AiClient
        okHttpClient: OkHttpClient,
    ): Retrofit =
        Retrofit
            .Builder()
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .baseUrl(BuildConfig.AI_SERVER_URL)
            .client(okHttpClient)
            .build()

    @Singleton
    @Provides
    fun provideJwtAuthenticator(
        tokenDataSource: TokenDataSource,
        authApi: AuthApi,
    ): JwtAuthenticator = JwtAuthenticator(tokenDataSource, authApi)

    @Singleton
    @Provides
    fun provideAccessTokenInterceptor(tokenDataSource: TokenDataSource): AccessTokenInterceptor = AccessTokenInterceptor(tokenDataSource)
}
