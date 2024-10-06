package com.semonemo.spring_server.domain.auction.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuctionResponseDTO {
	private long userId;
	private int anonym;
	private long bidAmount;
	private String bidTime;
	private String endTime;
}
