package com.semonemo.spring_server.domain.blockchain.dto.event;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.Utf8String;

import java.util.Arrays;
import java.util.List;

public class MarketEvent {
    public static final Event MARKET_CREATED_EVENT = new Event("MarketCreated",
        Arrays.<TypeReference<?>>asList(
            new TypeReference<Uint256>(true) {},  // indexed nftId
            new TypeReference<Address>(true) {},  // indexed seller
            new TypeReference<Uint256>() {}       // non-indexed price
        )
    );

    public static final Event MARKET_CANCELLED_EVENT = new Event("MarketCancelled",
        Arrays.<TypeReference<?>>asList(
            new TypeReference<Uint256>(true) {},  // indexed nftId
            new TypeReference<Address>(true) {}   // indexed seller
        )
    );

    public static final Event MARKET_SOLD_EVENT = new Event("MarketSold",
        Arrays.<TypeReference<?>>asList(
            new TypeReference<Uint256>(true) {},  // indexed nftId
            new TypeReference<Address>(true) {},  // indexed seller
            new TypeReference<Address>(true) {},  // indexed buyer
            new TypeReference<Uint256>() {}       // non-indexed price
        )
    );
}
