package com.semonemo.spring_server.domain.auction.controller;

import com.semonemo.spring_server.domain.auction.dto.request.AuctionRequestDTO;
import com.semonemo.spring_server.domain.auction.dto.response.AuctionJoinResponseDTO;
import com.semonemo.spring_server.domain.auction.dto.response.AuctionResponseDTO;
import com.semonemo.spring_server.domain.auction.entity.Auction;
import com.semonemo.spring_server.global.common.CommonResponse;
import com.semonemo.spring_server.global.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "경매 기능", description = "경매 기능 관련 API")
public interface AuctionAPI {

    @Operation(summary = "단일 경매 조회 API", description = "단일 경매 조회를 위한 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "단일 경매 조회 성공",
                    content = @Content(schema = @Schema(implementation = AuctionResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "단일 경매 조회 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "경매를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    CommonResponse<AuctionResponseDTO> getAuctionById(@PathVariable Long auctionId);

    @Operation(summary = "전체 경매 조회 API", description = "전체 경매 조회를 위한 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "전체 경매 조회 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = AuctionResponseDTO.class)))),
            @ApiResponse(responseCode = "400", description = "전체 경매 조회 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "경매를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    CommonResponse<List<AuctionResponseDTO>> getAllAuctions();

    @Operation(summary = "경매 생성 API", description = "경매 생성을 위한 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "경매 생성 성공",
                    content = @Content(schema = @Schema(implementation = AuctionJoinResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "경매 생성 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "경매를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    CommonResponse<Auction> createAuction(@RequestBody AuctionRequestDTO requestDTO);

    @Operation(summary = "경매 참여 API", description = "경매 참여를 위한 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "경매 참여 성공",
                    content = @Content(schema = @Schema(implementation = AuctionJoinResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "경매 참여 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "경매를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    CommonResponse<AuctionJoinResponseDTO> joinAuction(
            @PathVariable Long auctionId,
            @AuthenticationPrincipal UserDetails userDetails
    );

    @Operation(summary = "경매 나가기 API", description = "경매를 나가기 위한 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "경매 나가기 성공",
                    content = @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "400", description = "경매 나가기 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "경매를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    CommonResponse<?> leaveAuction(@PathVariable Long auctionId);

    @Operation(summary = "경매 시작 API", description = "경매를 시작하기 위한 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "경매 시작 성공",
                    content = @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "400", description = "경매 시작 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "경매를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    CommonResponse<?> startAuction(@PathVariable long auctionId);
}
