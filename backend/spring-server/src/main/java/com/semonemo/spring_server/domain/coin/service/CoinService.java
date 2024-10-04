package com.semonemo.spring_server.domain.coin.service;

import com.semonemo.spring_server.domain.coin.dto.request.CoinRequestDto;
import com.semonemo.spring_server.domain.coin.dto.request.CoinServiceRequestDto;
import com.semonemo.spring_server.domain.coin.dto.response.CoinResponseDto;

import java.math.BigInteger;

public interface CoinService {
    CoinResponseDto mintCoin(CoinServiceRequestDto coinRequestDto);

    CoinResponseDto getCoin(Long userId);

    Long payableToCoin(Long userId, Long amount);

    Long coinToPayable(Long userId, Long amount);
}
