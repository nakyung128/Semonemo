package com.semonemo.spring_server.domain.nft.entity;

import java.math.BigInteger;
import java.util.Set;

import com.semonemo.spring_server.domain.user.entity.Users;
import com.semonemo.spring_server.global.common.BaseTimeEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "nfts")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NFTs extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nft_id")
    private Long nftId;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private Users creator;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Users owner;

    @Column(name = "token_id", unique = true)
    private BigInteger tokenId;

    @Column(name = "is_open")
    private Boolean isOpen;

    @Column(name = "is_on_sale")
    private Boolean isOnSale;

    @OneToMany(mappedBy = "nftId")
    private Set<NFTMarket> createdNFTMarkets;

    @OneToMany(mappedBy = "nftId")
    private Set<NFTTag> tags;

    public void changeOwner(Users newOwner) {
        this.owner = newOwner;
    }

    public void toggleOnSale(boolean onSale) {
        this.isOnSale = onSale;
    }

    public void toggleOpen() {
        this.isOpen = !this.isOpen;
    }
}
