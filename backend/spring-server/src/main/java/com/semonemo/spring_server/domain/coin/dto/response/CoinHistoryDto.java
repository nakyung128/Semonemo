package com.semonemo.spring_server.domain.coin.dto.response;

import java.time.LocalDate;

public record CoinHistoryDto(
	LocalDate date,
	Long averagePrice,
	Double dailyChange,
	Long highestPrice,
	Long lowestPrice
) {
}
