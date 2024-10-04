package com.semonemo.spring_server.domain.coin.controller;

import com.semonemo.spring_server.domain.coin.dto.request.CoinRequestDto;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.RequestBody;

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

    @Operation(summary = "코인 조회", description = "코인 조회")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "발행 성공",
            content = @Content(schema = @Schema(implementation = CoinResponseDto.class))),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    CommonResponse<CoinResponseDto> getCoin(
        @AuthenticationPrincipal UserDetails userDetails
    );

}
