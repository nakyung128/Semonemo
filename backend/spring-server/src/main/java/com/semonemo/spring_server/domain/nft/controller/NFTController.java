package com.semonemo.spring_server.domain.nft.controller;

import com.semonemo.spring_server.domain.nft.dto.request.NFTMarketRequestDto;
import com.semonemo.spring_server.domain.nft.dto.request.NFTMarketServiceRequestDto;
import com.semonemo.spring_server.domain.nft.dto.request.NFTRequestDto;
import com.semonemo.spring_server.domain.nft.dto.request.NFTServiceRequestDto;
import com.semonemo.spring_server.domain.nft.dto.response.NFTMarketResponseDto;
import com.semonemo.spring_server.domain.nft.dto.response.NFTResponseDto;
import com.semonemo.spring_server.domain.user.entity.Users;
import com.semonemo.spring_server.domain.nft.service.NFTService;
import com.semonemo.spring_server.domain.user.service.UserService;
import com.semonemo.spring_server.global.common.CommonResponse;
import com.semonemo.spring_server.global.common.CursorResult;
import com.semonemo.spring_server.global.exception.CustomException;
import com.semonemo.spring_server.global.exception.ErrorCode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/nft")
@RequiredArgsConstructor
public class NFTController implements NFTApi {
    @Autowired
    private UserService userService;
    @Autowired
    private NFTService nftService;

    // NFT 발행
    @PostMapping(value = "")
    public CommonResponse<NFTResponseDto> mintNFT(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody NFTRequestDto NFTRequestDto) {
        try {
            Users users = userService.findByAddress(userDetails.getUsername());
            NFTServiceRequestDto nftServiceRequestDto = new NFTServiceRequestDto();
            nftServiceRequestDto.setUserId(users.getId());
            nftServiceRequestDto.setTokenId(NFTRequestDto.getTokenId());
            NFTResponseDto nftResponseDto = nftService.mintNFT(nftServiceRequestDto);
            return CommonResponse.success(nftResponseDto, "NFT 발행 성공");
        } catch (Exception e) {
            throw new CustomException(ErrorCode.MINT_NFT_FAIL);
        }
    }

    // NFT 판매 등록
    @PostMapping(value = "/sell")
    public CommonResponse<NFTMarketResponseDto> sellNFT(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestBody NFTMarketRequestDto nftMarketRequestDto) {
        try {
            Users users = userService.findByAddress(userDetails.getUsername());
            NFTMarketServiceRequestDto nftMarketServiceRequestDto = new NFTMarketServiceRequestDto();
            nftMarketServiceRequestDto.setNftId(nftMarketRequestDto.getNftId());
            nftMarketServiceRequestDto.setSeller(users.getId());
            nftMarketServiceRequestDto.setPrice(nftMarketRequestDto.getPrice());
            NFTMarketResponseDto nftMarketResponseDto = nftService.sellNFT(nftMarketServiceRequestDto);
            return CommonResponse.success(nftMarketResponseDto, "NFT 판매 등록 성공");
        } catch (Exception e) {
            throw new CustomException(ErrorCode.MINT_NFT_FAIL);
        }
    }

    // 마켓에 판매중인 모든 NFT 조회
    @GetMapping("")
    public CommonResponse<CursorResult<NFTMarketResponseDto>> getSellingNFT(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestParam(required = false) Long cursorId,
        @RequestParam(defaultValue = "15") int size) {
        try {
            Users users = userService.findByAddress(userDetails.getUsername());
            CursorResult<NFTMarketResponseDto> sellingNFT;
            sellingNFT = nftService.getSellingNFTs(users.getId(), cursorId, size);
            return CommonResponse.success(sellingNFT, "유저 NFT 조회 성공");
        } catch (Exception e) {
            throw new CustomException(ErrorCode.USER_NFT_NOT_FOUND_ERROR);
        }
    }

    // 유저 보유 NFT 조회
    @GetMapping("/users/{userId}")
    public CommonResponse<CursorResult<NFTResponseDto>> getUserNFT(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long userId,
            @RequestParam(required = false) Long cursorId,
            @RequestParam(defaultValue = "15") int size) {
        try {
            Users users = userService.findByAddress(userDetails.getUsername());
            CursorResult<NFTResponseDto> nft;
            if (users.getId().equals(userId)) {
                nft = nftService.getOwnedNFTs(userId, cursorId, size);
            } else {
                nft = nftService.getUserNFTs(userId, cursorId, size);
            }
            return CommonResponse.success(nft, "유저 NFT 조회 성공");
        } catch (Exception e) {
            throw new CustomException(ErrorCode.USER_NFT_NOT_FOUND_ERROR);
        }
    }
}
