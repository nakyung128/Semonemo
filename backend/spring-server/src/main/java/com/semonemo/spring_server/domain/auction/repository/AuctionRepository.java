package com.semonemo.spring_server.domain.auction.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.semonemo.spring_server.domain.auction.entity.Auction;

import java.util.List;

public interface AuctionRepository extends JpaRepository<Auction, Long> {

    List<Auction> findAllByOrderByIdDesc();
}
