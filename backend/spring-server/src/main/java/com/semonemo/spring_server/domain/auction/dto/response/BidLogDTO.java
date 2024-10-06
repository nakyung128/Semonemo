package com.semonemo.spring_server.domain.auction.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BidLogDTO {
	private Long userId;
	private int anonym;
	private long bidAmount;
	private String bidTime;
	private String endTime;
}
