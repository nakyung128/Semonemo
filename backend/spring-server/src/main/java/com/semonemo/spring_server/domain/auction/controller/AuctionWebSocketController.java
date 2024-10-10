package com.semonemo.spring_server.domain.auction.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import com.semonemo.spring_server.domain.auction.dto.request.BidRequestDTO;
import com.semonemo.spring_server.domain.auction.service.AuctionService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AuctionWebSocketController {

	private final AuctionService auctionService;

	@MessageMapping("/auction/{auctionId}/bid")
	public Void processBid(@DestinationVariable Long auctionId, BidRequestDTO bidRequest) {
		auctionService.processBid(auctionId, bidRequest);
		return null;
	}
}
