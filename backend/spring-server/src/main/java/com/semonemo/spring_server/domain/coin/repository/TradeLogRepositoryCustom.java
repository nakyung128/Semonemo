package com.semonemo.spring_server.domain.coin.repository;

import com.semonemo.spring_server.domain.coin.entity.TradeLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TradeLogRepositoryCustom {
    Page<TradeLog> findByUser(Long userId, Pageable pageable);
}
