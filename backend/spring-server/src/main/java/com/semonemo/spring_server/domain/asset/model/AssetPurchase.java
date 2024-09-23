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
@Entity(name = "asset_purchase")
public class AssetPurchase {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "asset_purchase_id")
	private Long assetPurchaseId;

	@Column(name = "asset_id")
	private Long assetId;

	@Column(name = "user_id")
	private Long userId;

	@Column(name = "created_at")
	private LocalDateTime createdAt;


	@PrePersist
	protected void onCreate() {
		createdAt = LocalDateTime.now();
	}
}
