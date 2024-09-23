package com.semonemo.spring_server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;

public class ElasticSearchConfig {
	@Configuration
	public class ElasticsearchConfig extends ElasticsearchConfiguration {
		@Override
		public ClientConfiguration clientConfiguration() {
			return ClientConfiguration.builder()
				.connectedTo("localhost:9200")
				.usingSsl()
				.withBasicAuth("elastic", "wns0201")
				.build();
		}
	}
}
