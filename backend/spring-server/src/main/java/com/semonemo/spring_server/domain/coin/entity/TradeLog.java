package com.semonemo.spring_server.domain.coin.entity;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Set;

import com.semonemo.spring_server.domain.user.entity.Users;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "trade_log")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TradeLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Long logId;

    // Contract 조회를 위한 Id
    @Column(name = "trade_id")
    private BigInteger tradeId;

    @ManyToOne
    @JoinColumn(name = "from_user_id")
    private Users fromUser;

    @ManyToOne
    @JoinColumn(name = "to_user_id")
    private Users toUser;

    @Column(name = "trade_type")
    private String tradeType;

    @Column(name = "amount")
    private Long amount;
}
