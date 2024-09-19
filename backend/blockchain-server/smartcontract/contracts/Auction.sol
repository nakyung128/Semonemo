// SPDX-License-Identifier: MIT
pragma solidity ^0.8.7;

import "./Base.sol";

contract NFTAuction is NFTBase {
    struct Auction {
        uint256 nftId;
        address seller;
        uint256 startingPrice;
        uint256 highestBid;
        address highestBidder;
        bool isFinished;
    }

    mapping(uint256 => Auction) public auctions;

    event AuctionCreated(uint256 indexed nftId, address indexed seller, uint256 startingPrice);
    event BidPlaced(uint256 indexed nftId, address indexed bidder, uint256 bidAmount);
    event AuctionFinalized(uint256 indexed nftId, address indexed winner, uint256 winningBid);

    function createAuction(uint256 _nftId, uint256 _startingPrice) external nonReentrant {
        require(nftContract.ownerOf(_nftId) == msg.sender, "You don't own this NFT");
        require(_startingPrice > 0, "Starting price must be greater than zero");
        
        nftContract.transferNFTUserToSystem(_nftId, msg.sender, address(this));

        auctions[_nftId] = Auction({
            nftId: _nftId,
            seller: msg.sender,
            startingPrice: _startingPrice,
            highestBid: 0,
            highestBidder: address(0),
            isFinished: false
        });

        emit AuctionCreated(_nftId, msg.sender, _startingPrice);
    }

    function placeBid(uint256 _nftId, uint256 _bidAmount) external nonReentrant {
        Auction storage auction = auctions[_nftId];
        require(!auction.isFinished, "Auction is already finished");
        require(_bidAmount > auction.highestBid, "Bid must be higher than current highest bid");
        require(_bidAmount >= auction.startingPrice, "Bid must be at least the starting price");

        if (auction.highestBidder != address(0)) {
            require(tokenContract.transferSenderToUser(auction.highestBidder, auction.highestBid), "Refund transfer failed");
        }

        require(tokenContract.transferUserToSystem(msg.sender, address(this), _bidAmount), "Bid transfer failed");

        auction.highestBid = _bidAmount;
        auction.highestBidder = msg.sender;

        emit BidPlaced(_nftId, msg.sender, _bidAmount);
    }

    function finalizeAuction(uint256 _nftId) external onlyOwner nonReentrant {
        Auction storage auction = auctions[_nftId];
        require(!auction.isFinished, "Auction is already finished");

        auction.isFinished = true;

        if (auction.highestBidder != address(0)) {
            require(nftContract.transferNFT(_nftId, auction.highestBidder), "NFT transfer failed");
            require(tokenContract.transferSenderToUser(auction.seller, auction.highestBid), "Transfer to seller failed");
            emit AuctionFinalized(_nftId, auction.highestBidder, auction.highestBid);
        } else {
            require(nftContract.transferNFT(_nftId, auction.seller), "NFT transfer failed");
        }
    }

    function getAuctionInfo(uint256 _nftId) public view returns (Auction memory) {
        Auction memory auction = auctions[_nftId];
        require(auction.seller != address(0), "Auction does not exist for this NFT");
        return auction;
    }
}