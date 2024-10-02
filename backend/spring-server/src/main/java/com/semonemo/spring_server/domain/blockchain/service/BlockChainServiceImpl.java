package com.semonemo.spring_server.domain.blockchain.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.semonemo.spring_server.domain.blockchain.dto.IPFSHashDto;
import com.semonemo.spring_server.domain.blockchain.dto.NFTInfoDto;
import com.semonemo.spring_server.domain.nft.service.NFTServiceImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.web.util.UriComponentsBuilder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.abi.FunctionEncoder;
import org.web3j.utils.Numeric;

import com.semonemo.spring_server.domain.blockchain.dto.NFTInfo;
import com.semonemo.spring_server.global.exception.CustomException;
import com.semonemo.spring_server.global.exception.ErrorCode;

@Service
public class BlockChainServiceImpl implements BlockChainService {
    private static final Log log = LogFactory.getLog(NFTServiceImpl.class);

    private static final String BASE_IPFS_URL = "http://j11d109.p.ssafy.io:8088/ipfs/";

    private final Web3j web3j;
    private final RestTemplate restTemplate;
    private final String coinContractAddress;
    private final String nftContractAddress;
    private final String systemContractAddress;
    private final String adminAddress;
    private final String adminPrivateKey;

    @Autowired
    public BlockChainServiceImpl(Web3j web3j, RestTemplate restTemplate) {
        this.web3j = web3j;
        this.restTemplate = restTemplate;
        this.coinContractAddress = "0x7dC302d7D99273Cab00C3046599108F605CCB55c";
        this.nftContractAddress = "0x7dC302d7D99273Cab00C3046599108F605CCB55c";
        this.systemContractAddress = "0x503932fFA68504646FebC302aedFEBd7f64CcAd8";
        this.adminAddress = "0xF17ce10D8c13f97Fd6Db4fCB05F7877512098337";
        this.adminPrivateKey = "0x746b86dcdb199524b77523d43bfb56f0e1b73cae738e66a6bfce4748072ee95c";
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
    public List<NFTInfoDto> getNFTsByIds(List<BigInteger> tokenIds) throws Exception {
        List<Uint256> uint256TokenIds = tokenIds.stream()
            .map(Uint256::new)
            .collect(Collectors.toList());

        Function function = new Function(
            "getNFTsByIds",
            Collections.singletonList(new DynamicArray<>(Uint256.class, uint256TokenIds)),
            Collections.singletonList(new TypeReference<DynamicArray<NFTInfo>>() {})
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
        log.info("Raw ethCall value: " + rawValue);

        if (ethCall.hasError()) {
            throw new Exception("Error calling contract: " + ethCall.getError().getMessage());
        }

        List<NFTInfoDto> result = new ArrayList<>();

        try {
            List<Type> decoded = FunctionReturnDecoder.decode(rawValue, function.getOutputParameters());
            log.info("Decoded value: " + decoded);

            if (decoded.isEmpty() || !(decoded.get(0) instanceof DynamicArray)) {
                throw new Exception("Unexpected response format");
            }

            DynamicArray<NFTInfo> nftArray = (DynamicArray<NFTInfo>) decoded.get(0);


            for (NFTInfo nft : nftArray.getValue()) {
                log.info(1);
                JsonNode tokenData = fetchTokenURIData(new String(nft.tokenURI.getValue(), StandardCharsets.UTF_8));

                log.info(tokenData);
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
            log.error("Error decoding contract response: " + e.getMessage(), e);
            log.error("Raw response: " + rawValue);
            throw new Exception("Failed to decode contract response", e);
        }

        return result;
    }


    // 1초마다 트랜잭션 확인, 40초까지. 결과 확인을 위한 함수
    @Override
    public TransactionReceipt waitForTransactionReceipt(String transactionHash) throws Exception {

        log.info(transactionHash);
        Optional<TransactionReceipt> receiptOptional;
        int attempts = 0;
        do {
            EthGetTransactionReceipt transactionReceipt = web3j.ethGetTransactionReceipt(transactionHash).send();
            log.info(transactionReceipt);
            receiptOptional = transactionReceipt.getTransactionReceipt();

            if (receiptOptional.isPresent()) {
                return receiptOptional.get();
            }

            Thread.sleep(1000);
            attempts++;
        } while (attempts < 30);

        log.info("end");
        throw new CustomException(ErrorCode.BLOCKCHAIN_ERROR);
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