package com.semonemo.spring_server.domain.nft.controller;

import com.semonemo.spring_server.domain.blockchain.dto.event.MarketEvent;
import com.semonemo.spring_server.domain.blockchain.dto.event.TradeEvent;
import com.semonemo.spring_server.domain.blockchain.service.BlockChainService;
import com.semonemo.spring_server.domain.blockchain.dto.event.NFTEvent;
import com.semonemo.spring_server.domain.nft.dto.request.*;
import com.semonemo.spring_server.domain.nft.dto.response.NFTMarketHistoryResponseDto;
import com.semonemo.spring_server.domain.nft.dto.response.NFTMarketLikedResponseDto;
import com.semonemo.spring_server.domain.nft.dto.response.NFTMarketResponseDto;
import com.semonemo.spring_server.domain.nft.dto.response.NFTResponseDto;
import com.semonemo.spring_server.domain.user.entity.Users;
import com.semonemo.spring_server.domain.nft.service.NFTService;
import com.semonemo.spring_server.domain.user.service.UserService;
import com.semonemo.spring_server.global.common.CommonResponse;
import com.semonemo.spring_server.global.exception.CustomException;
import com.semonemo.spring_server.global.exception.ErrorCode;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.EventValues;
import org.web3j.abi.datatypes.Type;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/nft")
@RequiredArgsConstructor
public class NFTController implements NFTApi {
    private static final Log log = LogFactory.getLog(NFTController.class);

    private final UserService userService;
    private final NFTService nftService;
    private final BlockChainService blockChainService;

    // NFT 발행
    @PostMapping(value = "")
    public CommonResponse<NFTResponseDto> mintNFT(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody NFTRequestDto NFTRequestDto) {
        try {
            TransactionReceipt transactionResult = blockChainService.waitForTransactionReceipt(NFTRequestDto.getTxHash());

            BigInteger tokenId = null;

            if (Objects.equals(transactionResult.getStatus(), "0x1")) {
                for (org.web3j.protocol.core.methods.response.Log txLog : transactionResult.getLogs()) {
                    String eventHash = EventEncoder.encode(NFTEvent.NFT_MINTED_EVENT);

                    if (txLog.getTopics().get(0).equals(eventHash)) {
                        EventValues eventValues = Contract.staticExtractEventParameters(
                            NFTEvent.NFT_MINTED_EVENT, txLog
                        );

                        if (eventValues != null) {
                            List<Type> indexedValues = eventValues.getIndexedValues();
                            tokenId = (BigInteger) indexedValues.get(0).getValue();
                        } else {
                            throw new CustomException(ErrorCode.MINT_NFT_FAIL);
                        }
                    }
                }
            } else {
                throw new CustomException(ErrorCode.BLOCKCHAIN_ERROR);
            }

            if (tokenId == null) {
                throw new CustomException(ErrorCode.MINT_NFT_FAIL);
            }

            if (nftService.checkTokenId(tokenId)) {
                throw new CustomException(ErrorCode.NFT_ALREADY_MINT);
            }
            Users users = userService.findByAddress(userDetails.getUsername());
            NFTServiceRequestDto nftServiceRequestDto = new NFTServiceRequestDto();
            nftServiceRequestDto.setUserId(users.getId());
            nftServiceRequestDto.setTokenId(tokenId);
            nftServiceRequestDto.setTags(NFTRequestDto.getTags());
            nftServiceRequestDto.setFrameType(NFTRequestDto.getFrameType());
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
            TransactionReceipt transactionResult = blockChainService.waitForTransactionReceipt(nftMarketRequestDto.getTxHash());

            boolean eventFound = false;

            if (Objects.equals(transactionResult.getStatus(), "0x1")) {
                for (org.web3j.protocol.core.methods.response.Log txLog : transactionResult.getLogs()) {
                    String eventHash = EventEncoder.encode(MarketEvent.MARKET_CREATED_EVENT);
                    if (txLog.getTopics().get(0).equals(eventHash)) {
                        eventFound = true;
                        break;
                    }
                }

                if (!eventFound) {
                    throw new CustomException(ErrorCode.BLOCKCHAIN_ERROR);
                }
            } else {
                throw new CustomException(ErrorCode.BLOCKCHAIN_ERROR);
            }

            if (nftService.checkMarket(nftMarketRequestDto.getNftId())) {
                throw new CustomException(ErrorCode.NFT_ALREADY_ON_SALE);
            }

            Users users = userService.findByAddress(userDetails.getUsername());
            NFTMarketServiceRequestDto nftMarketServiceRequestDto = new NFTMarketServiceRequestDto();
            nftMarketServiceRequestDto.setNftId(nftMarketRequestDto.getNftId());
            nftMarketServiceRequestDto.setSeller(users.getId());
            nftMarketServiceRequestDto.setPrice(nftMarketRequestDto.getPrice());
            NFTMarketResponseDto nftMarketResponseDto = nftService.sellNFT(nftMarketServiceRequestDto);
            return CommonResponse.success(nftMarketResponseDto, "NFT 판매 등록 성공");
        } catch (Exception e) {
            throw new CustomException(ErrorCode.MARKET_CREATE_FAIL);
        }
    }

