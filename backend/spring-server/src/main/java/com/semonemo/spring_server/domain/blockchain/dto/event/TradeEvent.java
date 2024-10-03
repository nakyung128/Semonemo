package com.semonemo.spring_server.domain.blockchain.dto.event;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.Utf8String;

import java.util.Arrays;
import java.util.List;

public class TradeEvent {
    public static final Event TRADE_RECORDED_EVENT = new Event("TradeRecorded",
        Arrays.<TypeReference<?>>asList(
            new TypeReference<Uint256>(true) {},  // indexed tradeId
            new TypeReference<Address>() {},      // non-indexed from
            new TypeReference<Address>() {},      // non-indexed to
            new TypeReference<Uint256>() {},      // non-indexed amount
            new TypeReference<Uint256>() {}       // non-indexed timestamp
        )
    );

    public static final Event DEPOSIT_EVENT = new Event("Deposit",
        Arrays.<TypeReference<?>>asList(
            new TypeReference<Address>(true) {},  // indexed user
            new TypeReference<Uint256>() {}       // non-indexed amount
        )
    );

    public static final Event WITHDRAWAL_EVENT = new Event("Withdrawal",
        Arrays.<TypeReference<?>>asList(
            new TypeReference<Address>(true) {},  // indexed user
            new TypeReference<Uint256>() {}       // non-indexed amount
        )
    );

    public static final Event BALANCE_ADJUSTED_EVENT = new Event("BalanceAdjusted",
        Arrays.<TypeReference<?>>asList(
            new TypeReference<Address>(true) {},  // indexed from
            new TypeReference<Address>(true) {},  // indexed to
            new TypeReference<Uint256>() {}       // non-indexed amount
        )
    );

    public static final Event BALANCE_TRANSFERRED_EVENT = new Event("BalanceTransferred",
        Arrays.<TypeReference<?>>asList(
            new TypeReference<Address>(true) {},  // indexed from
            new TypeReference<Address>(true) {},  // indexed to
            new TypeReference<Uint256>() {}       // non-indexed amount
        )
    );
}
