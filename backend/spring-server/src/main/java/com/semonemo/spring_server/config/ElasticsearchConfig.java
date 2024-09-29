package com.semonemo.spring_server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;


@Configuration
public class ElasticsearchConfig extends ElasticsearchConfiguration {
	@Value("${elasticsearch.server}")
	private String server;
	@Value("${elasticsearch.id}")
	private String id;

	@Value("${elasticsearch.password}")
	private String password;
	@Override
	public ClientConfiguration clientConfiguration() {
		return ClientConfiguration.builder()
			.connectedTo(server)
			.withBasicAuth(id, password)
			.build();
	}
}

