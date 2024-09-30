package com.semonemo.spring_server.domain.blockchain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigInteger;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class NFTInfoDto {
    private BigInteger tokenId;
    private String creator;
    private String currentOwner;
    private String tokenURI;
}

