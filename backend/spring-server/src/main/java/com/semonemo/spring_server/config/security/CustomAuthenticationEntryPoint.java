package com.semonemo.spring_server.config.security;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.semonemo.spring_server.global.exception.ErrorCode;
import com.semonemo.spring_server.global.exception.ErrorResponse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private static final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authException) throws IOException, ServletException {

		ErrorCode errorCode = getErrorCode(authException, response);
		ErrorResponse<?> errorResponse = ErrorResponse.error(errorCode);

		response.setStatus(errorCode.getHttpStatus().value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("UTF-8");

		response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
	}

	private ErrorCode getErrorCode(AuthenticationException authException, HttpServletResponse response) {
		if (response.getStatus() == HttpServletResponse.SC_NOT_FOUND) {
			return ErrorCode.RESOURCE_NOT_FOUND_ERROR;
		} else {
			return ErrorCode.AUTHENTICATION_FAIL_ERROR;
		}
	}
}
