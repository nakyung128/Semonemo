package com.semonemo.data.network.api

import com.semonemo.data.network.response.BaseResponse
import com.semonemo.data.network.response.LikeResponse
import com.semonemo.domain.model.FrameDetail
import com.semonemo.domain.model.Nft
import com.semonemo.domain.model.SearchFrame
import com.semonemo.domain.model.myFrame.GetMyFrameResponse
import com.semonemo.domain.model.myFrame.MyFrame
import com.semonemo.domain.request.PublishNftRequest
import com.semonemo.domain.request.PurchaseNftRequest
import com.semonemo.domain.request.SellNftRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface NftApi {
    @POST("api/nft")
    suspend fun publishNft(
        @Body request: PublishNftRequest,
    ): BaseResponse<Nft>

    @GET("api/nft/users/{userId}/owned")
    suspend fun getUserNft(
        @Path("userId") userId: Long,
    ): BaseResponse<GetMyFrameResponse>

    @GET("api/nft/users/{seller}/seller")
    suspend fun getSellNft(
        @Path("seller") seller: Long,
    ): BaseResponse<SearchFrame>

    @GET("api/nft/available")
    suspend fun getAvailableNft(
        @Query("type") type: Int,
    ): BaseResponse<GetMyFrameResponse>

    @POST("api/nft/sell")
    suspend fun sellRegisterNft(
        @Body request: SellNftRequest,
    ): BaseResponse<FrameDetail>

    @GET("api/nft")
    suspend fun getAllSaleNft(
        @Query("orderBy") orderBy: String = "latest",
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 15,
    ): BaseResponse<SearchFrame>

    @GET("api/nft/{marketId}")
    suspend fun getSaleNftDetail(
        @Path("marketId") marketId: Long,
    ): BaseResponse<FrameDetail>

    @POST("api/nft/{marketId}/like")
    suspend fun likeNft(
        @Path("marketId") marketId: Long,
    ): BaseResponse<LikeResponse>

    @DELETE("api/nft/{marketId}/like")
    suspend fun disLikeNft(
        @Path("marketId") marketId: Long,
    ): BaseResponse<LikeResponse>

    @POST("api/nft/{nftId}/open")
    suspend fun open(
        @Path("nftId") nftId: Long,
    ): BaseResponse<Unit>

    @GET("api/nft/users/{nftId}")
    suspend fun getNftDetail(
        @Path("nftId") nftId: Long,
    ): BaseResponse<MyFrame>

    @GET("api/nft/users/liked")
    suspend fun getSaleLikeNft(): BaseResponse<SearchFrame>

    @POST("api/nft/purchase")
    suspend fun purchaseNft(
        @Body request: PurchaseNftRequest,
    ): BaseResponse<MyFrame>

    @DELETE("api/nft/sell/{marketId}/{txHash}")
    suspend fun cancelSaleNft(
        @Path("marketId") marketId: Long,
        @Path("txHash") txHash: String,
    ): BaseResponse<Unit>

    @GET("api/nft/users/{creator}/creator")
    suspend fun getCreatorSaleNft(
        @Path("creator") creator: Long,
    ): BaseResponse<SearchFrame>

    @GET("api/nft/hot")
    suspend fun getHotNft(): BaseResponse<List<FrameDetail>>
}
