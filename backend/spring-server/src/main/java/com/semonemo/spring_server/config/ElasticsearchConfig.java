package com.semonemo.spring_server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;


@Configuration
public class ElasticsearchConfig extends ElasticsearchConfiguration {
	@Value("${elasticsearch.server}")
	private String server;
	@Override
	public ClientConfiguration clientConfiguration() {
		return ClientConfiguration.builder()
			.connectedTo(server)
			// .usingSsl()  // 제거 또는 주석 처리
			// .withBasicAuth("elastic", "wns0201")  // 제거 또는 주석 처리
			.build();
	}
}

