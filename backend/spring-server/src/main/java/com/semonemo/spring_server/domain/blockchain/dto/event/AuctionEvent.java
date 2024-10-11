package com.semonemo.spring_server.domain.blockchain.dto.event;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.Utf8String;

import java.util.Arrays;
import java.util.List;

public class AuctionEvent {
    public static final Event AUCTION_STARTED_EVENT = new Event("AuctionStarted",
        Arrays.<TypeReference<?>>asList(
            new TypeReference<Uint256>(true) {},  // indexed nftId
            new TypeReference<Address>(true) {}   // indexed seller
        )
    );

    public static final Event AUCTION_CANCELLED_EVENT = new Event("AuctionCancelled",
        Arrays.<TypeReference<?>>asList(
            new TypeReference<Uint256>(true) {},  // indexed nftId
            new TypeReference<Address>(true) {}   // indexed seller
        )
    );

    public static final Event AUCTION_CLOSED_EVENT = new Event("AuctionClosed",
        Arrays.<TypeReference<?>>asList(
            new TypeReference<Uint256>(true) {},  // indexed nftId
            new TypeReference<Address>(true) {},  // indexed seller
            new TypeReference<Address>(true) {},  // indexed buyer
            new TypeReference<Uint256>() {}       // non-indexed price
        )
    );
}
