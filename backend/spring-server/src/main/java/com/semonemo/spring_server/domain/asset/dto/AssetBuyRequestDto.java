package com.semonemo.spring_server.domain.asset.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class AssetBuyRequestDto {
    private String txHash;
    private Long assetSellId;
}
