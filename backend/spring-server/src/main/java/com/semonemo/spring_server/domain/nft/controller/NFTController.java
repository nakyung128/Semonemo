package com.semonemo.spring_server.domain.nft.controller;

import com.semonemo.spring_server.domain.nft.dto.request.NFTMarketRequestDto;
import com.semonemo.spring_server.domain.nft.dto.request.NFTMarketServiceRequestDto;
import com.semonemo.spring_server.domain.nft.dto.request.NFTRequestDto;
import com.semonemo.spring_server.domain.nft.dto.request.NFTServiceRequestDto;
import com.semonemo.spring_server.domain.nft.dto.response.NFTMarketHistoryResponseDto;
import com.semonemo.spring_server.domain.nft.dto.response.NFTMarketResponseDto;
import com.semonemo.spring_server.domain.nft.dto.response.NFTResponseDto;
import com.semonemo.spring_server.domain.nft.entity.NFTMarket;
import com.semonemo.spring_server.domain.user.entity.Users;
import com.semonemo.spring_server.domain.nft.service.NFTService;
import com.semonemo.spring_server.domain.user.service.UserService;
import com.semonemo.spring_server.global.common.CommonResponse;
import com.semonemo.spring_server.global.common.CursorResult;
import com.semonemo.spring_server.global.exception.CustomException;
import com.semonemo.spring_server.global.exception.ErrorCode;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/nft")
@RequiredArgsConstructor
public class NFTController implements NFTApi {
    private static final Log log = LogFactory.getLog(NFTController.class);

    private final UserService userService;
    private final NFTService nftService;

    // NFT 발행
    @PostMapping(value = "")
    public CommonResponse<NFTResponseDto> mintNFT(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody NFTRequestDto NFTRequestDto) {
        try {
            if (nftService.checkTokenId(NFTRequestDto.getTokenId())) {
                throw new CustomException(ErrorCode.NFT_ALREADY_MINT);
            }
            Users users = userService.findByAddress(userDetails.getUsername());
            NFTServiceRequestDto nftServiceRequestDto = new NFTServiceRequestDto();
            nftServiceRequestDto.setUserId(users.getId());
            nftServiceRequestDto.setTokenId(NFTRequestDto.getTokenId());
            nftServiceRequestDto.setTags(NFTRequestDto.getTags());
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
            if (nftService.checkMarket(nftMarketRequestDto.getNftId())) {
                throw new CustomException(ErrorCode.NFT_ALREADY_ON_SALE);
            }
            Users users = userService.findByAddress(userDetails.getUsername());
            NFTMarketServiceRequestDto nftMarketServiceRequestDto = new NFTMarketServiceRequestDto();
            nftMarketServiceRequestDto.setNftId(nftMarketRequestDto.getNftId());
            nftMarketServiceRequestDto.setSeller(users.getId());
            nftMarketServiceRequestDto.setPrice(nftMarketRequestDto.getPrice());
            log.info(nftMarketServiceRequestDto);
            NFTMarketResponseDto nftMarketResponseDto = nftService.sellNFT(nftMarketServiceRequestDto);
            return CommonResponse.success(nftMarketResponseDto, "NFT 판매 등록 성공");
        } catch (Exception e) {
            throw new CustomException(ErrorCode.MARKET_CREATE_FAIL);
        }
    }

