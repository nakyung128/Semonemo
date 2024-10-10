package com.semonemo.spring_server.domain.blockchain.dto.event;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.Utf8String;

import java.util.Arrays;
import java.util.List;

public class NFTEvent {
    public static final Event NFT_MINTED_EVENT = new Event("NFTMinted",
        Arrays.<TypeReference<?>>asList(
            new TypeReference<Uint256>(true) {},  // indexed tokenId
            new TypeReference<Address>(true) {},  // indexed creator
            new TypeReference<Utf8String>() {}    // non-indexed tokenURI
        )
    );

    public static final Event NFT_TRANSFERRED_EVENT = new Event("NFTTransferred",
        Arrays.<TypeReference<?>>asList(
            new TypeReference<Uint256>(true) {},  // indexed tokenId
            new TypeReference<Address>(true) {},  // indexed from
            new TypeReference<Address>(true) {}   // indexed to
        )
    );

    public static final Event TOKEN_URI_UPDATED_EVENT = new Event("TokenURIUpdated",
        Arrays.<TypeReference<?>>asList(
            new TypeReference<Uint256>(true) {},  // indexed tokenId
            new TypeReference<Utf8String>() {}    // non-indexed newTokenURI
        )
    );

    public static final Event NFT_BURNED_EVENT = new Event("NFTBurned",
        List.<TypeReference<?>>of(
            new TypeReference<Uint256>(true) {
            }   // indexed tokenId
        )
    );
}
