package com.semonemo.spring_server.domain.coin.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigInteger;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CoinRequestDto {
    private Long userId;
    private Long amount;
}