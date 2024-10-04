package com.semonemo.spring_server.domain.coin.repository;

import com.semonemo.spring_server.domain.coin.entity.TradeLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeLogRepository extends JpaRepository<TradeLog, Long>, TradeLogRepositoryCustom {
}
