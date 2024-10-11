package com.semonemo.data.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.metamask.androidsdk.Dapp
import io.metamask.androidsdk.Ethereum
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object EthereumModule {
    @Provides
    @Singleton
    fun provideEthereum(
        @ApplicationContext context: Context,
    ): Ethereum = Ethereum(context)

    @Provides
    @Singleton
    fun provideDAPP(): Dapp = Dapp("Droid Dapp", "https://droiddapp.com")
}
