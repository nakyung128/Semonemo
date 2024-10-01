package com.semonemo.spring_server.domain.nft.dto.request;

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
public class NFTRequestDto {
    private String txHash;
    private List<String> tags;
}
