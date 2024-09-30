package com.semonemo.spring_server.domain.blockchain.service;

import com.semonemo.spring_server.domain.blockchain.dto.NFTInfoDto;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;
import java.util.List;

public interface BlockChainService {
    String getClientVersion() throws Exception;

    public TransactionReceipt mintCoin(String to, BigInteger amount) throws Exception;

    public List<NFTInfoDto> getNFTsByIds(List<BigInteger> tokenIds) throws Exception;
}
