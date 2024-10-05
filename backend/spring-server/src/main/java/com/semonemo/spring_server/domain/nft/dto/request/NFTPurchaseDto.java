package com.semonemo.spring_server.domain.nft.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigInteger;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class NFTPurchaseDto {
    private String txHash;
    private Long marketId;
}
