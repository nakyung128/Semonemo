package com.semonemo.spring_server.domain.auction.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuctionResponseDTO {
	private long auctionId;
	private int currentBid;
	private long currentBidder;
	private LocalDateTime endTime;
}
