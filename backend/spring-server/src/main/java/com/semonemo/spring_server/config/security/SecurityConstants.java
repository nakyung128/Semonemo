package com.semonemo.spring_server.config.security;

public final class SecurityConstants {

	public static final String[] CORS_ALLOWED_ORIGINS = {"*"};
	public static final String[] CORS_ALLOWED_METHODS = {"GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS"};
	public static final String[] CORS_ALLOWED_HEADERS = {"*"};
	public static final String[] CORS_EXPOSE_HEADER = {"Authorization"};
	public static final String[] PUBLIC_ENDPOINTS = {
		"/api/api-docs/**",
		"/api/swagger-ui/**",
		"/api/auth/**",
		"/api/asset/**",
		"/api/search/**"

	};

	private SecurityConstants() {}
}
