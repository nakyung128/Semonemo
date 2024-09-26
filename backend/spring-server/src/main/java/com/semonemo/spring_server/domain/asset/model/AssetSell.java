package com.semonemo.spring_server.domain.asset.model;

import com.semonemo.spring_server.global.common.BaseTimeEntity;

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
public class AssetSell extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long Id;

	@Column(name = "asset_id")
	private Long assetId;

	@Column(name = "price")
	private Long price;

	@Column(name = "hits")
	private Long hits;

	@Column(name = "like_count")
	private Long likeCount;

	@PrePersist
	protected void onCreate() {
		hits = 0L;
		likeCount = 0L;
	}
}
