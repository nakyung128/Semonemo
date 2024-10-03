package com.semonemo.spring_server.config.redis;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import com.semonemo.spring_server.domain.auction.service.AuctionService;

@Component
public class KeyExpirationEventListener extends KeyExpirationEventMessageListener {

	private final AuctionService auctionService;

	public KeyExpirationEventListener(RedisMessageListenerContainer listenerContainer, AuctionService auctionService) {
		super(listenerContainer);
		this.auctionService = auctionService;
	}

	@Override
	public void onMessage(Message message, byte[] pattern) {
		String expiredKey = message.toString();
		if (expiredKey.startsWith("auction:")) {
			long auctionId = Long.parseLong(expiredKey.split(":")[1]);
			sendAuctionEndMessage(auctionId);
		}
	}

	private void sendAuctionEndMessage(long auctionId) {
		auctionService.endAuction(auctionId);
	}
}
