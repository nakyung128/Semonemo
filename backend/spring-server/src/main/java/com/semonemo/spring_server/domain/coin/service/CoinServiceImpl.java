package com.semonemo.spring_server.domain.coin.service;

import com.semonemo.spring_server.domain.blockchain.dto.NFTInfoDto;
import com.semonemo.spring_server.domain.blockchain.dto.event.CoinEvent;
import com.semonemo.spring_server.domain.blockchain.dto.event.NFTEvent;
import com.semonemo.spring_server.domain.blockchain.service.BlockChainService;
import com.semonemo.spring_server.domain.coin.dto.request.CoinRequestDto;
import com.semonemo.spring_server.domain.coin.dto.request.CoinServiceRequestDto;
import com.semonemo.spring_server.domain.coin.dto.response.CoinResponseDto;
import com.semonemo.spring_server.domain.coin.dto.response.TradeLogResponseDto;
import com.semonemo.spring_server.domain.coin.entity.TradeLog;
import com.semonemo.spring_server.domain.coin.repository.TradeLogRepository;
import com.semonemo.spring_server.domain.nft.dto.response.NFTMarketResponseDto;
import com.semonemo.spring_server.domain.nft.dto.response.NFTResponseDto;
import com.semonemo.spring_server.domain.nft.entity.NFTMarket;
import com.semonemo.spring_server.domain.nft.entity.NFTs;
import com.semonemo.spring_server.domain.user.dto.response.UserInfoResponseDTO;
import com.semonemo.spring_server.domain.user.entity.Users;
import com.semonemo.spring_server.domain.user.repository.UserRepository;
import com.semonemo.spring_server.global.exception.CustomException;
import com.semonemo.spring_server.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.web3j.abi.EventEncoder;
import org.web3j.abi.EventValues;
import org.web3j.abi.datatypes.Type;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.utils.Convert;

@Service
@RequiredArgsConstructor
public class CoinServiceImpl implements CoinService {
    private static final Log log = LogFactory.getLog(CoinServiceImpl.class);

    private final UserRepository userRepository;
    private final TradeLogRepository tradeLogRepository;
    private final BlockChainService blockChainService;

    @Override
    public CoinResponseDto mintCoin(CoinServiceRequestDto coinRequestDto) {
        Users user = userRepository.findById(coinRequestDto.getUserId())
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND_ERROR));

        BigInteger amountToBigInteger = BigInteger.valueOf(coinRequestDto.getAmount());

        BigInteger amountInSmallestUnit = blockChainService.convertToSmallestUnit(amountToBigInteger);

        // 결제 후에 작업
        try {
            TransactionReceipt transactionResult = blockChainService.mintCoin(user.getAddress(), amountInSmallestUnit);

            String to = "";
            BigInteger value = null;

            if (Objects.equals(transactionResult.getStatus(), "0x1")) {
                for (org.web3j.protocol.core.methods.response.Log txLog : transactionResult.getLogs()) {
                    String eventHash = EventEncoder.encode(CoinEvent.TOKENS_MINTED_EVENT);

                    if (txLog.getTopics().get(0).equals(eventHash)) {
                        EventValues eventValues = Contract.staticExtractEventParameters(
                            CoinEvent.TOKENS_MINTED_EVENT, txLog
                        );

                        if (eventValues != null) {
                            List<Type> indexedValues = eventValues.getIndexedValues();
                            List<Type> nonIndexedValues = eventValues.getNonIndexedValues();

                            to = (String) indexedValues.get(0).getValue();
                            value = (BigInteger) nonIndexedValues.get(1).getValue();

                            log.info(to);
                            log.info(value);
                        } else {
                            throw new CustomException(ErrorCode.COIN_MINT_FAIL);
                        }
                    }
                }
            } else {
                throw new CustomException(ErrorCode.BLOCKCHAIN_ERROR);
            }

            if (!Objects.equals(to, user.getAddress()) || value == null) {
                throw new CustomException(ErrorCode.COIN_MINT_FAIL);
            }

            return new CoinResponseDto(
                coinRequestDto.getUserId(),
                blockChainService.convertFromSmallestUnit(value),
                user.getBalance()
            );
        } catch (Exception e) {
            throw new CustomException(ErrorCode.BLOCKCHAIN_ERROR);
        }
    }

    @Override
    public CoinResponseDto getCoin(Long userId) {
        Users user = userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND_ERROR));

        try {
            BigInteger coinBalance = blockChainService.getBalanceOf(user.getAddress());

            Long payableBalance = user.getBalance();

            return new CoinResponseDto(
                userId,
                blockChainService.convertFromSmallestUnit(coinBalance),
                payableBalance
            );
        } catch (Exception e) {
            throw new CustomException(ErrorCode.BLOCKCHAIN_ERROR);
        }
    }

    @Override
    public Page<TradeLogResponseDto> getTradeLog(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<TradeLog> tradeLogs = tradeLogRepository.findByUser(userId, pageable);

        List<TradeLogResponseDto> dtos = new ArrayList<>();

        for (TradeLog tradeLog : tradeLogs.getContent()) {
            TradeLogResponseDto dto = tradeLogConvertToDto(tradeLog);
            dtos.add(dto);
        }

        return new PageImpl<>(dtos, pageable, tradeLogs.getTotalElements());
    }

    @Override
    public Long payableToCoin(Long userId, Long amount, BigInteger tradeId) {
        Users user = userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND_ERROR));

        try {
            user.minusBalance(amount);

            TradeLog tradeLog = TradeLog.builder()
                .tradeId(tradeId)
                .fromUser(user)
                .toUser(null)
                .amount(amount)
                .tradeType("코인 전환")
                .build();
            tradeLogRepository.save(tradeLog);

            return user.getBalance();
        } catch (Exception e) {
            throw new CustomException(ErrorCode.COIN_EXCHANGE_FAIL);
        }
    }

    @Override
    public Long coinToPayable(Long userId, Long amount, BigInteger tradeId) {
        Users user = userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND_ERROR));

        try {
            user.plusBalance(amount);

            TradeLog tradeLog = TradeLog.builder()
                .tradeId(tradeId)
                .fromUser(null)
                .toUser(user)
                .amount(amount)
                .tradeType("페이코인 전환")
                .build();
            tradeLogRepository.save(tradeLog);

            return user.getBalance();
        } catch (Exception e) {
            throw new CustomException(ErrorCode.COIN_EXCHANGE_FAIL);
        }
    }

    // Utils
    private TradeLogResponseDto tradeLogConvertToDto(TradeLog tradeLog) {
        UserInfoResponseDTO fromUserDto = new UserInfoResponseDTO(
            tradeLog.getFromUser().getId(),
            tradeLog.getFromUser().getAddress(),
            tradeLog.getFromUser().getNickname(),
            tradeLog.getFromUser().getProfileImage()
        );

        UserInfoResponseDTO toUserDto = new UserInfoResponseDTO(
            tradeLog.getToUser().getId(),
            tradeLog.getToUser().getAddress(),
            tradeLog.getToUser().getNickname(),
            tradeLog.getToUser().getProfileImage()
        );

        return new TradeLogResponseDto(
            tradeLog.getLogId(),
            tradeLog.getTradeId(),
            fromUserDto,
            toUserDto,
            tradeLog.getTradeType(),
            tradeLog.getAmount()
        );
    }
}
