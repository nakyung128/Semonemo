package com.semonemo.spring_server.domain.auction.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

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
import com.semonemo.spring_server.domain.auction.dto.request.AuctionRequestDTO;
import com.semonemo.spring_server.domain.auction.dto.response.AuctionEndDTO;
import com.semonemo.spring_server.domain.auction.dto.response.AuctionResponseDTO;
import com.semonemo.spring_server.domain.auction.dto.response.BidLogDTO;
import com.semonemo.spring_server.domain.auction.dto.request.BidRequestDTO;
import com.semonemo.spring_server.domain.auction.dto.response.BidResponseDTO;
import com.semonemo.spring_server.domain.auction.entity.Auction;
import com.semonemo.spring_server.domain.auction.entity.AuctionStatus;
import com.semonemo.spring_server.domain.auction.repository.AuctionRepository;
import com.semonemo.spring_server.domain.blockchain.dto.NFTInfoDto;
import com.semonemo.spring_server.domain.blockchain.service.BlockChainService;
import com.semonemo.spring_server.domain.nft.entity.NFTs;
import com.semonemo.spring_server.domain.nft.repository.nfts.NFTRepository;
import com.semonemo.spring_server.domain.user.entity.Users;
import com.semonemo.spring_server.domain.user.repository.UserRepository;
import com.semonemo.spring_server.global.exception.CustomException;
import com.semonemo.spring_server.global.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuctionServiceImpl implements AuctionService {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String AUCTION_KEY_PREFIX = "auction:";
    private static final String AUCTION_LOG_KEY_PREFIX = "auction-log:";
    private static final String AUCTION_PARTICIPANT_KEY_PREFIX = "auction-participant:";

    private final Map<Long, AtomicInteger> participantCount = new ConcurrentHashMap<>();

    private final UserRepository userRepository;
    private final AuctionRepository auctionRepository;
    private final NFTRepository nftRepository;
    private final BlockChainService blockChainService;
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
        String participantKey = AUCTION_PARTICIPANT_KEY_PREFIX + savedAuction.getId();

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
    @Transactional
    public Auction convertWithNFT(AuctionRequestDTO requestDTO) {
        NFTs nft = nftRepository.findById(requestDTO.nftId())
                .orElseThrow(() -> new CustomException(ErrorCode.NFT_NOT_FOUND_ERROR));

        return requestDTO.toEntity(nft);
    }

    @Override
    @Transactional
    public AuctionResponseDTO getAuctionById(Long auctionId) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new CustomException(ErrorCode.AUCTION_NOT_FOUND));

        NFTInfoDto nftInfo;
        try {
            nftInfo = blockChainService.getNFTById(auction.getNft().getTokenId());
        } catch (Exception e) {
            throw new CustomException(ErrorCode.BLOCKCHAIN_ERROR);
        }

        Long currentBid = null;
        if (redisTemplate.opsForHash().hasKey(AUCTION_KEY_PREFIX + auctionId, "currentBid")) {
            currentBid = Long.parseLong(String.valueOf(redisTemplate.opsForHash().get(AUCTION_KEY_PREFIX + auctionId, "currentBid")));
        }

        return AuctionResponseDTO.builder()
                .id(auction.getId())
                .status(auction.getStatus())
                .nftId(auction.getNft().getNftId())
                .nftImageUrl(nftInfo.getData().getImage())
                .participants(getParticipantCount(auctionId))
                .startPrice(auction.getStartPrice())
                .currentBid(currentBid)
                .startTime(auction.getStartTime() != null ? auction.getStartTime().format(formatter) : null)
                .endTime(auction.getEndTime() != null ? auction.getEndTime().format(formatter) : null)
                .build();
    }

    @Override
    @Transactional
    public List<AuctionResponseDTO> getAllAuctions() {
        List<Auction> auctions = auctionRepository.findAll();

        List<AuctionResponseDTO> auctionResponseDTOs = new ArrayList<>();
        for (Auction auction : auctions) {
            NFTInfoDto nftInfo;
            try {
                nftInfo = blockChainService.getNFTById(auction.getNft().getTokenId());
            } catch (Exception e) {
                throw new CustomException(ErrorCode.BLOCKCHAIN_ERROR);
            }

            Long currentBid = null;
            if (redisTemplate.opsForHash().hasKey(AUCTION_KEY_PREFIX + auction.getId(), "currentBid")) {
                currentBid = Long.parseLong(String.valueOf(redisTemplate.opsForHash().get(AUCTION_KEY_PREFIX + auction.getId(), "currentBid")));
            }

            AuctionResponseDTO auctionResponseDTO = AuctionResponseDTO.builder()
                    .id(auction.getId())
                    .status(auction.getStatus())
                    .nftId(auction.getNft().getNftId())
                    .nftImageUrl(nftInfo.getData().getImage())
                    .participants(getParticipantCount(auction.getId()))
                    .startPrice(auction.getStartPrice())
                    .currentBid(currentBid)
                    .startTime(auction.getStartTime() != null ? auction.getStartTime().format(formatter) : null)
                    .endTime(auction.getEndTime() != null ? auction.getEndTime().format(formatter) : null)
                    .build();
            auctionResponseDTOs.add(auctionResponseDTO);
        }

        return auctionResponseDTOs;
    }

    @Override
    public List<BidLogDTO> readAuctionLog(Long auctionId) {
        String auctionKey = AUCTION_KEY_PREFIX + auctionId;
        if (Boolean.FALSE.equals(redisTemplate.hasKey(auctionKey))) {
            throw new CustomException(ErrorCode.AUCTION_NOT_FOUND);
        }

        String logKey = AUCTION_LOG_KEY_PREFIX + auctionId;
        HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();

        String logsJson = hashOps.get(logKey, "bidLogs");

        if (logsJson == null) {
            return new ArrayList<>();
        }

        try {
            return objectMapper.readValue(logsJson, new TypeReference<List<BidLogDTO>>() {
            });
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

        if (auction.getStatus() != AuctionStatus.READY) {
            throw new CustomException(ErrorCode.INVALID_AUCTION_STATUS);
        }

        // TODO : 경매 시작 로직 수정하기
        auction.setStartTime(LocalDateTime.now());
        auction.setStatus(AuctionStatus.PROGRESS);

        String auctionKey = AUCTION_KEY_PREFIX + auctionId;
        HashOperations<String, String, Object> hashOps = redisTemplate.opsForHash();

        hashOps.put(auctionKey, "startTime", LocalDateTime.now().format(formatter));
        hashOps.put(auctionKey, "endTime", LocalDateTime.now().plusSeconds(15).format(formatter));

        redisTemplate.expire(auctionKey, 15, TimeUnit.SECONDS);

        Map<String, Object> auctionData = hashOps.entries(auctionKey);
        messagingTemplate.convertAndSend("/topic/auction/" + auctionId + "/start", auctionData);
    }

    @Override
    public int addParticipantCount(long auctionId, long userId) {
        participantCount.computeIfAbsent(auctionId, k -> new AtomicInteger(0)).incrementAndGet();
        return sendParticipantCount(auctionId);
    }

    @Override
    public int saveParticipant(long auctionId, long userId, int count) {
        String participantKey = AUCTION_PARTICIPANT_KEY_PREFIX + auctionId;
        HashOperations<String, String, String> participantHashOps = redisTemplate.opsForHash();
        participantHashOps.put(participantKey, String.valueOf(userId), String.valueOf(count));
        return count;
    }

    @Override
    public void removeParticipant(long auctionId) {
        participantCount.computeIfPresent(auctionId, (k, v) -> {
            v.decrementAndGet();
            return v.get() > 0 ? v : null;
        });
        sendParticipantCount(auctionId);
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

                // 사용자 잔액 확인
                Users user = userRepository.findById(bidRequest.getUserId())
                        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND_ERROR));
                if (user.getBalance() < bidRequest.getBidAmount()) {
                    throw new CustomException(ErrorCode.INSUFFICIENT_BALANCE);
                }

                String participantKey = AUCTION_PARTICIPANT_KEY_PREFIX + auctionId;
                int anonym = Integer.parseInt(String.valueOf(hashOps.get(participantKey, String.valueOf(bidRequest.getUserId()))));

                if (bidRequest.getBidAmount() > currentBid) {
                    hashOps.put(auctionKey, "currentBid", bidRequest.getBidAmount());
                    hashOps.put(auctionKey, "currentBidder", bidRequest.getUserId());
                    hashOps.put(auctionKey, "endTime", LocalDateTime.now().plusSeconds(15).format(formatter));
                    redisTemplate.expire(auctionKey, 15, TimeUnit.SECONDS);

                    // TODO : 사용자 임의 번호 적용
                    String now = LocalDateTime.now().format(formatter);
                    String endTime = (String) hashOps.get(auctionKey, "endTime");

                    BidLogDTO logDTO = BidLogDTO.builder()
                            .userId(bidRequest.getUserId())
                            .anonym(anonym)
                            .bidAmount(bidRequest.getBidAmount())
                            .bidTime(now)
                            .endTime(endTime)
                            .build();
                    addBidLog(auctionId, logDTO);

                    BidResponseDTO response = new BidResponseDTO(
                            bidRequest.getUserId(),
                            anonym,
                            bidRequest.getBidAmount(),
                            now,
                            endTime
                    );

                    messagingTemplate.convertAndSend("/topic/auction/" + auctionId, response);
                } else {
                    throw new CustomException(ErrorCode.BID_AMOUNT_LESS_THAN_HIGHEST);
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
        String logKey = AUCTION_LOG_KEY_PREFIX + auctionId;
        HashOperations<String, String, Object> hashOps = redisTemplate.opsForHash();

        // 경매 로그에서 마지막 입찰 정보 가져오기
        String logsJson = (String) hashOps.get(logKey, "bidLogs");
        List<BidLogDTO> bidLogs = new ArrayList<>();
        BidLogDTO lastBid = null;

        try {
            bidLogs = objectMapper.readValue(logsJson, new TypeReference<List<BidLogDTO>>() {
            });
            if (!bidLogs.isEmpty()) {
                lastBid = bidLogs.get(bidLogs.size() - 1);
            }
        } catch (JsonProcessingException e) {
            log.error("Failed to parse auction logs for auction ID: " + auctionId, e);
        }

        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new RuntimeException("Auction not found"));

        // 경매가 유찰된 경우
        if (lastBid == null) {
            auction.setStatus(AuctionStatus.CANCEL);
        } else {
            auction.updateResult(
                    lastBid.getUserId(),
                    lastBid.getBidAmount(),
                    LocalDateTime.now()
            );

            Users user = userRepository.findById(lastBid.getUserId())
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND_ERROR));
            user.minusBalance(lastBid.getBidAmount());
        }

        AuctionEndDTO response = AuctionEndDTO.builder()
                .auctionId(auctionId)
                .finalPrice(lastBid != null ? lastBid.getBidAmount() : auction.getStartPrice())
                .winner(lastBid != null ? lastBid.getUserId() : null)
                .endTime(LocalDateTime.now().format(formatter))
                .build();

        messagingTemplate.convertAndSend("/topic/auction/" + auctionId + "/end", response);

        // 경매 종료 후 Redis 에서 경매 로그 데이터 삭제
        redisTemplate.delete(logKey);
        redisTemplate.delete(AUCTION_PARTICIPANT_KEY_PREFIX + auctionId);
    }

    private void addBidLog(Long auctionId, BidLogDTO bidLog) {
        String logKey = AUCTION_LOG_KEY_PREFIX + auctionId;
        HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();

        try {
            String logsJson = hashOps.get(logKey, "bidLogs");
            List<BidLogDTO> logs = logsJson != null ?
                    objectMapper.readValue(logsJson, new TypeReference<List<BidLogDTO>>() {
                    }) :
                    new ArrayList<>();

            logs.add(bidLog);
            String updatedLogsJson = objectMapper.writeValueAsString(logs);
            hashOps.put(logKey, "bidLogs", updatedLogsJson);
        } catch (JsonProcessingException e) {
            log.error("Failed to add bid log for auction ID: " + auctionId, e);
        }
    }

    private int getParticipantCount(long auctionId) {
        return participantCount.getOrDefault(auctionId, new AtomicInteger(0)).get();
    }

    private int sendParticipantCount(long auctionId) {
        int count = getParticipantCount(auctionId);
        messagingTemplate.convertAndSend("/topic/auction/" + auctionId + "/participants", count);
        return count;
    }
}
