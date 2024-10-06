package com.semonemo.spring_server.domain.coin.dto.response;

import com.semonemo.spring_server.domain.user.dto.response.UserInfoResponseDTO;
import com.semonemo.spring_server.domain.user.entity.Users;
import jakarta.persistence.*;

import java.math.BigInteger;
import java.util.List;

public record TradeLogResponseDto (
    Long logId,
    BigInteger tradeId,
    UserInfoResponseDTO fromUser,
    UserInfoResponseDTO toUser,
    String tradeType,
    Long amount
) {
}