package com.semonemo.spring_server.domain.coin.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.semonemo.spring_server.domain.coin.entity.CoinPrice;

public interface CoinPriceRepository extends JpaRepository<CoinPrice, Long> {
	CoinPrice findTopByOrderByCreatedAtDesc();

	List<CoinPrice> findByCreatedAtBetween(LocalDateTime localDateTime, LocalDateTime localDateTime1);
}
