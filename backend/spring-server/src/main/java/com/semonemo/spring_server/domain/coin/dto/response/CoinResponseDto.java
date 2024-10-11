package com.semonemo.spring_server.domain.coin.dto.response;

import java.math.BigInteger;
import java.util.List;

public record CoinResponseDto (
    Long userId,
    Long coinBalance,
    Long payableBalance
) {
}