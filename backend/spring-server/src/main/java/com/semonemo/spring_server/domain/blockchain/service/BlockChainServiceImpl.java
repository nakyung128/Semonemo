package com.semonemo.spring_server.domain.blockchain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;

import org.web3j.crypto.RawTransaction;
import org.web3j.protocol.Web3j;
import org.web3j.tx.RawTransactionManager;
import org.web3j.utils.Convert;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Function;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;

import org.json.JSONArray;
import org.json.JSONObject;

import com.semonemo.spring_server.global.exception.CustomException;
import com.semonemo.spring_server.global.exception.ErrorCode;

@Service
public class BlockChainServiceImpl implements BlockChainService {
    private final Web3j web3j;
    private final String coinContractAddress;
    private final String nftContractAddress;
    private final String systemContractAddress;
    private final String adminAddress;
    private final String adminPrivateKey;

    @Autowired
    public BlockChainServiceImpl(Web3j web3j) {
        this.web3j = web3j;
        this.coinContractAddress = "0x7dC302d7D99273Cab00C3046599108F605CCB55c"; // 예시 주소
        this.nftContractAddress = "0x1234567890123456789012345678901234567890"; // 예시 주소
        this.systemContractAddress = "0x1234567890123456789012345678901234567890"; // 예시 주소
        this.adminAddress = "0xF17ce10D8c13f97Fd6Db4fCB05F7877512098337"; // 예시 주소
        this.adminPrivateKey = "0x746b86dcdb199524b77523d43bfb56f0e1b73cae738e66a6bfce4748072ee95c"; // 예시 키
    }

    @Override
    public String getClientVersion() throws Exception {
        Web3ClientVersion web3ClientVersion = web3j.web3ClientVersion().send();
        return web3ClientVersion.getWeb3ClientVersion();
    }

    @Override
    public TransactionReceipt mintCoin(String to, BigInteger amount) throws Exception {
        Function function = new Function(
            "mint",
            Arrays.asList(
                new org.web3j.abi.datatypes.Address(to),
                new org.web3j.abi.datatypes.generated.Uint256(amount)
            ),
            Collections.emptyList()
        );

        String encodedFunction = FunctionEncoder.encode(function);

        Credentials credentials = Credentials.create(adminPrivateKey);

        // gasPrice와 gasLimit를 0으로 설정
        BigInteger gasPrice = BigInteger.ZERO;
        BigInteger gasLimit = BigInteger.ZERO;

        // nonce 값을 가져옴
        BigInteger nonce = web3j.ethGetTransactionCount(adminAddress, org.web3j.protocol.core.DefaultBlockParameterName.LATEST).send().getTransactionCount();

        // RawTransaction 생성
        RawTransaction rawTransaction = RawTransaction.createTransaction(
            nonce,
            gasPrice,
            gasLimit,
            coinContractAddress,
            encodedFunction
        );

        // RawTransactionManager를 사용하여 트랜잭션 서명 및 전송
        RawTransactionManager transactionManager = new RawTransactionManager(web3j, credentials);
        String transactionHash = transactionManager.signAndSend(rawTransaction).getTransactionHash();

        // 트랜잭션 전송 및 영수증 반환
        return transactionManager.sendTransaction(
            gasPrice,
            gasLimit,
            coinContractAddress,
            encodedFunction,
            BigInteger.ZERO
        ).send();
    }
}