    // NFT 판매 취소
    @DeleteMapping(value = "/sell/{marketId}/{txHash}")
    public CommonResponse<NFTMarketResponseDto> cancelNFTSell(
        @AuthenticationPrincipal UserDetails userDetails,
        @PathVariable Long marketId,
        @PathVariable String txHash) {
        try {
            TransactionReceipt transactionResult = blockChainService.waitForTransactionReceipt(txHash);

            boolean eventFound = false;

            if (Objects.equals(transactionResult.getStatus(), "0x1")) {
                for (org.web3j.protocol.core.methods.response.Log txLog : transactionResult.getLogs()) {
                    String eventHash = EventEncoder.encode(MarketEvent.MARKET_CANCELLED_EVENT);
                    if (txLog.getTopics().get(0).equals(eventHash)) {
                        eventFound = true;
                        break;
                    }
                }

                if (!eventFound) {
                    throw new CustomException(ErrorCode.BLOCKCHAIN_ERROR);
                }
            } else {
                throw new CustomException(ErrorCode.BLOCKCHAIN_ERROR);
            }

            Users users = userService.findByAddress(userDetails.getUsername());
            nftService.cancelNFTMarket(users.getId(), marketId);
            return CommonResponse.success("NFT 판매 취소 성공");
        } catch (Exception e) {
            throw new CustomException(ErrorCode.MARKET_CANCEL_FAIL);
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

    // 좋아요한 마켓 NFT 조회
    @GetMapping("/users/liked")
    public CommonResponse<?> getLikedSellingNFTs(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "15") int size) {
        try {
            Users users = userService.findByAddress(userDetails.getUsername());
            Page<NFTMarketResponseDto> sellingNFT;
            sellingNFT = nftService.getLikedSellingNFTs(users.getId(), page, size);
            return CommonResponse.success(sellingNFT, "좋아요 NFT 조회 성공");
        } catch (Exception e) {
            throw new CustomException(ErrorCode.NFT_NOT_FOUND_ERROR);
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
    public CommonResponse<NFTMarketLikedResponseDto> marketLike(
        @AuthenticationPrincipal UserDetails userDetails,
        @PathVariable Long marketId) {
        try {
            Users users = userService.findByAddress(userDetails.getUsername());
            if (nftService.checkLike(users.getId(), marketId)) {
                throw new CustomException(ErrorCode.MARKET_ALREADY_LIKE);
            }
            int likedCount = nftService.marketLike(users.getId(), marketId);
            NFTMarketLikedResponseDto responseDto = new NFTMarketLikedResponseDto(
                marketId,
                likedCount
            );
            return CommonResponse.success(responseDto, "NFT 좋아요 성공");
        } catch (Exception e) {
            throw new CustomException(ErrorCode.MARKET_LIKE_FAIL);
        }
    }

    // 좋아요 취소
    @DeleteMapping("/{marketId}/like")
    public CommonResponse<NFTMarketLikedResponseDto> marketDisLike(
        @AuthenticationPrincipal UserDetails userDetails,
        @PathVariable Long marketId) {
        try {
            Users users = userService.findByAddress(userDetails.getUsername());
            if (!nftService.checkLike(users.getId(), marketId)) {
                throw new CustomException(ErrorCode.MARKET_ALREADY_DISLIKE);
            }
            int likedCount = nftService.marketDislike(users.getId(), marketId);

            NFTMarketLikedResponseDto responseDto = new NFTMarketLikedResponseDto(
                marketId,
                likedCount
            );
            return CommonResponse.success(responseDto, "NFT 좋아요 취소 성공");
        } catch (Exception e) {
            throw new CustomException(ErrorCode.MARKET_DISLIKE_FAIL);
        }
    }

    // NFT 구매
    @PostMapping("/purchase")
    public CommonResponse<NFTResponseDto> buyNFT(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestBody NFTPurchaseDto nftPurchaseDto) {
        try {
            TransactionReceipt transactionResult = blockChainService.waitForTransactionReceipt(nftPurchaseDto.getTxHash());

            BigInteger tradeId = null;

            if (Objects.equals(transactionResult.getStatus(), "0x1")) {
                for (org.web3j.protocol.core.methods.response.Log txLog : transactionResult.getLogs()) {
                    String eventHash = EventEncoder.encode(TradeEvent.TRADE_RECORDED_EVENT);

                    if (txLog.getTopics().get(0).equals(eventHash)) {
                        EventValues eventValues = Contract.staticExtractEventParameters(
                            TradeEvent.TRADE_RECORDED_EVENT, txLog
                        );

                        if (eventValues != null) {
                            List<Type> indexedValues = eventValues.getIndexedValues();

                            tradeId = (BigInteger) indexedValues.get(0).getValue();
                        } else {
                            throw new CustomException(ErrorCode.BLOCKCHAIN_ERROR);
                        }
                    }
                }
            } else {
                throw new CustomException(ErrorCode.BLOCKCHAIN_ERROR);
            }

            if (nftService.checkOnSale(nftPurchaseDto.getMarketId())) {
                throw new CustomException(ErrorCode.MARKET_ALREADY_SOLD);
            }

            Users users = userService.findByAddress(userDetails.getUsername());
            nftService.marketBuy(users.getId(), nftPurchaseDto.getMarketId(), tradeId);
            return CommonResponse.success("NFT 구매 성공");
        } catch (Exception e) {
            throw new CustomException(ErrorCode.MARKET_BUY_FAIL);
        }
    }

    // 타입별 사용가능한 NFT 조회 (보유중)
    @GetMapping("/available")
    public CommonResponse<?> availableNFTList(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestParam() int type,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "15") int size) {
        try {
            Users users = userService.findByAddress(userDetails.getUsername());

            Page<NFTResponseDto> availableNFT;
            availableNFT = nftService.getOwnedNFTsByType(users.getId(), type, page, size);
            return CommonResponse.success(availableNFT, "유저 NFT 조회 성공");
        } catch (Exception e) {
            throw new CustomException(ErrorCode.NFT_MARKET_NOT_FOUND_ERROR);
        }
    }

    // 타입별 사용가능한 NFT 조회 (보유중)
    @GetMapping("/hot")
    public CommonResponse<List<NFTMarketResponseDto>> hotNFTMarketList(
        @AuthenticationPrincipal UserDetails userDetails) {
        try {
            Users users = userService.findByAddress(userDetails.getUsername());
            List<NFTMarketResponseDto> hotNFTs = nftService.getHotNFTs(users.getId());
            return CommonResponse.success(hotNFTs, "인기 NFT 조회 성공");
        } catch (Exception e) {
            throw new CustomException(ErrorCode.NFT_MARKET_NOT_FOUND_ERROR);
        }
    }
}
