package com.semonemo.spring_server.domain.coin.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.semonemo.spring_server.domain.coin.entity.CoinPriceHistory;

public interface CoinHistoryRepository extends JpaRepository<CoinPriceHistory,Long> {

	CoinPriceHistory findByDate(LocalDate date);

	@Query(value = "SELECT * FROM (SELECT * FROM coin_price_history ORDER BY date DESC LIMIT 7) AS c ORDER BY date", nativeQuery = true)
	List<CoinPriceHistory> findTop7ByOrderByDateAsc();
}
