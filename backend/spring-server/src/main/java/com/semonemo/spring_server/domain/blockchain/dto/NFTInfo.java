package com.semonemo.spring_server.domain.blockchain.dto;

import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Uint256;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

public class NFTInfo extends DynamicStruct {
    public Uint256 tokenId;
    public Address creator;
    public Address currentOwner;
    public DynamicBytes tokenURI;  // Changed from Utf8String to DynamicBytes

    public NFTInfo(Uint256 tokenId, Address creator, Address currentOwner, DynamicBytes tokenURI) {
        super(tokenId, creator, currentOwner, tokenURI);
        this.tokenId = tokenId;
        this.creator = creator;
        this.currentOwner = currentOwner;
        this.tokenURI = tokenURI;
    }

    public static NFTInfo decode(List<Type> values) {
        return new NFTInfo(
            (Uint256) values.get(0),
            (Address) values.get(1),
            (Address) values.get(2),
            (DynamicBytes) values.get(3)
        );
    }
}