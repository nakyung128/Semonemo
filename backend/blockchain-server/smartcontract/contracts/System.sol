// SPDX-License-Identifier: MIT
pragma solidity ^0.8.7;

import "./Auction.sol";
import "./Market.sol";
import "./Base.sol";

contract IntegratedNFTSystem is NFTAuction, NFTMarket {
    constructor(address _nftContractAddress, address _tokenContractAddress) {
        initializeContracts(_nftContractAddress, _tokenContractAddress);
        nftContract.setMainContractAddress(address(this));
        tokenContract.setMainContractAddress(address(this));
    }

    function getAllContractOwnedNFTs() public view returns (uint256[] memory) {
        return getContractOwnedNFTs();
    }

    function getTotalContractBalance() public view returns (uint256) {
        return getContractBalance();
    }

    function getCombinedOwnedAssets() public view returns (uint256[] memory nftIds, uint256 totalCoins) {
        uint256[] memory allNFTs = getAllContractOwnedNFTs();
        uint256 totalBalance = getTotalContractBalance();

        return (allNFTs, totalBalance);
    }
}