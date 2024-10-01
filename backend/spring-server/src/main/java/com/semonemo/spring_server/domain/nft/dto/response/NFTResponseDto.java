package com.semonemo.spring_server.domain.nft.dto.response;

import com.semonemo.spring_server.domain.blockchain.dto.NFTInfoDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigInteger;
import java.util.List;

public record NFTResponseDto (
    Long nftId,
    Long creator,
    Long owner,
    BigInteger tokenId,
    List<String> tags,
    boolean isOpen,
    boolean isOnSale,
    NFTInfoDto nftInfo
) {
}
