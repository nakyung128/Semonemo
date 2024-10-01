package com.semonemo.spring_server.domain.blockchain.service;

import com.semonemo.spring_server.domain.blockchain.dto.NFTInfoDto;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;
import java.util.List;

public interface BlockChainService {
    String getClientVersion() throws Exception;

    TransactionReceipt mintCoin(String to, BigInteger amount) throws Exception;

    List<NFTInfoDto> getNFTsByIds(List<BigInteger> tokenIds) throws Exception;

    TransactionReceipt waitForTransactionReceipt(String transactionHash) throws Exception;
}
