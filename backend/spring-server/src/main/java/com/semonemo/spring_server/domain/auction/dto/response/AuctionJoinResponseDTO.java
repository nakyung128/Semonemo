package com.semonemo.spring_server.domain.auction.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuctionJoinResponseDTO {
	private int anonym;
	private int participants;
	private List<BidLogDTO> bidLogs;
}
