package com.semonemo.spring_server.domain.user.service;

import com.semonemo.spring_server.domain.user.dto.UserRegisterDTO;

public interface AuthService {

	public String registerUser(UserRegisterDTO requestDTO);

	public boolean existsByAddress(String address);
}
