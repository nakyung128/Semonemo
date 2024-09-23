package com.semonemo.spring_server.domain.asset.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
@Entity(name = "asset_sell")
public class AssetSell {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "asset_sell_id")
	private Long assetSellId;

	@Column(name = "asset_id")
	private Long assetId;

	@Column(name = "price")
	private Long price;

	@Column(name = "hits")
	private Long hits;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "like_count")
	private Long likeCount;

	@PrePersist
	protected void onCreate() {
		createdAt = LocalDateTime.now();
		hits = 0L;
		likeCount = 0L;
	}
}
