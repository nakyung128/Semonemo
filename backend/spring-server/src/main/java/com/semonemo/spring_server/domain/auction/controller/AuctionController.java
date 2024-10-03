package com.semonemo.spring_server.domain.auction.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.semonemo.spring_server.domain.auction.dto.response.BidLogDTO;
import com.semonemo.spring_server.domain.auction.dto.request.AuctionRequestDTO;
import com.semonemo.spring_server.domain.auction.entity.Auction;
import com.semonemo.spring_server.domain.auction.service.AuctionService;
import com.semonemo.spring_server.global.common.CommonResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auction")
@RequiredArgsConstructor
public class AuctionController {

	private final AuctionService auctionService;

	@PostMapping
	public CommonResponse<?> createAuction(@RequestBody AuctionRequestDTO requestDTO) {
		Auction auction = requestDTO.toEntity();
		return CommonResponse.success(auctionService.createAuction(auction), "경매 등록에 성공했습니다.");
	}

	@GetMapping("/{auctionId}/join")
	public CommonResponse<?> joinAuction(@PathVariable Long auctionId) {
		List<BidLogDTO> response = auctionService.readAuctionLog(auctionId);
		return CommonResponse.success(response, "경매 참여에 성공했습니다.");
	}

	@GetMapping("/{auctionId}")
	public CommonResponse<?> startAuction(@PathVariable long auctionId) {
		auctionService.startAuction(auctionId);
		return CommonResponse.success("경매를 성공적으로 시작했습니다.");
	}
}
