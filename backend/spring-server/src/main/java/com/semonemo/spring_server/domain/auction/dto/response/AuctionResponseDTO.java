package com.semonemo.spring_server.domain.auction.dto.response;

import com.semonemo.spring_server.domain.auction.entity.AuctionStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuctionResponseDTO {
	private long id;
	private AuctionStatus status;
	private long nftId;
	private long registerId;
	private String nftImageUrl;
	private int participants;
	private long startPrice;
	private Long currentBid;
	private String startTime;
	private String endTime;
}
