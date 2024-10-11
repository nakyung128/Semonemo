// SPDX-License-Identifier: MIT
pragma solidity ^0.8.7;

import "./NFTAuction.sol";
import "./NFTMarket.sol";
import "./TradeBase.sol";

contract IntegratedNFTSystem is NFTAuction, NFTMarket {
    // 초기설정
    constructor(address _nftContractAddress, address _tokenContractAddress) {
        initializeContracts(_nftContractAddress, _tokenContractAddress);
        nftContract.setMainContractAddress(address(this));
        tokenContract.setMainContractAddress(address(this));
    }

    // 컨트랙트가 보유한 NFT (마켓이나 옥션에 올라온 NFT)
    function getAllContractOwnedNFTs() public view returns (uint256[] memory) {
        return getContractOwnedNFTs();
    }

    // 컨트랙트가 보유한 잔액 (유저들의 코인)
    function getTotalContractBalance() public view returns (uint256) {
        return getContractBalance();
    }

    // NFT와 잔액 한번에 제공
    function getCombinedOwnedAssets() public view returns (uint256[] memory nftIds, uint256 totalCoins) {
        uint256[] memory allNFTs = getAllContractOwnedNFTs();
        uint256 totalBalance = getTotalContractBalance();

        return (allNFTs, totalBalance);
    }
}