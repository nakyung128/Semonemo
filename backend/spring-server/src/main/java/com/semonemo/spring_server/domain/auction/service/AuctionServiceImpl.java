package com.semonemo.spring_server.domain.auction.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.semonemo.spring_server.domain.auction.dto.response.AuctionEndDTO;
import com.semonemo.spring_server.domain.auction.dto.response.BidLogDTO;
import com.semonemo.spring_server.domain.auction.dto.request.BidRequestDTO;
import com.semonemo.spring_server.domain.auction.dto.response.AuctionResponseDTO;
import com.semonemo.spring_server.domain.auction.entity.Auction;
import com.semonemo.spring_server.domain.auction.entity.AuctionStatus;
import com.semonemo.spring_server.domain.auction.repository.AuctionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuctionServiceImpl implements AuctionService {

	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	private static final String AUCTION_KEY_PREFIX = "auction:";
	private static final String AUCTION_LOG_KEY_PREFIX = "auction:log:";

	private final AuctionRepository auctionRepository;
	private final RedisTemplate<String, Object> redisTemplate;
	private final SimpMessagingTemplate messagingTemplate;
	private final RedissonClient redissonClient;
	private final ObjectMapper objectMapper;

	@Override
	@Transactional
	public Auction createAuction(Auction auction) {
		auction.setStatus(AuctionStatus.READY);
		Auction savedAuction = auctionRepository.save(auction);

		String auctionKey = AUCTION_KEY_PREFIX + savedAuction.getId();
		String logKey = AUCTION_LOG_KEY_PREFIX + savedAuction.getId();

		HashOperations<String, String, Object> hashOps = redisTemplate.opsForHash();

		// 경매 정보 저장
		hashOps.put(auctionKey, "id", savedAuction.getId());
		hashOps.put(auctionKey, "startPrice", savedAuction.getStartPrice());
		hashOps.put(auctionKey, "currentBid", savedAuction.getStartPrice());
		hashOps.put(auctionKey, "currentBidder", null);

		// 경매 로그 저장
		hashOps.put(logKey, "bidLogs", "[]");

		return savedAuction;
	}

	@Override
	public List<BidLogDTO> readAuctionLog(Long auctionId) {
		String logKey = AUCTION_LOG_KEY_PREFIX + auctionId;
		HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();

		String logsJson = hashOps.get(logKey, "bidLogs");

		if(logsJson == null) {
			return new ArrayList<>();
		}

		try {
			return objectMapper.readValue(logsJson, new TypeReference<List<BidLogDTO>>() {});
		} catch (JsonProcessingException e) {
			log.error("Failed to parse auction logs for auction ID: " + auctionId, e);
			return new ArrayList<>();
		}
	}

	@Override
	@Transactional
	public void startAuction(long auctionId) {
		Auction auction = auctionRepository.findById(auctionId)
			.orElseThrow(() -> new RuntimeException("Auction not found"));

		// TODO : 경매 시작 로직 수정하기
		auction.setStartTime(LocalDateTime.now());
		auction.setStatus(AuctionStatus.PROGRESS);

		String auctionKey = AUCTION_KEY_PREFIX + auctionId;
		HashOperations<String, String, Object> hashOps = redisTemplate.opsForHash();

		hashOps.put(auctionKey, "startTime", LocalDateTime.now().format(formatter));
		hashOps.put(auctionKey, "endTime", LocalDateTime.now().plusSeconds(15).format(formatter));

		redisTemplate.expire(auctionKey, 15, TimeUnit.SECONDS);

		Map<String, Object> auctionData = hashOps.entries(auctionKey);
		messagingTemplate.convertAndSend("/topic/auction/" + auctionId, auctionData);
	}

	@Override
	@Transactional
	public void processBid(long auctionId, BidRequestDTO bidRequest) {
		RLock lock = redissonClient.getLock("auction-lock:" + auctionId);
		try {
			if (lock.tryLock(5, 10, TimeUnit.SECONDS)) {
				String auctionKey = AUCTION_KEY_PREFIX + auctionId;
				HashOperations<String, String, Object> hashOps = redisTemplate.opsForHash();

				int currentBid = (Integer) hashOps.get(auctionKey, "currentBid");

				// TODO : 사용자 잔고 확인
				// TODO : 사용자가 입찰한 금액이 현재 입찰가보다 높은지 확인 -> 클라이언트에서 처리? 서버에서 처리?
				if (bidRequest.getBidAmount() > currentBid) {
					hashOps.put(auctionKey, "currentBid", bidRequest.getBidAmount());
					hashOps.put(auctionKey, "currentBidder", bidRequest.getUserId());
					redisTemplate.expire(auctionKey, 15, TimeUnit.SECONDS);

					// TODO : 사용자 임의 번호 적용
					BidLogDTO logDTO = BidLogDTO.builder()
						.userId(bidRequest.getUserId())
						.bidAmount(bidRequest.getBidAmount())
						.bidTime(LocalDateTime.now())
						.build();
					addBidLog(auctionId, logDTO);

					AuctionResponseDTO response = new AuctionResponseDTO(
						auctionId,
						bidRequest.getBidAmount(),
						bidRequest.getUserId()
					);

					messagingTemplate.convertAndSend("/topic/auction/" + auctionId, response);
				}
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		} finally {
			if (lock.isHeldByCurrentThread()) {
				lock.unlock();
			}
		}
	}

	@Override
	@Transactional
	public void endAuction(long auctionId) {
		String auctionKey = AUCTION_KEY_PREFIX + auctionId;
		String logKey = AUCTION_LOG_KEY_PREFIX + auctionId;
		HashOperations<String, String, Object> hashOps = redisTemplate.opsForHash();

		// 경매 로그에서 마지막 입찰 정보 가져오기
		String logsJson = (String) hashOps.get(logKey, "bidLogs");
		List<BidLogDTO> bidLogs;
		BidLogDTO lastBid = null;

		try {
			bidLogs = objectMapper.readValue(logsJson, new TypeReference<List<BidLogDTO>>() {});
			if (!bidLogs.isEmpty()) {
				lastBid = bidLogs.get(bidLogs.size() - 1);
			}
		} catch (JsonProcessingException e) {
			log.error("Failed to parse auction logs for auction ID: " + auctionId, e);
		}

		Auction auction = auctionRepository.findById(auctionId)
			.orElseThrow(() -> new RuntimeException("Auction not found"));

		if (lastBid == null) {
			auction.setStatus(AuctionStatus.CANCEL);
		} else {
			auction.updateResult(
				lastBid.getUserId(),
				lastBid.getBidAmount(),
				LocalDateTime.now()
			);
		}

		AuctionEndDTO response = AuctionEndDTO.builder()
			.auctionId(auctionId)
			.finalPrice(lastBid != null ? lastBid.getBidAmount() : auction.getStartPrice())
			.winner(lastBid != null ? lastBid.getUserId() : null)
			.endTime(LocalDateTime.now())
			.build();

		messagingTemplate.convertAndSend("/topic/auction/" + auctionId + "/end", response);

		// 경매 종료 후 Redis에서 관련 데이터 삭제
		// redisTemplate.delete(auctionKey);
		// redisTemplate.delete(logKey);
	}

	private void addBidLog(Long auctionId, BidLogDTO bidLog) {
		String logKey = AUCTION_LOG_KEY_PREFIX + auctionId;
		HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();

		try {
			String logsJson = hashOps.get(logKey, "bidLogs");
			List<BidLogDTO> logs = logsJson != null ?
				objectMapper.readValue(logsJson, new TypeReference<List<BidLogDTO>>() {}) :
				new ArrayList<>();

			logs.add(bidLog);
			String updatedLogsJson = objectMapper.writeValueAsString(logs);
			hashOps.put(logKey, "bidLogs", updatedLogsJson);
		} catch (JsonProcessingException e) {
			log.error("Failed to add bid log for auction ID: " + auctionId, e);
		}
	}
}
