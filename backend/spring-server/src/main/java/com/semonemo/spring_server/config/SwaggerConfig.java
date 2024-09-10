package com.semonemo.spring_server.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI openAPI() {
		Info info = new Info()
			.version("v1.0")
			.title("세모내모 Spring Server API")
			.description("세모내모 서비스의 Spring Server API");
		return new OpenAPI()
			.info(info);
	}
}
