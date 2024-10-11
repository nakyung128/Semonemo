package com.semonemo.spring_server.domain.nft.entity;

import com.semonemo.spring_server.domain.user.entity.Users;
import com.semonemo.spring_server.global.common.BaseTimeEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "nft_market_like")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NFTMarketLike extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "market_like_id")
    private Long likeId;

    @ManyToOne
    @JoinColumn(name = "market_id")
    private NFTMarket marketId;

    @ManyToOne
    @JoinColumn(name = "liked_user_id")
    private Users likedUserId;
}
