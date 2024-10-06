package com.semonemo.spring_server.domain.auction.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BidResponseDTO {
	private long userId;
	private int anonym;
	private long bidAmount;
	private String bidTime;
	private String endTime;
}
