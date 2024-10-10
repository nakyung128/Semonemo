package com.semonemo.spring_server.domain.user.service;

import com.semonemo.spring_server.domain.user.dto.request.UserRegisterRequestDTO;
import com.semonemo.spring_server.domain.user.dto.response.UserLoginResponseDTO;

public interface AuthService {

	void registerUser(UserRegisterRequestDTO requestDTO);

	boolean existsByAddress(String address);

	boolean existsByNickname(String nickname);

	void login(String address, String password);

	UserLoginResponseDTO generateUserToken(String address);

	UserLoginResponseDTO regenerateToken(String refreshToken);
}
