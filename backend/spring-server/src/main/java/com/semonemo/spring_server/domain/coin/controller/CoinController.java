package com.semonemo.spring_server.domain.coin.controller;

import com.semonemo.spring_server.domain.blockchain.dto.event.MarketEvent;
import com.semonemo.spring_server.domain.blockchain.dto.event.NFTEvent;
import com.semonemo.spring_server.domain.blockchain.dto.event.TradeEvent;
import com.semonemo.spring_server.domain.blockchain.service.BlockChainService;
import com.semonemo.spring_server.domain.coin.dto.request.CoinRequestDto;
import com.semonemo.spring_server.domain.coin.dto.request.CoinServiceRequestDto;
import com.semonemo.spring_server.domain.coin.dto.response.CoinResponseDto;
import com.semonemo.spring_server.domain.coin.dto.response.TradeLogResponseDto;
import com.semonemo.spring_server.domain.coin.service.CoinService;
import com.semonemo.spring_server.domain.nft.controller.NFTController;
import com.semonemo.spring_server.domain.nft.dto.request.NFTRequestDto;
import com.semonemo.spring_server.domain.nft.dto.request.NFTServiceRequestDto;
import com.semonemo.spring_server.domain.nft.dto.response.NFTResponseDto;
import com.semonemo.spring_server.domain.user.entity.Users;
import com.semonemo.spring_server.domain.user.service.UserService;
import com.semonemo.spring_server.global.common.CommonResponse;
import com.semonemo.spring_server.global.exception.CustomException;
import com.semonemo.spring_server.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.EventValues;
import org.web3j.abi.datatypes.Type;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/coin")
@RequiredArgsConstructor
public class CoinController implements CoinApi{
    private static final Log log = LogFactory.getLog(CoinController.class);

    private final UserService userService;
    private final CoinService coinService;
    private final BlockChainService blockChainService;

    // 코인 발행
    @PostMapping(value = "/buy")
    public CommonResponse<CoinResponseDto> mintCoin(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestBody CoinRequestDto coinRequestDto) {
        try {
            Users users = userService.findByAddress(userDetails.getUsername());
            CoinServiceRequestDto coinServiceRequestDto = new CoinServiceRequestDto();
            coinServiceRequestDto.setAmount(coinRequestDto.getAmount());
            coinServiceRequestDto.setUserId(users.getId());
            CoinResponseDto coinResponseDto = coinService.mintCoin(coinServiceRequestDto);
            return CommonResponse.success(coinResponseDto, "코인 구매 성공");
        } catch (Exception e) {
            throw new CustomException(ErrorCode.COIN_MINT_FAIL);
        }
    }

    // 코인 조회
    @GetMapping(value = "")
    public CommonResponse<CoinResponseDto> getCoin(
        @AuthenticationPrincipal UserDetails userDetails) {
        try {
            Users users = userService.findByAddress(userDetails.getUsername());
            CoinResponseDto coinResponseDto = coinService.getCoin(users.getId());
            return CommonResponse.success(coinResponseDto, "코인 조회 성공");
        } catch (Exception e) {
            throw new CustomException(ErrorCode.COIN_GET_FAIL);
        }
    }

