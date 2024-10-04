package com.semonemo.spring_server.domain.auction.entity;

import java.time.LocalDateTime;

import com.semonemo.spring_server.global.common.BaseTimeEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "auctions")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Auction extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	// TODO : NFT 와 mapping
	private Long nftId;

	@Setter
	@Enumerated(EnumType.STRING)
	private AuctionStatus status;

	private Long startPrice;

	// TODO : 낙찰자 Id 와 mapping
	private Long winner;

	private Long finalPrice;

	@Setter
	private LocalDateTime startTime;

	@Setter
	private LocalDateTime endTime;

	public void updateResult(Long winner, Long finalPrice, LocalDateTime endTime) {
		this.winner = winner;
		this.finalPrice = finalPrice;
		this.endTime = endTime;
		this.status = AuctionStatus.END;
	}
}
