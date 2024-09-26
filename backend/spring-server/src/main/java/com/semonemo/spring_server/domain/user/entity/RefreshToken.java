package com.semonemo.spring_server.domain.user.entity;

import com.semonemo.spring_server.global.common.BaseTimeEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "refresh_tokens")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String refreshToken;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private Users user;

	public void modify(String refreshToken) {
		this.refreshToken = refreshToken;
	}
}
