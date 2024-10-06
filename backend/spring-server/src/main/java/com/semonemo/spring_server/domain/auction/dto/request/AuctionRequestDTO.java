package com.semonemo.spring_server.domain.auction.dto.request;

import com.semonemo.spring_server.domain.auction.entity.Auction;
import com.semonemo.spring_server.domain.nft.entity.NFTs;

public record AuctionRequestDTO(
	Long nftId,
	long startPrice) {

	public Auction toEntity(NFTs nft) {
		return Auction.builder()
			.nft(nft)
			.startPrice(startPrice)
			.build();
	}
}
