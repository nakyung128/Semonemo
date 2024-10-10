package com.semonemo.spring_server.domain.auction.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuctionEndDTO {
    private long auctionId;
    private long finalPrice;
    private Long winner;
    private int anonym;
    private String endTime;
}