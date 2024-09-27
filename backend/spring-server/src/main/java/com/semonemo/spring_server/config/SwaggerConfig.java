package com.semonemo.spring_server.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.Schema;

import org.springdoc.core.customizers.OperationCustomizer;
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

	@Bean
	public OperationCustomizer operationCustomizer() {
		return (operation, handlerMethod) -> {
			this.addResponseBodyWrapperSchemaExample(operation);
			return operation;
		};
	}

	private void addResponseBodyWrapperSchemaExample(Operation operation) {
		final Content content = operation.getResponses().get("200").getContent();
		if (content != null) {
			content.forEach((mediaTypeKey, mediaType) -> {
				Schema<?> originalSchema = mediaType.getSchema();
				Schema<?> wrappedSchema = wrapSchema(originalSchema);
				mediaType.setSchema(wrappedSchema);
			});
		}
	}

	private Schema<?> wrapSchema(Schema<?> originalSchema) {
		final Schema<?> wrapperSchema = new Schema<>();

		wrapperSchema.addProperty("code", new Schema<>().type("String").example("S000"));
		wrapperSchema.addProperty("data", originalSchema);
		wrapperSchema.addProperty("message", new Schema<>().type("String").example("성공 메시지"));

		return wrapperSchema;
	}
}
