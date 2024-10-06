package com.semonemo.spring_server.domain.auction.service;

import java.util.List;

import com.semonemo.spring_server.domain.auction.dto.request.AuctionRequestDTO;
import com.semonemo.spring_server.domain.auction.dto.response.AuctionResponseDTO;
import com.semonemo.spring_server.domain.auction.dto.response.BidLogDTO;
import com.semonemo.spring_server.domain.auction.dto.request.BidRequestDTO;
import com.semonemo.spring_server.domain.auction.entity.Auction;

public interface AuctionService {
	Auction createAuction(Auction auction);

	Auction convertWithNFT(AuctionRequestDTO requestDTO);

	AuctionResponseDTO getAuctionById(Long auctionId);

	List<AuctionResponseDTO> getAllAuctions();

	List<BidLogDTO> readAuctionLog(Long auctionId);

	void startAuction(long auctionId);

	int addParticipantCount(long auctionId, long userId);

	int saveParticipant(long auctionId, long userId, int count);

	void removeParticipant(long auctionId);

	void processBid(long auctionId, BidRequestDTO bidRequest);

	void endAuction(long auctionId);
}
