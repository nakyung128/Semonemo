package com.semonemo.spring_server.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;

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

	@Bean
	public ElasticsearchClient elasticsearchClient() {
		RestClient restClient = RestClient.builder(
			new HttpHost("elasticsearch", 9200)
		).build();

		ElasticsearchTransport transport = new RestClientTransport(
			restClient, new JacksonJsonpMapper());

		return new ElasticsearchClient(transport);
	}
}

