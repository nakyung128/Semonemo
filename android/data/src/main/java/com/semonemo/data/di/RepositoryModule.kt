package com.semonemo.data.di

import com.semonemo.data.datasource.AuthDataSourceImpl
import com.semonemo.data.datasource.TokenDataSourceImpl
import com.semonemo.data.repository.AiRepositoryImpl
import com.semonemo.data.repository.AssetRepositoryImpl
import com.semonemo.data.repository.AuctionRepositoryImpl
import com.semonemo.data.repository.AuthRepositoryImpl
import com.semonemo.data.repository.CoinRepositoryImpl
import com.semonemo.data.repository.IpfsRepositoryImpl
import com.semonemo.data.repository.NftRepositoryImpl
import com.semonemo.data.repository.SearchRepositoryImpl
import com.semonemo.data.repository.UserRepositoryImpl
import com.semonemo.domain.datasource.AuthDataSource
import com.semonemo.domain.datasource.TokenDataSource
import com.semonemo.domain.repository.AiRepository
import com.semonemo.domain.repository.AssetRepository
import com.semonemo.domain.repository.AuctionRepository
import com.semonemo.domain.repository.AuthRepository
import com.semonemo.domain.repository.CoinRepository
import com.semonemo.domain.repository.IpfsRepository
import com.semonemo.domain.repository.NftRepository
import com.semonemo.domain.repository.SearchRepository
import com.semonemo.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    fun bindIpfsRepository(ipfsRepositoryImpl: IpfsRepositoryImpl): IpfsRepository

    @Binds
    fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    fun bindApiRepository(aiRepositoryImpl: AiRepositoryImpl): AiRepository

    @Binds
    fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

    @Binds
    fun bindNftRepository(nftRepositoryImpl: NftRepositoryImpl): NftRepository

    @Binds
    fun bindAssetRepository(assetRepositoryImpl: AssetRepositoryImpl): AssetRepository

    @Binds
    fun bindAuthDataSource(authDataSourceImpl: AuthDataSourceImpl): AuthDataSource

    @Binds
    fun bindTokenDataSource(tokenDataSourceImpl: TokenDataSourceImpl): TokenDataSource

    @Binds
    fun bindSearchRepository(searchRepositoryImpl: SearchRepositoryImpl): SearchRepository

    @Binds
    fun bindCoinRepository(coinRepositoryImpl: CoinRepositoryImpl): CoinRepository

    @Binds
    fun bindAuctionRepository(auctionRepositoryImpl: AuctionRepositoryImpl): AuctionRepository
}
