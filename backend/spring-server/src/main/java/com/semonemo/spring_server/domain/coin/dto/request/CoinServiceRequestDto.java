package com.semonemo.spring_server.domain.coin.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigInteger;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CoinServiceRequestDto {
    private Long userId;
    private BigInteger amount;
}
