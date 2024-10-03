package com.semonemo.spring_server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableSimpleBroker("/topic"); // 메시지를 구독하는 요청 설정
		registry.setApplicationDestinationPrefixes("/app"); // 메시지를 발행하는 요청 설정
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/ws-stomp")
			.setAllowedOriginPatterns("*")
			.withSockJS();

		// API 통신 시, `withSockJS()` 설정을 빼야한다.
		registry.addEndpoint("/ws-stomp")
			.setAllowedOriginPatterns("*");
	}
}
