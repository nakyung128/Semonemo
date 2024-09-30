// SPDX-License-Identifier: MIT
pragma solidity ^0.8.7;

import "./NFTFrame.sol";
import "./AhoraCoin.sol";
import "@openzeppelin/contracts/access/Ownable.sol";
import "@openzeppelin/contracts/security/ReentrancyGuard.sol";

// Base contract with common functionality
contract TradeBase is Ownable, ReentrancyGuard {
    NFTFrame public nftContract;
    AhoraCoin public tokenContract;

    // 사용자별 잔액을 추적하는 매핑
    mapping(address => uint256) private userBalances;

    event Deposit(address indexed user, uint256 amount);
    event Withdrawal(address indexed user, uint256 amount);
    event BalanceAdjusted(address indexed from, address indexed to, uint256 amount);
    event BalanceTransferred(address indexed from, address indexed to, uint256 amount);

    // 초기설정
    function initializeContracts(address _nftContractAddress, address _tokenContractAddress) internal {
        require(address(nftContract) == address(0) && address(tokenContract) == address(0), "Contracts already initialized");
        nftContract = NFTFrame(_nftContractAddress);
        tokenContract = AhoraCoin(_tokenContractAddress);
    }

    // 입금 함수
    function deposit(uint256 amount) external nonReentrant {
        require(amount > 0, "Deposit amount must be greater than 0");
        require(tokenContract.transferUserToSystem(msg.sender, address(this), amount), "Transfer failed");
        
        userBalances[msg.sender] += amount;
        emit Deposit(msg.sender, amount);
    }

    // 출금 함수
    function withdraw(uint256 amount) external nonReentrant {
        require(amount > 0, "Withdrawal amount must be greater than 0");
        require(userBalances[msg.sender] >= amount, "Insufficient balance");
        
        userBalances[msg.sender] -= amount;
        require(tokenContract.transferSenderToUser(msg.sender, amount), "Transfer failed");
        
        emit Withdrawal(msg.sender, amount);
    }

    // 잔액 조정
    function _adjustBalances(address from, address to, uint256 amount) internal {
        require(userBalances[from] >= amount, "Insufficient balance");
        userBalances[from] -= amount;
        userBalances[to] += amount;
        emit BalanceAdjusted(from, to, amount);
    }

    // 유저 간 잔액 이동
    function transferBalance(address to, uint256 amount) external nonReentrant {
        require(to != address(0), "Invalid recipient address");
        require(to != msg.sender, "Cannot transfer to yourself");
        require(amount > 0, "Transfer amount must be greater than 0");
        require(userBalances[msg.sender] >= amount, "Insufficient balance");

        userBalances[msg.sender] -= amount;
        userBalances[to] += amount;

        emit BalanceTransferred(msg.sender, to, amount);
    }

    // 컨트랙트 보유 NFT
    function getContractOwnedNFTs() public view returns (uint256[] memory) {
        return nftContract.getUserNFTIds(address(this)); 
    }

    // 컨트랙트 보유 잔액
    function getContractBalance() public view returns (uint256) {
        return tokenContract.balanceOf(address(this));
    }

    // 유저 거래가능 잔액
    function getUserBalance(address user) public view returns (uint256) {
        return userBalances[user];
    }
}