    // 거래 내역 조회
    @GetMapping(value = "/history")
    public CommonResponse<?> getTradeLog(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "15") int size) {
        try {
            Users users = userService.findByAddress(userDetails.getUsername());
            Page<TradeLogResponseDto> tradeLogs = coinService.getTradeLog(users.getId(), page, size);
            return CommonResponse.success(tradeLogs, "코인 거래내역 조회 성공");
        } catch (Exception e) {
            throw new CustomException(ErrorCode.GET_LOG_FAIL);
        }
    }

    // 코인 발행
    @PostMapping(value = "/exchange/payable")
    public CommonResponse<CoinResponseDto> coinToPayable(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestBody String txHash,
        @RequestBody Long amount) {
        try {
            TransactionReceipt transactionResult = blockChainService.waitForTransactionReceipt(txHash);

            String userAddress = null;
            BigInteger depositAmount = null;
            BigInteger newBalance = null;

            if (Objects.equals(transactionResult.getStatus(), "0x1")) {
                for (org.web3j.protocol.core.methods.response.Log txLog : transactionResult.getLogs()) {
                    String eventHash = EventEncoder.encode(TradeEvent.DEPOSIT_EVENT);

                    if (txLog.getTopics().get(0).equals(eventHash)) {
                        EventValues eventValues = Contract.staticExtractEventParameters(
                            TradeEvent.DEPOSIT_EVENT, txLog
                        );

                        if (eventValues != null) {
                            List<Type> indexedValues = eventValues.getIndexedValues();
                            List<Type> nonIndexedValues = eventValues.getNonIndexedValues();

                            userAddress = (String) indexedValues.get(0).getValue();
                            depositAmount = (BigInteger) nonIndexedValues.get(0).getValue();
                            newBalance = (BigInteger) nonIndexedValues.get(1).getValue();

                        } else {
                            throw new CustomException(ErrorCode.BLOCKCHAIN_ERROR);
                        }
                    }
                }
            } else {
                throw new CustomException(ErrorCode.BLOCKCHAIN_ERROR);
            }

            Users users = userService.findByAddress(userDetails.getUsername());
            Long payableBalance = coinService.coinToPayable(users.getId(), amount);
            Long coinBalance = blockChainService.convertFromSmallestUnit(newBalance);

            CoinResponseDto responseValue = new CoinResponseDto(
                users.getId(),
                coinBalance,
                payableBalance
            );

            return CommonResponse.success(responseValue, "코인 전환 성공");
        } catch (Exception e) {
            throw new CustomException(ErrorCode.COIN_EXCHANGE_FAIL);
        }
    }

    // 코인 발행
    @PostMapping(value = "/exchange/coin")
    public CommonResponse<CoinResponseDto> payableToCoin(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestBody String txHash,
        @RequestBody Long amount) {
        try {
            TransactionReceipt transactionResult = blockChainService.waitForTransactionReceipt(txHash);

            String userAddress = null;
            BigInteger depositAmount = null;
            BigInteger newBalance = null;

            if (Objects.equals(transactionResult.getStatus(), "0x1")) {
                for (org.web3j.protocol.core.methods.response.Log txLog : transactionResult.getLogs()) {
                    String eventHash = EventEncoder.encode(TradeEvent.WITHDRAWAL_EVENT);

                    if (txLog.getTopics().get(0).equals(eventHash)) {
                        EventValues eventValues = Contract.staticExtractEventParameters(
                            TradeEvent.WITHDRAWAL_EVENT, txLog
                        );

                        if (eventValues != null) {
                            List<Type> indexedValues = eventValues.getIndexedValues();
                            List<Type> nonIndexedValues = eventValues.getNonIndexedValues();

                            userAddress = (String) indexedValues.get(0).getValue();
                            depositAmount = (BigInteger) nonIndexedValues.get(0).getValue();
                            newBalance = (BigInteger) nonIndexedValues.get(1).getValue();

                        } else {
                            throw new CustomException(ErrorCode.BLOCKCHAIN_ERROR);
                        }
                    }
                }
            } else {
                throw new CustomException(ErrorCode.BLOCKCHAIN_ERROR);
            }

            Users users = userService.findByAddress(userDetails.getUsername());
            Long payableBalance = coinService.payableToCoin(users.getId(), amount);
            Long coinBalance = blockChainService.convertFromSmallestUnit(newBalance);

            CoinResponseDto responseValue = new CoinResponseDto(
                users.getId(),
                coinBalance,
                payableBalance
            );

            return CommonResponse.success(responseValue, "코인 전환 성공");
        } catch (Exception e) {
            throw new CustomException(ErrorCode.COIN_EXCHANGE_FAIL);
        }
    }
}
