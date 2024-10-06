package com.semonemo.spring_server.domain.blockchain.service;

import com.semonemo.spring_server.domain.blockchain.dto.NFTInfoDto;
import com.semonemo.spring_server.domain.coin.dto.response.CoinResponseDto;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;
import java.util.List;

public interface BlockChainService {
    String getClientVersion() throws Exception;

    TransactionReceipt mintCoin(String to, BigInteger amount) throws Exception;

    TransactionReceipt cancelAuction(BigInteger tokenId) throws Exception;

    TransactionReceipt encAuction(String buyer, BigInteger tokenId) throws Exception;

    List<NFTInfoDto> getNFTsByIds(List<BigInteger> tokenIds) throws Exception;

    NFTInfoDto getNFTById(BigInteger tokenId) throws Exception;

    BigInteger getBalanceOf(String address) throws Exception;

    TransactionReceipt waitForTransactionReceipt(String transactionHash) throws Exception;

    BigInteger convertToSmallestUnit(BigInteger amount);

    Long convertFromSmallestUnit(BigInteger amount);
}

