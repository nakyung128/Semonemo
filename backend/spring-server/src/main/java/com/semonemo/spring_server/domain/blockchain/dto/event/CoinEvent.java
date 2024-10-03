package com.semonemo.spring_server.domain.blockchain.dto.event;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.Utf8String;

import java.util.Arrays;
import java.util.List;

public class CoinEvent {
    public static final Event TOKENS_MINTED_EVENT = new Event("TokensMinted",
        Arrays.<TypeReference<?>>asList(
            new TypeReference<Address>(true) {},  // indexed to
            new TypeReference<Uint256>() {},      // non-indexed amount
            new TypeReference<Uint256>() {}       // non-indexed newBalance
        )
    );
}
