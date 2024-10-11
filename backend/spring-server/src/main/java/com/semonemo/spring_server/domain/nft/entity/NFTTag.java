package com.semonemo.spring_server.domain.nft.entity;

import com.semonemo.spring_server.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "nft_tag")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NFTTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "ntag_id")
    private Ntags nTagId;

    @ManyToOne
    @JoinColumn(name = "nft_id")
    private NFTs nftId;
}
