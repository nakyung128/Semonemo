package com.semonemo.spring_server.domain.auction.service;

import java.util.List;

import com.semonemo.spring_server.domain.auction.dto.response.BidLogDTO;
import com.semonemo.spring_server.domain.auction.dto.request.BidRequestDTO;
import com.semonemo.spring_server.domain.auction.entity.Auction;

public interface AuctionService {
	Auction createAuction(Auction auction);

	List<BidLogDTO> readAuctionLog(Long auctionId);

	void startAuction(long auctionId);

	void processBid(long auctionId, BidRequestDTO bidRequest);

	void endAuction(long auctionId);
}
