// SPDX-License-Identifier: MIT
pragma solidity ^0.8.7;

import "./NFT.sol";
import "./Coin.sol";
import "@openzeppelin/contracts/access/Ownable.sol";
import "@openzeppelin/contracts/security/ReentrancyGuard.sol";

contract NFTAuction is Ownable, ReentrancyGuard {
    NFTFrame public nftContract;
    AhoraCoin public tokenContract;

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

    constructor(address _nftContractAddress, address _tokenContractAddress) {
        nftContract = NFTFrame(_nftContractAddress);
        tokenContract = AhoraCoin(_tokenContractAddress);
    }

    // 경매 생성
    function createAuction(uint256 _nftId, uint256 _startingPrice) external nonReentrant {
        require(nftContract.ownerOf(_nftId) == msg.sender, "You don't own this NFT");
        require(_startingPrice > 0, "Starting price must be greater than zero");
        
        nftContract.transferNFTUserToUser(_nftId, msg.sender, address(this));

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

    // 경매 입찰
    function placeBid(uint256 _nftId, uint256 _bidAmount) external nonReentrant {
        Auction storage auction = auctions[_nftId];
        require(!auction.isFinished, "Auction is already finished");
        require(_bidAmount > auction.highestBid, "Bid must be higher than current highest bid");
        require(_bidAmount >= auction.startingPrice, "Bid must be at least the starting price");

        if (auction.highestBidder != address(0)) {
            // Automatically refund the previous highest bidder
            require(tokenContract.transferSenderToUser(auction.highestBidder, auction.highestBid), "Refund transfer failed");
        }

        require(tokenContract.transferUserToUser(msg.sender, address(this), _bidAmount), "Bid transfer failed");

        auction.highestBid = _bidAmount;
        auction.highestBidder = msg.sender;

        emit BidPlaced(_nftId, msg.sender, _bidAmount);
    }

    // 경매 종료
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

    // 경매정보 조회
    function getAuctionInfo(uint256 _nftId) public view returns (Auction memory) {
        return auctions[_nftId];
    }
}