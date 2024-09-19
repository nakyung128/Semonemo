// SPDX-License-Identifier: MIT
pragma solidity ^0.8.7;

import "./Base.sol";

contract NFTMarket is NFTBase {
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

    function createSell(uint256 _nftId, uint256 price) external nonReentrant {
        require(nftContract.ownerOf(_nftId) == msg.sender, "You don't own this NFT");
        require(price > 0, "Price must be greater than zero");
        
        nftContract.transferNFTUserToSystem(_nftId, msg.sender, address(this));

        frames[_nftId] = Frame({
            nftId: _nftId,
            seller: msg.sender,
            price: price,
            isFinished: false
        });

        emit SellCreated(_nftId, msg.sender, price);
    }

    function cancelSell(uint256 _nftId) external nonReentrant {
        Frame storage frame = frames[_nftId];
        require(nftContract.ownerOf(_nftId) == address(this), "NFT not in market");
        require(frame.seller == msg.sender, "You don't own this NFT");
        require(!frame.isFinished, "This Sell is already finished");
        
        frame.isFinished = true;

        nftContract.transferNFT(_nftId, msg.sender);

        emit SellCancelled(_nftId, msg.sender);
    }

    function buyFrame(uint256 _nftId) external nonReentrant {
        Frame storage frame = frames[_nftId];
        require(!frame.isFinished, "This sale is already finished");
        require(msg.sender != frame.seller, "You can't buy your own NFT");

        uint256 price = frame.price;
        address seller = frame.seller;

        require(tokenContract.balanceOf(msg.sender) >= price, "Insufficient token balance");

        frame.isFinished = true;

        require(tokenContract.transferUserToSystem(msg.sender, address(this), price), "Token transfer failed");
        require(nftContract.transferNFT(_nftId, msg.sender), "NFT transfer failed");
        require(tokenContract.transferSenderToUser(seller, price), "Token transfer to seller failed");

        emit FrameSold(_nftId, seller, msg.sender, price);
    }

    function getSellInfo(uint256 _nftId) public view returns (Frame memory) {
        Frame memory frame = frames[_nftId];
        require(frame.seller != address(0), "Sell does not exist for this NFT");
        return frame;
    }
}