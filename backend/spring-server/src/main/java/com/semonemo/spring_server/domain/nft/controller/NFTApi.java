package com.semonemo.spring_server.domain.nft.controller;

import com.semonemo.spring_server.domain.nft.dto.request.NFTMarketRequestDto;
import com.semonemo.spring_server.domain.nft.dto.request.NFTRequestDto;
import com.semonemo.spring_server.domain.nft.dto.response.NFTMarketHistoryResponseDto;
import com.semonemo.spring_server.domain.nft.dto.response.NFTMarketResponseDto;
import com.semonemo.spring_server.global.common.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.semonemo.spring_server.global.common.CursorResult;
import com.semonemo.spring_server.domain.nft.dto.response.NFTResponseDto;

import java.util.List;

@Tag(name = "NFT 관리", description = "NFT 관련 API")
public interface NFTApi {

    @Operation(summary = "NFT 발행", description = "신규 NFT 발행")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "발행 성공",
            content = @Content(schema = @Schema(implementation = NFTResponseDto.class))),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    CommonResponse<NFTResponseDto> mintNFT(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestBody NFTRequestDto NFTRequestDto
    );

    @Operation(summary = "NFT 판매 등록", description = "NFT 판매 등록")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "판매 등록 성공",
            content = @Content(schema = @Schema(implementation = NFTMarketResponseDto.class))),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    CommonResponse<NFTMarketResponseDto> sellNFT(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestBody NFTMarketRequestDto nftMarketRequestDto
    );

    @Operation(summary = "마켓 판매 NFT 조회", description = "마켓에 판매중인 모든 NFT 조회")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = CursorResult.class))),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    CommonResponse<CursorResult<NFTMarketResponseDto>> getSellingNFT(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestParam(required = false) Long cursorId,
        @RequestParam(defaultValue = "40") int size
    );

    @Operation(summary = "특정 유저 마켓 판매 NFT 조회", description = "마켓에 판매중인 특정 유저의 NFT 조회")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = CursorResult.class))),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    CommonResponse<CursorResult<NFTMarketResponseDto>> getUserSellingNFT(
        @AuthenticationPrincipal UserDetails userDetails,
        @PathVariable Long seller,
        @RequestParam(required = false) Long cursorId,
        @RequestParam(defaultValue = "40") int size
    );

    @Operation(summary = "특정 제작자 마켓 판매 NFT 조회", description = "마켓에 판매중인 특정 제작자의 NFT 조회")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = CursorResult.class))),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    CommonResponse<CursorResult<NFTMarketResponseDto>> getCreatorSellingNFT(
        @AuthenticationPrincipal UserDetails userDetails,
        @PathVariable Long creator,
        @RequestParam(required = false) Long cursorId,
        @RequestParam(defaultValue = "40") int size
    );

    @Operation(summary = "특정 마켓 판매 NFT 상세 조회", description = "마켓에 판매중인 특정 NFT 상세 조회")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = NFTMarketResponseDto.class))),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    CommonResponse<NFTMarketResponseDto> getSellingNFTDetail(
        @AuthenticationPrincipal UserDetails userDetails,
        @PathVariable Long marketId
    );

    @Operation(summary = "특정 NFT 시세 조회", description = "특정 NFT의 거래 시세 조회")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = NFTMarketHistoryResponseDto.class))),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    CommonResponse<List<NFTMarketHistoryResponseDto>> getMarketHistory(
        @AuthenticationPrincipal UserDetails userDetails,
        @PathVariable Long nftId
    );

    @Operation(summary = "유저 NFT 조회", description = "유저가 보유하고 있는 NFT 조회")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = CursorResult.class))),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    CommonResponse<CursorResult<NFTResponseDto>> getUserNFT(
        @AuthenticationPrincipal UserDetails userDetails,
        @PathVariable Long userId,
        @RequestParam(required = false) Long cursorId,
        @RequestParam(defaultValue = "40") int size
    );

    @Operation(summary = "NFT 정보 상세 조회", description = "판매중이지 않은 NFT 정보 상세 조회")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = NFTResponseDto.class))),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    CommonResponse<NFTResponseDto> getNFTDetail(
        @AuthenticationPrincipal UserDetails userDetails,
        @PathVariable Long nftId
    );

    @Operation(summary = "NFT 공개 비공개", description = "NFT 공개 비공개 전환")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = NFTResponseDto.class))),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    CommonResponse<NFTResponseDto> marketOpenToggle(
        @AuthenticationPrincipal UserDetails userDetails,
        @PathVariable Long nftId
    );
    
    @Operation(summary = "NFT 판매 좋아요", description = "NFT 판매 좋아요 등록")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = NFTResponseDto.class))),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    CommonResponse<NFTResponseDto> marketLike(
        @AuthenticationPrincipal UserDetails userDetails,
        @PathVariable Long marketId
    );

    @Operation(summary = "NFT 판매 좋아요 취소", description = "NFT 판매 좋아요 취소")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = NFTResponseDto.class))),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    CommonResponse<NFTResponseDto> marketDisLike(
        @AuthenticationPrincipal UserDetails userDetails,
        @PathVariable Long marketId
    );

    @Operation(summary = "NFT 구매", description = "판매중인 NFT 구매")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = NFTResponseDto.class))),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    CommonResponse<NFTResponseDto> buyNFT(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestParam() Long nftId
    );
}
