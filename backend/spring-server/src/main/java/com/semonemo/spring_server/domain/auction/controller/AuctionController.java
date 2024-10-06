package com.semonemo.spring_server.domain.auction.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.semonemo.spring_server.domain.auction.dto.response.AuctionJoinResponseDTO;
import com.semonemo.spring_server.domain.auction.dto.response.AuctionResponseDTO;
import com.semonemo.spring_server.domain.auction.dto.response.BidLogDTO;
import com.semonemo.spring_server.domain.auction.dto.request.AuctionRequestDTO;
import com.semonemo.spring_server.domain.auction.entity.Auction;
import com.semonemo.spring_server.domain.auction.service.AuctionService;
import com.semonemo.spring_server.domain.user.entity.Users;
import com.semonemo.spring_server.domain.user.service.UserService;
import com.semonemo.spring_server.global.common.CommonResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auction")
@RequiredArgsConstructor
public class AuctionController {

	private final AuctionService auctionService;
	private final UserService userService;

	@GetMapping("/{auctionId}")
	public CommonResponse<?> getAuctionById(@PathVariable Long auctionId) {
		AuctionResponseDTO response = auctionService.getAuctionById(auctionId);
		return CommonResponse.success(response, "경매 조회에 성공했습니다.");
	}

	@PostMapping
	public CommonResponse<?> createAuction(@RequestBody AuctionRequestDTO requestDTO) {
		Auction auction = auctionService.convertWithNFT(requestDTO);
		return CommonResponse.success(auctionService.createAuction(auction), "경매 등록에 성공했습니다.");
	}

	@GetMapping("/{auctionId}/join")
	public CommonResponse<?> joinAuction(
		@PathVariable Long auctionId,
		@AuthenticationPrincipal UserDetails userDetails
	) {
		Users user = userService.findByAddress(userDetails.getUsername());

		int participants = auctionService.addParticipantCount(auctionId, user.getId());
		int anonym = auctionService.saveParticipant(auctionId, user.getId(), participants);
		List<BidLogDTO> logs = auctionService.readAuctionLog(auctionId);

		AuctionJoinResponseDTO response = AuctionJoinResponseDTO.builder()
			.anonym(anonym)
			.participants(participants)
			.bidLogs(logs)
			.build();

		return CommonResponse.success(response, "경매 참여에 성공했습니다.");
	}

	@GetMapping("/{auctionId}/leave")
	public CommonResponse<?> leaveAuction(@PathVariable Long auctionId) {
		auctionService.removeParticipant(auctionId);
		return CommonResponse.success("경매 참여를 취소했습니다.");
	}

	@GetMapping("/{auctionId}/start")
	public CommonResponse<?> startAuction(@PathVariable long auctionId) {
		auctionService.startAuction(auctionId);
		return CommonResponse.success("경매를 성공적으로 시작했습니다.");
	}
}
