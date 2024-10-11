package com.semonemo.data.di

import com.semonemo.data.network.api.AiApi
import com.semonemo.data.network.api.AssetApi
import com.semonemo.data.network.api.AuctionApi
import com.semonemo.data.network.api.AuthApi
import com.semonemo.data.network.api.CoinApi
import com.semonemo.data.network.api.IpfsApi
import com.semonemo.data.network.api.NftApi
import com.semonemo.data.network.api.SearchApi
import com.semonemo.data.network.api.UserApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Provides
    @Singleton
    fun provideIpfsApi(
        @NetworkModule.NftUploadClient retrofit: Retrofit,
    ): IpfsApi = retrofit.create()

    @Provides
    @Singleton
    fun provideAuthApi(
        @NetworkModule.AuthClient retrofit: Retrofit,
    ): AuthApi = retrofit.create()

    @Provides
    @Singleton
    fun provideAiApi(
        @NetworkModule.AiClient retrofit: Retrofit,
    ): AiApi = retrofit.create()

    @Provides
    @Singleton
    fun provideUserApi(
        @NetworkModule.BaseClient retrofit: Retrofit,
    ): UserApi = retrofit.create()

    @Provides
    @Singleton
    fun provideNftApi(
        @NetworkModule.BaseClient retrofit: Retrofit,
    ): NftApi = retrofit.create()

    @Provides
    @Singleton
    fun provideAssetApi(
        @NetworkModule.BaseClient retrofit: Retrofit,
    ): AssetApi = retrofit.create()

    @Provides
    @Singleton
    fun provideSearchApi(
        @NetworkModule.BaseClient retrofit: Retrofit,
    ): SearchApi = retrofit.create()

    @Provides
    @Singleton
    fun provideCoinApi(
        @NetworkModule.BaseClient retrofit: Retrofit,
    ): CoinApi = retrofit.create()

    @Provides
    @Singleton
    fun provideAuctionApi(
        @NetworkModule.BaseClient retrofit: Retrofit,
    ): AuctionApi = retrofit.create()
}
