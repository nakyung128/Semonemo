package com.semonemo.spring_server.domain.blockchain.event;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.Utf8String;

import java.util.Arrays;

public class NFTEvent {
    public static final Event NFT_MINTED_EVENT = new Event("NFTMinted",
        Arrays.<TypeReference<?>>asList(
            new TypeReference<Uint256>(true) {},  // indexed tokenId
            new TypeReference<Address>(true) {},  // indexed creator
            new TypeReference<Utf8String>() {}    // non-indexed tokenURI
        )
    );
}
