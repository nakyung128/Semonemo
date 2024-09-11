// SPDX-License-Identifier: MIT
pragma solidity ^0.8.7;

import "./NFT.sol";
import "./Coin.sol";
import "@openzeppelin/contracts/access/Ownable.sol";
import "@openzeppelin/contracts/security/ReentrancyGuard.sol";

contract NFTMarket is Ownable, ReentrancyGuard {
    NFTFrame public nftContract;
    AhoraCoin public tokenContract;

    struct Frame {
        uint256 nftId;
        address seller;
        uint256 price;
        bool isFinished;
    }

    mapping(uint256 => Frame) public frames;

    event SellCreated(uint256 indexed nftId, address indexed seller, uint256 price);
    event SellCancelled(uint256 indexed nftId, address indexed seller);
    event FrameSold(uint256 indexed nftId, address indexed seller, address indexed buyer, uint256 price);

    constructor(address _nftContractAddress, address _tokenContractAddress) {
        nftContract = NFTFrame(_nftContractAddress);
        tokenContract = AhoraCoin(_tokenContractAddress);
    }

    // 판매 생성
    function createSell(uint256 _nftId, uint256 price) external nonReentrant {
        require(nftContract.ownerOf(_nftId) == msg.sender, "You don't own this NFT");
        require(price > 0, "Price must be greater than zero");
        
        nftContract.transferNFTUserToUser(_nftId, msg.sender, address(this));

        frames[_nftId] = Frame({
            nftId: _nftId,
            seller: msg.sender,
            price: price,
            isFinished: false
        });

        emit SellCreated(_nftId, msg.sender, price);
    }

    // 판매 취소
    function cancleSell(uint256 _nftId) external nonReentrant {
        Frame storage frame = frames[_nftId];
        require(nftContract.ownerOf(_nftId) == address(this), "NFT not in market");
        require(frame.seller == msg.sender, "You don't own this NFT");
        require(!frame.isFinished, "This Sell is already finished");
        
        frame.isFinished = true;

        nftContract.transferNFT(_nftId, msg.sender);

        emit SellCancelled(_nftId, msg.sender);
    }

    // 프레임 구매
    function buyFrame(uint256 _nftId) external nonReentrant {
        Frame storage frame = frames[_nftId];
        require(!frame.isFinished, "This sale is already finished");
        require(msg.sender != frame.seller, "You can't buy your own NFT");

        uint256 price = frame.price;
        address seller = frame.seller;

        require(tokenContract.balanceOf(msg.sender) >= price, "Insufficient token balance");

        frame.isFinished = true;

        require(tokenContract.transferUserToUser(msg.sender, address(this), price), "Token transfer failed");
        require(nftContract.transferNFT(_nftId, msg.sender), "NFT transfer failed");
        require(tokenContract.transferSenderToUser(seller, price), "Token transfer to seller failed");

        emit FrameSold(_nftId, seller, msg.sender, price);
    }

    // 판매 정보 조회
    function getSellInfo(uint256 _nftId) public view returns (Frame memory) {
        return frames[_nftId];
    }
}