package com.semonemo.data.network.api

import com.semonemo.data.network.response.BaseResponse
import com.semonemo.domain.model.HotKeyword
import com.semonemo.domain.model.SearchAsset
import com.semonemo.domain.model.SearchFrame
import com.semonemo.domain.model.SearchUser
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {
    @GET("api/search/users")
    suspend fun searchUsers(
        @Query("keyword") keyword: String,
    ): BaseResponse<SearchUser>

    @GET("api/search/asset")
    suspend fun searchAsset(
        @Query("keyword") keyword: String,
        @Query("orderBy") orderBy: String,
    ): BaseResponse<SearchAsset>

    @GET("api/search/nft")
    suspend fun searchNft(
        @Query("keyword") keyword: String,
    ): BaseResponse<SearchFrame>

    @GET("api/search/hot")
    suspend fun getHotKeywords(): BaseResponse<List<HotKeyword>>
}
