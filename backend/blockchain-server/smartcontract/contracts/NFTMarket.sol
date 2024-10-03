// SPDX-License-Identifier: MIT
pragma solidity ^0.8.7;

import "./TradeBase.sol";

contract NFTMarket is TradeBase {
    struct Market {
        uint256 nftId;
        address seller;
        uint256 price;
    }

    mapping(uint256 => Market) public markets;

    event MarketCreated(uint256 indexed nftId, address indexed seller, uint256 price);
    event MarketCancelled(uint256 indexed nftId, address indexed seller);
    event MarketSold(uint256 indexed nftId, address indexed seller, address indexed buyer, uint256 price);

    // 판매 생성
    function createMarket(uint256 _nftId, uint256 price) external nonReentrant {
        require(nftContract.ownerOf(_nftId) == msg.sender, "You don't own this NFT");
        require(price > 0, "Price must be greater than zero");
        
        nftContract.transferNFTByAdmin(_nftId, msg.sender, address(this));

        markets[_nftId] = Market({
            nftId: _nftId,
            seller: msg.sender,
            price: price
        });

        emit MarketCreated(_nftId, msg.sender, price);
    }

    // 판매 취소
    function cancelMarket(uint256 _nftId) external nonReentrant {
        Market storage market = markets[_nftId];
        require(nftContract.ownerOf(_nftId) == address(this), "NFT not in market");
        require(market.seller == msg.sender, "You don't own this NFT");
        require(market.seller != address(0), "This market does not exist");

        nftContract.transferNFTByAdmin(_nftId, address(this), msg.sender);

        delete markets[_nftId];

        emit MarketCancelled(_nftId, msg.sender);
    }

    // 판매 구매
    function buyMarket(uint256 _nftId) external nonReentrant {
        Market storage market = markets[_nftId];
        require(market.seller != address(0), "This NFT is not for sale");
        require(msg.sender != market.seller, "You can't buy your own NFT");

        uint256 price = market.price;
        address seller = market.seller;

        require(getUserBalance(msg.sender) >= price, "Insufficient balance");

        _adjustBalances(msg.sender, seller, price);

        nftContract.transferNFTByAdmin(_nftId, address(this), msg.sender);

        delete markets[_nftId];

        emit MarketSold(_nftId, seller, msg.sender, price);
    }

    function getMarketInfo(uint256 _nftId) public view returns (Market memory) {
        Market memory market = markets[_nftId];
        require(market.seller != address(0), "Sell does not exist for this NFT");
        return market;
    }
}