package com.semonemo.spring_server.global.common;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.semonemo.spring_server.config.security.CustomUserDetailsService;
import com.semonemo.spring_server.global.exception.CustomException;
import com.semonemo.spring_server.global.exception.ErrorCode;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtProvider {

	private final CustomUserDetailsService userDetailsService;

	@Value("${jwt.secret}")
	private String jwtSecret;

	@Value("${jwt.access-token-expiration}")
	private long accessTokenExpiration;

	@Value("${jwt.refresh-token-expiration}")
	private long refreshTokenExpiration;

	private SecretKey secretKey;

	@PostConstruct
	protected void init() {
		byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
		this.secretKey = Keys.hmacShaKeyFor(keyBytes);
	}

	public String generateAccessToken(String walletAddress) {
		return generateToken(walletAddress, accessTokenExpiration);
	}

	public String generateRefreshToken(String walletAddress) {
		return generateToken(walletAddress, refreshTokenExpiration);
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parser()
				.verifyWith(secretKey)
				.build()
				.parseSignedClaims(token);
			return true;
		} catch (ExpiredJwtException e) {
			throw new CustomException(ErrorCode.EXPIRED_TOKEN_ERROR);
		} catch (JwtException | IllegalArgumentException e) {
			throw new CustomException(ErrorCode.INVALID_TOKEN_ERROR);
		}
	}

	public Authentication getAuthentication(String token) {
		String address = getClaims(token).getSubject();
		UserDetails userDetails = userDetailsService.loadUserByUsername(address);
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	private Claims getClaims(String token) {
		return Jwts.parser()
			.verifyWith(secretKey)
			.build()
			.parseSignedClaims(token)
			.getPayload();
	}

	private String generateToken(String walletAddress, long expiration) {
		return Jwts.builder()
			.issuer("Semonemo")
			.subject(walletAddress)
			.issuedAt(new Date())
			.expiration(new Date(new Date().getTime() + expiration))
			.signWith(secretKey)
			.compact();
	}
}
