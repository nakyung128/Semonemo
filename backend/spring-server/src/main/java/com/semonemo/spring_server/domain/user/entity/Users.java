package com.semonemo.spring_server.domain.user.entity;

import java.math.BigInteger;
import java.util.Set;

import com.semonemo.spring_server.domain.coin.entity.TradeLog;
import com.semonemo.spring_server.domain.nft.entity.NFTs;
import com.semonemo.spring_server.domain.nft.entity.NFTMarket;
import com.semonemo.spring_server.domain.nft.entity.NFTMarketLike;
import com.semonemo.spring_server.global.common.BaseTimeEntity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "users")
@Getter
@Builder
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Users extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String address;
	@Setter
	private String password;
	private String nickname;
	private String profileImage;

	@Column(columnDefinition = "BIGINT DEFAULT 0")
    private Long balance;

	@OneToMany(mappedBy = "fromUser")
	private Set<Follow> following;

	@OneToMany(mappedBy = "toUser")
	private Set<Follow> followers;

	@OneToMany(mappedBy = "creator")
	private Set<NFTs> createdNFTs;

	@OneToMany(mappedBy = "owner")
	private Set<NFTs> ownedNFTs;

	@OneToMany(mappedBy = "seller")
	private Set<NFTMarket> selledNFTMarkets;

	@OneToMany(mappedBy = "likedUserId")
	private Set<NFTMarketLike> createdNFTMarketLikes;

    @OneToMany(mappedBy = "fromUser")
    private Set<TradeLog> tradeFrom;

    @OneToMany(mappedBy = "toUser")
    private Set<TradeLog> tradeTo;

	public void modify(String nickname) {
		this.nickname = nickname;
		this.profileImage = profileImage;
	}

	public void modify(String nickname, String profileImage) {
		this.nickname = nickname;
		this.profileImage = profileImage;
	}

    public void plusBalance(Long amount) {
        this.balance += amount;
    }

    public void minusBalance(Long amount) {
        this.balance -= amount;
    }
}
