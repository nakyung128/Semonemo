package com.semonemo.spring_server.domain.coin.service;

import com.semonemo.spring_server.domain.blockchain.dto.event.CoinEvent;
import com.semonemo.spring_server.domain.blockchain.dto.event.NFTEvent;
import com.semonemo.spring_server.domain.blockchain.service.BlockChainService;
import com.semonemo.spring_server.domain.coin.dto.request.CoinRequestDto;
import com.semonemo.spring_server.domain.coin.dto.request.CoinServiceRequestDto;
import com.semonemo.spring_server.domain.coin.dto.response.CoinResponseDto;
import com.semonemo.spring_server.domain.user.entity.Users;
import com.semonemo.spring_server.domain.user.repository.UserRepository;
import com.semonemo.spring_server.global.exception.CustomException;
import com.semonemo.spring_server.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

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
    public Long payableToCoin(Long userId, Long amount) {
        Users user = userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND_ERROR));

        try {
            user.minusBalance(amount);

            return user.getBalance();
        } catch (Exception e) {
            throw new CustomException(ErrorCode.COIN_EXCHANGE_FAIL);
        }
    }

    @Override
    public Long coinToPayable(Long userId, Long amount) {
        Users user = userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND_ERROR));

        try {
            user.plusBalance(amount);

            return user.getBalance();
        } catch (Exception e) {
            throw new CustomException(ErrorCode.COIN_EXCHANGE_FAIL);
        }
    }
}
