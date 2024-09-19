package com.semonemo.spring_server.config.security;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			// TODO : CORS 설정 필요
			.cors(cors -> cors.configurationSource(request -> {
				CorsConfiguration config = new CorsConfiguration();
				config.setAllowedOrigins(Arrays.asList(SecurityConstants.CORS_ALLOWED_ORIGINS));
				config.setAllowedMethods(Arrays.asList(SecurityConstants.CORS_ALLOWED_METHODS));
				config.setAllowedHeaders(Arrays.asList(SecurityConstants.CORS_ALLOWED_HEADERS));
				config.setExposedHeaders(Arrays.asList(SecurityConstants.CORS_EXPOSE_HEADER));
				config.setAllowCredentials(true);
				config.setMaxAge(3600L);
				return config;
			}))
			.csrf(AbstractHttpConfigurer::disable)
			// TODO : JWTFilter 적용
			.authorizeHttpRequests(request -> request
				.requestMatchers(SecurityConstants.PUBLIC_ENDPOINTS).permitAll()
				.anyRequest().authenticated())
			// TODO : Spring Security 에외 처리 적용
			.exceptionHandling(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable);

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
