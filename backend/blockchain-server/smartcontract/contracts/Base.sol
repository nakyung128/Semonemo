// SPDX-License-Identifier: MIT
pragma solidity ^0.8.7;

import "./NFT.sol";
import "./Coin.sol";
import "@openzeppelin/contracts/access/Ownable.sol";
import "@openzeppelin/contracts/security/ReentrancyGuard.sol";

// Base contract with common functionality
contract NFTBase is Ownable, ReentrancyGuard {
    NFTFrame public nftContract;
    AhoraCoin public tokenContract;

    function initializeContracts(address _nftContractAddress, address _tokenContractAddress) internal {
        require(address(nftContract) == address(0) && address(tokenContract) == address(0), "Contracts already initialized");
        nftContract = NFTFrame(_nftContractAddress);
        tokenContract = AhoraCoin(_tokenContractAddress);
    }

    function getContractOwnedNFTs() public view returns (uint256[] memory) {
        return nftContract.getUserNFTIds(address(this));
    }

    function getContractBalance() public view returns (uint256) {
        return tokenContract.balanceOf(address(this));
    }
}