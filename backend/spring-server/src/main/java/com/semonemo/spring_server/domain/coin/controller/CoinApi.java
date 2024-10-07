package com.semonemo.spring_server.domain.coin.controller;

import com.semonemo.spring_server.domain.coin.dto.request.CoinBurnRequestDto;
import com.semonemo.spring_server.domain.coin.dto.request.CoinRequestDto;
import com.semonemo.spring_server.domain.coin.dto.request.CoinTxRequestDto;
import com.semonemo.spring_server.domain.coin.dto.response.CoinResponseDto;
import com.semonemo.spring_server.domain.nft.dto.request.NFTRequestDto;
import com.semonemo.spring_server.domain.nft.dto.response.NFTResponseDto;
import com.semonemo.spring_server.global.common.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigInteger;

@Tag(name = "코인 관리", description = "코인 관련 API")
public interface CoinApi {

    @Operation(summary = "코인 구매", description = "코인 발행")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "발행 성공",
            content = @Content(schema = @Schema(implementation = CoinResponseDto.class))),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    CommonResponse<CoinResponseDto> mintCoin(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestBody CoinRequestDto coinRequestDto
    );

    @Operation(summary = "코인 환전(소각)", description = "코인을 소각하여 현금으로 환전")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "발행 성공",
            content = @Content(schema = @Schema(implementation = CoinResponseDto.class))),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    CommonResponse<CoinResponseDto> burnCoin(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestBody CoinBurnRequestDto coinBurnRequestDto
    );

    @Operation(summary = "코인 조회", description = "코인 조회")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = CoinResponseDto.class))),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    CommonResponse<CoinResponseDto> getCoin(
        @AuthenticationPrincipal UserDetails userDetails
    );

    @Operation(summary = "거래 내역 조회", description = "거래 내역 조회")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = Page.class))),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    CommonResponse<?> getTradeLog(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "15") int size
    );

    @Operation(summary = "코인 페이머니로 전환", description = "코인 결제용 페이머니로 전환")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "전환 성공",
            content = @Content(schema = @Schema(implementation = CoinResponseDto.class))),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    CommonResponse<CoinResponseDto> coinToPayable(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestBody CoinTxRequestDto coinTxRequestDto
    );

    @Operation(summary = "페이머니 코인으로 전환", description = "페이머니 코인으로 전환")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "전환 성공",
            content = @Content(schema = @Schema(implementation = CoinResponseDto.class))),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    CommonResponse<CoinResponseDto> payableToCoin(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestBody CoinTxRequestDto coinTxRequestDto
    );

    @Operation(summary = "코인 시세 조회", description = "코인 시세 및 변동률 조회하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "코인 시세 조회 성공",
            content = @Content(schema = @Schema(implementation = CoinResponseDto.class))),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    CommonResponse<?> getPrice(
    );

    @Operation(summary = "주간 코인 시세 조회", description = "주간 코인 시세 및 변동률 조회하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "주간 코인 시세 조회 성공",
            content = @Content(schema = @Schema(implementation = CoinResponseDto.class))),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    CommonResponse<?> getWeeklyPrices(
    );


}
