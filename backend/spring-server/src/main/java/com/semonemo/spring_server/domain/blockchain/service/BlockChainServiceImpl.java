package com.semonemo.spring_server.domain.blockchain.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.semonemo.spring_server.domain.blockchain.dto.IPFSHashDto;
import com.semonemo.spring_server.domain.blockchain.dto.NFTInfoDto;
import com.semonemo.spring_server.domain.blockchain.dto.event.TradeEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.web.util.UriComponentsBuilder;
import org.web3j.abi.*;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.tx.Contract;
import org.web3j.utils.Numeric;

import com.semonemo.spring_server.domain.blockchain.dto.decoded.NFTInfo;
import com.semonemo.spring_server.global.exception.CustomException;
import com.semonemo.spring_server.global.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class BlockChainServiceImpl implements BlockChainService {

    private static final BigInteger UNIT_CONVERSION_FACTOR = BigInteger.TEN.pow(18);

    @Value("${blockchain.baseurl}")
    private String BASE_IPFS_URL;

    @Value("${blockchain.coinContractAddress}")
    private String coinContractAddress;

    @Value("${blockchain.nftContractAddress}")
    private String nftContractAddress;

    @Value("${blockchain.systemContractAddress}")
    private String systemContractAddress;

    @Value("${blockchain.adminAddress}")
    private String adminAddress;

    @Value("${blockchain.adminPrivateKey}")
    private String adminPrivateKey;

    private final Web3j web3j;
    private final RestTemplate restTemplate;

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
                        new Address(to),
                        new Uint256(amount)
                ),
                Collections.emptyList()
        );

        String encodedFunction = FunctionEncoder.encode(function);

        Credentials credentials = Credentials.create(adminPrivateKey);

        BigInteger gasPrice = web3j.ethGasPrice().send().getGasPrice();
        BigInteger gasLimit = BigInteger.valueOf(300000); // 예상 가스 한도

        BigInteger nonce = web3j.ethGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.LATEST).send().getTransactionCount();

        RawTransaction rawTransaction = RawTransaction.createTransaction(
                nonce,
                gasPrice,
                gasLimit,
                coinContractAddress,
                encodedFunction
        );

        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signedMessage);

        EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).send();

        if (ethSendTransaction.hasError()) {
            throw new CustomException(ErrorCode.BLOCKCHAIN_ERROR);
        }

        String transactionHash = ethSendTransaction.getTransactionHash();

        return waitForTransactionReceipt(transactionHash);
    }

    @Override
    public TransactionReceipt cancelAuction(BigInteger tokenId) throws Exception {
        Function function = new Function(
                "cancelAuction",
                Collections.singletonList(new Uint256(tokenId)),
                Collections.emptyList()
        );

        String encodedFunction = FunctionEncoder.encode(function);

        Credentials credentials = Credentials.create(adminPrivateKey);

        BigInteger gasPrice = web3j.ethGasPrice().send().getGasPrice();
        BigInteger gasLimit = BigInteger.valueOf(300000); // 예상 가스 한도

        BigInteger nonce = web3j.ethGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.LATEST).send().getTransactionCount();

        RawTransaction rawTransaction = RawTransaction.createTransaction(
                nonce,
                gasPrice,
                gasLimit,
                systemContractAddress,
                encodedFunction
        );

        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signedMessage);

        EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).send();

        if (ethSendTransaction.hasError()) {
            throw new CustomException(ErrorCode.BLOCKCHAIN_ERROR);
        }

        String transactionHash = ethSendTransaction.getTransactionHash();

        return waitForTransactionReceipt(transactionHash);
    }

    @Override
    public BigInteger endAuction(String buyer, BigInteger tokenId, BigInteger amount) throws Exception {
        Function function = new Function(
            "closeAuction",
            Arrays.asList(
                new Uint256(tokenId),
                new Address(buyer),
                new Uint256(amount)
            ),
            Collections.emptyList()
        );

        String encodedFunction = FunctionEncoder.encode(function);

        Credentials credentials = Credentials.create(adminPrivateKey);

        BigInteger gasPrice = web3j.ethGasPrice().send().getGasPrice();
        BigInteger gasLimit = BigInteger.valueOf(300000); // 예상 가스 한도


        BigInteger nonce = web3j.ethGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.LATEST).send().getTransactionCount();

        RawTransaction rawTransaction = RawTransaction.createTransaction(
                nonce,
                gasPrice,
                gasLimit,
                systemContractAddress,
                encodedFunction
        );

        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signedMessage);

        EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).send();

        if (ethSendTransaction.hasError()) {
            throw new CustomException(ErrorCode.BLOCKCHAIN_ERROR);
        }

        String transactionHash = ethSendTransaction.getTransactionHash();

        TransactionReceipt transactionResult = waitForTransactionReceipt(transactionHash);

        BigInteger tradeId = null;

        if (Objects.equals(transactionResult.getStatus(), "0x1")) {
            for (org.web3j.protocol.core.methods.response.Log txLog : transactionResult.getLogs()) {
                String recordEventHash = EventEncoder.encode(TradeEvent.TRADE_RECORDED_EVENT);
                if (txLog.getTopics().get(0).equals(recordEventHash)) {
                    EventValues eventValues = Contract.staticExtractEventParameters(
                            TradeEvent.TRADE_RECORDED_EVENT, txLog
                    );

                    if (eventValues != null) {
                        List<Type> indexedValues = eventValues.getIndexedValues();
                        ;
                        tradeId = (BigInteger) indexedValues.get(0).getValue();
                    } else {
                        throw new CustomException(ErrorCode.BLOCKCHAIN_ERROR);
                    }
                }
            }
        } else {
            throw new CustomException(ErrorCode.BLOCKCHAIN_ERROR);
        }

        return tradeId;
    }

    @Override
    public List<NFTInfoDto> getNFTsByIds(List<BigInteger> tokenIds) throws Exception {
        List<Uint256> uint256TokenIds = tokenIds.stream()
                .map(Uint256::new)
                .collect(Collectors.toList());

        Function function = new Function(
                "getNFTsByIds",
                Collections.singletonList(new DynamicArray<>(Uint256.class, uint256TokenIds)),
                Collections.singletonList(new TypeReference<DynamicArray<NFTInfo>>() {
                })
        );

        String encodedFunction = FunctionEncoder.encode(function);

        Credentials credentials = Credentials.create(adminPrivateKey);

        EthCall ethCall = web3j.ethCall(
                Transaction.createEthCallTransaction(
                        credentials.getAddress(), nftContractAddress, encodedFunction
                ),
                DefaultBlockParameterName.LATEST
        ).send();

        String rawValue = ethCall.getValue();

        if (ethCall.hasError()) {
            throw new Exception("Error calling contract: " + ethCall.getError().getMessage());
        }

        List<NFTInfoDto> result = new ArrayList<>();

        try {
            List<Type> decoded = FunctionReturnDecoder.decode(rawValue, function.getOutputParameters());

            if (decoded.isEmpty() || !(decoded.get(0) instanceof DynamicArray)) {
                throw new Exception("Unexpected response format");
            }

            DynamicArray<NFTInfo> nftArray = (DynamicArray<NFTInfo>) decoded.get(0);

            for (NFTInfo nft : nftArray.getValue()) {
                JsonNode tokenData = fetchTokenURIData(new String(nft.tokenURI.getValue(), StandardCharsets.UTF_8));

                NFTInfoDto nftInfoDto = new NFTInfoDto();
                nftInfoDto.setTokenId(nft.tokenId.getValue());
                nftInfoDto.setCreator(nft.creator.toString());
                nftInfoDto.setCurrentOwner(nft.currentOwner.toString());

                IPFSHashDto ipfsHashDto = new IPFSHashDto();
                if (tokenData != null) {
                    ipfsHashDto.setContent(tokenData.get("content").asText());
                    ipfsHashDto.setImage(tokenData.get("image").asText());
                    ipfsHashDto.setTitle(tokenData.get("title").asText());
                    nftInfoDto.setData(ipfsHashDto);
                } else {
                    nftInfoDto.setData(null); // 기존 방식으로 fallback
                }
                result.add(nftInfoDto);
            }
        } catch (Exception e) {
            throw new Exception("Failed to decode contract response", e);
        }

        return result;
    }

    @Override
    public NFTInfoDto getNFTById(BigInteger tokenId) throws Exception {
        Function function = new Function(
                "getNFTInfo",
                Collections.singletonList(new Uint256(tokenId)),
                List.of(new TypeReference<NFTInfo>() {
                })
        );

        String encodedFunction = FunctionEncoder.encode(function);

        Credentials credentials = Credentials.create(adminPrivateKey);

        EthCall ethCall = web3j.ethCall(
                Transaction.createEthCallTransaction(
                        credentials.getAddress(), nftContractAddress, encodedFunction
                ),
                DefaultBlockParameterName.LATEST
        ).send();

        String rawValue = ethCall.getValue();

        if (ethCall.hasError()) {
            throw new Exception("Error calling contract: " + ethCall.getError().getMessage());
        }

        NFTInfoDto result = new NFTInfoDto();

        try {
            List<Type> decoded = FunctionReturnDecoder.decode(rawValue, function.getOutputParameters());

            if (decoded.isEmpty() || !(decoded.get(0) instanceof NFTInfo nftInfo)) {
                throw new Exception("Unexpected response format");
            }

            JsonNode tokenData = fetchTokenURIData(new String(nftInfo.tokenURI.getValue(), StandardCharsets.UTF_8));

            result.setTokenId(nftInfo.tokenId.getValue());
            result.setCreator(nftInfo.creator.toString());
            result.setCurrentOwner(nftInfo.currentOwner.toString());

            IPFSHashDto ipfsHashDto = new IPFSHashDto();
            if (tokenData != null) {
                ipfsHashDto.setContent(tokenData.get("content").asText());
                ipfsHashDto.setImage(tokenData.get("image").asText());
                ipfsHashDto.setTitle(tokenData.get("title").asText());
                result.setData(ipfsHashDto);
            } else {
                result.setData(null); // 기존 방식으로 fallback
            }
        } catch (Exception e) {
            throw new Exception("Failed to decode contract response", e);
        }

        return result;
    }

    @Override
    public BigInteger getBalanceOf(String address) throws Exception {
        Function function = new Function(
                "balanceOf",
                List.of(new Address(address)),
                List.of(new TypeReference<Uint256>() {
                })
        );

        String encodedFunction = FunctionEncoder.encode(function);

        EthCall ethCall = web3j.ethCall(
                Transaction.createEthCallTransaction(
                        null, coinContractAddress, encodedFunction
                ),
                DefaultBlockParameterName.LATEST
        ).send();

        if (ethCall.hasError()) {
            throw new Exception("Error calling contract: " + ethCall.getError().getMessage());
        }

        List<Type> decoded = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());

        if (!decoded.isEmpty()) {
            return ((Uint256) decoded.get(0)).getValue();
        } else {
            throw new Exception("Unexpected empty response");
        }
    }

    // 1초마다 트랜잭션 확인, 40초까지. 결과 확인을 위한 함수
    @Override
    public TransactionReceipt waitForTransactionReceipt(String transactionHash) throws Exception {
        Optional<TransactionReceipt> receiptOptional;
        int attempts = 0;
        do {
            EthGetTransactionReceipt transactionReceipt = web3j.ethGetTransactionReceipt(transactionHash).send();
            receiptOptional = transactionReceipt.getTransactionReceipt();

            if (receiptOptional.isPresent()) {
                return receiptOptional.get();
            }

            Thread.sleep(1000);
            attempts++;
        } while (attempts < 30);

        throw new CustomException(ErrorCode.BLOCKCHAIN_ERROR);
    }

    @Override
    public BigInteger convertToSmallestUnit(BigInteger amount) {
        return amount.multiply(UNIT_CONVERSION_FACTOR);
    }

    @Override
    public Long convertFromSmallestUnit(BigInteger amount) {
        return amount.divide(UNIT_CONVERSION_FACTOR).longValue();
    }


    public JsonNode fetchTokenURIData(String tokenURI) {
        String fullUrl = UriComponentsBuilder.fromUriString(BASE_IPFS_URL)
                .path(tokenURI)
                .build()
                .toUriString();

        String jsonString = restTemplate.getForObject(fullUrl, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readTree(jsonString);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.BLOCKCHAIN_ERROR);
        }
    }
}