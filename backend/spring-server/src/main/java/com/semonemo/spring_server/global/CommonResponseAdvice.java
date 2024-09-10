package com.semonemo.spring_server.global;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice(basePackages = "com.semonemo.spring_server")
public class CommonResponseAdvice implements ResponseBodyAdvice<Object> {

	private final HttpStatus successStatus = HttpStatus.OK;
	private final String successCode = "S000";
	// TODO : API 별로 성공 메시지 다르게 받기
	private final String successMsg = "성공";

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return MappingJackson2HttpMessageConverter.class.isAssignableFrom(converterType);
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
		Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
		ServerHttpResponse response) {
		if (body instanceof CommonResponse) {
			return body;
		}

		response.setStatusCode(successStatus);
		return CommonResponse.builder()
			.status(successCode)
			.data(body)
			.message(successMsg)
			.build();
	}
}
