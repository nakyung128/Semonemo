package com.semonemo.spring_server.domain.auction.dto.request;

import com.semonemo.spring_server.domain.auction.entity.Auction;

public record AuctionRequestDTO(
	Long nftId,
	long startPrice) {

	public Auction toEntity() {
		return Auction.builder()
			.nftId(nftId)
			.startPrice(startPrice)
			.build();
	}
}
