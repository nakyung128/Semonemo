// SPDX-License-Identifier: MIT
pragma solidity ^0.8.7;

import "./TradeBase.sol";

contract NFTAuction is TradeBase {
    struct Auction {
        uint256 nftId;
        address seller;
    }

    mapping(uint256 => Auction) public auctions;

    event AuctionStarted(uint256 indexed nftId, address indexed seller);
    event AuctionCancelled(uint256 indexed nftId, address indexed seller);
    event AuctionClosed(uint256 indexed nftId, address indexed seller, address indexed buyer, uint256 price);

    // 옥션에 물건 올리기
    function startAuction(uint256 _nftId) external nonReentrant {
        require(nftContract.ownerOf(_nftId) == msg.sender, "You don't own this NFT");
        
        nftContract.transferNFTByAdmin(_nftId, msg.sender, address(this));

        auctions[_nftId] = Auction({
            nftId: _nftId,
            seller: msg.sender
        });

        emit AuctionStarted(_nftId, msg.sender);
    }

    // 옥션에 올린 물건 취소
    function cancelAuction(uint256 _nftId) external nonReentrant onlyOwner {
        Auction storage auction = auctions[_nftId];
        require(nftContract.ownerOf(_nftId) == address(this), "NFT not in Auction");
        require(auction.seller != address(0), "This auction does not exist");
        
        nftContract.transferNFTByAdmin(_nftId, address(this), auction.seller);

        delete auctions[_nftId];

        emit AuctionCancelled(_nftId, auction.seller);
    }

    // 옥션 물건 구매 (경매 종료)
    function closeAuction(uint256 _nftId, address buyer, uint256 amount) external nonReentrant onlyOwner {
        Auction storage auction = auctions[_nftId];
        require(auction.seller != address(0), "This auction does not exist");
        require(buyer != auction.seller, "Seller cannot be the buyer");

        address seller = auction.seller;

        require(getUserBalance(buyer) >= amount, "Insufficient balance");

        _adjustBalances(buyer, seller, amount);

        nftContract.transferNFTByAdmin(_nftId, address(this), buyer);

        delete auctions[_nftId];

        emit AuctionClosed(_nftId, seller, buyer, amount);
    }

    function getAuctionInfo(uint256 _nftId) public view returns (Auction memory) {
        Auction memory auction = auctions[_nftId];
        require(auction.seller != address(0), "Auction does not exist for this NFT");
        return auction;
    }
}