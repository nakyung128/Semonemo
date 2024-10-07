package com.semonemo.spring_server.domain.coin.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.semonemo.spring_server.domain.coin.entity.CoinPriceHistory;

public interface CoinHistoryRepository extends JpaRepository<CoinPriceHistory,Long> {

	CoinPriceHistory findByDate(LocalDate date);
	List<CoinPriceHistory> findTop7ByOrderByDateAsc();
}