    // 마켓에 판매중인 모든 NFT 조회
    @GetMapping("")
    public CommonResponse<?> getSellingNFT(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestParam(defaultValue = "latest") String orderBy,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "15") int size) {
        try {
            Users users = userService.findByAddress(userDetails.getUsername());
            Page<NFTMarketResponseDto> sellingNFT;
            sellingNFT = nftService.getSellingNFTs(users.getId(), orderBy, page, size);
            return CommonResponse.success(sellingNFT, "NFT 조회 성공");
        } catch (Exception e) {
            throw new CustomException(ErrorCode.NFT_NOT_FOUND_ERROR);
        }
    }

    // 마켓에 판매중인 특정 유저의 NFT 조회
    @GetMapping("/users/{seller}/seller")
    public CommonResponse<?> getUserSellingNFT(
        @AuthenticationPrincipal UserDetails userDetails,
        @PathVariable Long seller,
        @RequestParam(defaultValue = "latest") String orderBy,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "15") int size) {
        try {
            Users users = userService.findByAddress(userDetails.getUsername());
            Page<NFTMarketResponseDto> sellingNFT;
            sellingNFT = nftService.getUserSellingNFTs(seller, users.getId(), orderBy, page, size);
            return CommonResponse.success(sellingNFT, "유저 NFT 조회 성공");
        } catch (Exception e) {
            throw new CustomException(ErrorCode.USER_NFT_NOT_FOUND_ERROR);
        }
    }

    // 마켓에 판매중인 특정 제작자의 NFT 조회
    @GetMapping("/users/{creator}/creator")
    public CommonResponse<?> getCreatorSellingNFT(
        @AuthenticationPrincipal UserDetails userDetails,
        @PathVariable Long creator,
        @RequestParam(defaultValue = "latest") String orderBy,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "15") int size) {
        try {
            Users users = userService.findByAddress(userDetails.getUsername());
            Page<NFTMarketResponseDto> sellingNFT;
            sellingNFT = nftService.getCreatorSellingNFTs(creator, users.getId(), orderBy, page, size);
            return CommonResponse.success(sellingNFT, "제작자 NFT 조회 성공");
        } catch (Exception e) {
            throw new CustomException(ErrorCode.USER_NFT_NOT_FOUND_ERROR);
        }
    }

    // 마켓에 판매중인 특정 NFT 상세 조회
    @GetMapping("/{marketId}")
    public CommonResponse<NFTMarketResponseDto> getSellingNFTDetail(
        @AuthenticationPrincipal UserDetails userDetails,
        @PathVariable Long marketId) {
        try {
            Users users = userService.findByAddress(userDetails.getUsername());
            NFTMarketResponseDto sellingNFT;
            sellingNFT = nftService.getSellingNFTDetails(users.getId(), marketId);
            return CommonResponse.success(sellingNFT, "NFT 상세 조회 성공");
        } catch (Exception e) {
            throw new CustomException(ErrorCode.NFT_NOT_FOUND_ERROR);
        }
    }

    // 특정 NFT의 시세 조회
    @GetMapping("/{nftId}/history")
    public CommonResponse<List<NFTMarketHistoryResponseDto>> getMarketHistory(
        @AuthenticationPrincipal UserDetails userDetails,
        @PathVariable Long nftId) {
        try {
            List<NFTMarketHistoryResponseDto> priceHistory;
            priceHistory = nftService.getMarketHistory(nftId);
            return CommonResponse.success(priceHistory, "NFT 시세 조회 성공");
        } catch (Exception e) {
            throw new CustomException(ErrorCode.NFT_NOT_FOUND_ERROR);
        }
    }

    // 유저 보유 NFT 조회
    @GetMapping("/users/{userId}/owned")
    public CommonResponse<?> getUserNFT(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size) {
        try {
            Users users = userService.findByAddress(userDetails.getUsername());
            Page<NFTResponseDto> nft;
            if (users.getId().equals(userId)) {
                nft = nftService.getOwnedNFTs(userId, page, size);
            } else {
                nft = nftService.getUserNFTs(userId, page, size);
            }
            return CommonResponse.success(nft, "유저 NFT 조회 성공");
        } catch (Exception e) {
            throw new CustomException(ErrorCode.USER_NFT_NOT_FOUND_ERROR);
        }
    }

    // 특정 NFT 정보 조회
    @GetMapping("/users/{nftId}")
    public CommonResponse<NFTResponseDto> getNFTDetail(
        @AuthenticationPrincipal UserDetails userDetails,
        @PathVariable Long nftId) {
        try {
            NFTResponseDto nft;
            nft = nftService.getNFTDetails(nftId);
            return CommonResponse.success(nft, "NFT 조회 성공");
        } catch (Exception e) {
            throw new CustomException(ErrorCode.NFT_NOT_FOUND_ERROR);
        }
    }

    // NFT 공개 비공개 토글
    @PostMapping("/{nftId}/open")
    public CommonResponse<NFTResponseDto> marketOpenToggle(
        @AuthenticationPrincipal UserDetails userDetails,
        @PathVariable Long nftId) {
        try {
            NFTResponseDto nft;
            nftService.nftToggleOpen(nftId);
            return CommonResponse.success("NFT 공개 / 비공개 전환 성공");
        } catch (Exception e) {
            throw new CustomException(ErrorCode.MARKET_OPEN_FAIL);
        }
    }

    // 좋아요
    @PostMapping("/{marketId}/like")
    public CommonResponse<NFTResponseDto> marketLike(
        @AuthenticationPrincipal UserDetails userDetails,
        @PathVariable Long marketId) {
        try {
            Users users = userService.findByAddress(userDetails.getUsername());
            if (nftService.checkLike(users.getId(), marketId)) {
                throw new CustomException(ErrorCode.MARKET_ALREADY_LIKE);
            }
            nftService.marketLike(users.getId(), marketId);
            return CommonResponse.success("NFT 좋아요 성공");
        } catch (Exception e) {
            throw new CustomException(ErrorCode.MARKET_LIKE_FAIL);
        }
    }

    // 좋아요 취소
    @DeleteMapping("/{marketId}/like")
    public CommonResponse<NFTResponseDto> marketDisLike(
        @AuthenticationPrincipal UserDetails userDetails,
        @PathVariable Long marketId) {
        try {
            Users users = userService.findByAddress(userDetails.getUsername());
            if (!nftService.checkLike(users.getId(), marketId)) {
                throw new CustomException(ErrorCode.MARKET_ALREADY_DISLIKE);
            }
            nftService.marketDislike(users.getId(), marketId);
            return CommonResponse.success("NFT 좋아요 취소 성공");
        } catch (Exception e) {
            throw new CustomException(ErrorCode.MARKET_DISLIKE_FAIL);
        }
    }

    // NFT 구매
    @PostMapping("/purchase")
    public CommonResponse<NFTResponseDto> buyNFT(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestParam() Long marketId) {
        try {
            if (nftService.checkOnSale(marketId)) {
                throw new CustomException(ErrorCode.MARKET_ALREADY_SOLD);
            }
            Users users = userService.findByAddress(userDetails.getUsername());
            nftService.marketBuy(users.getId(), marketId);
            return CommonResponse.success("NFT 시세 조회 성공");
        } catch (Exception e) {
            throw new CustomException(ErrorCode.MARKET_BUY_FAIL);
        }
    }
}
