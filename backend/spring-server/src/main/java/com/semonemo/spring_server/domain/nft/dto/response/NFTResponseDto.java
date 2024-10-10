package com.semonemo.spring_server.domain.nft.dto.response;

import com.semonemo.spring_server.domain.blockchain.dto.NFTInfoDto;
import com.semonemo.spring_server.domain.user.dto.response.UserInfoResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigInteger;
import java.util.List;

public record NFTResponseDto (
    Long nftId,
    UserInfoResponseDTO creator,
    UserInfoResponseDTO owner,
    BigInteger tokenId,
    int frameType,
    List<String> tags,
    boolean isOpen,
    boolean isOnSale,
    NFTInfoDto nftInfo
) {
}
