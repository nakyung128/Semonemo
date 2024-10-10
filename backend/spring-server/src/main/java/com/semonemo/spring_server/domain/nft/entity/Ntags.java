package com.semonemo.spring_server.domain.nft.entity;

import com.semonemo.spring_server.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "ntags")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Ntags extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long Id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "nTagId")
    private Set<NFTTag> nftTags;
}
