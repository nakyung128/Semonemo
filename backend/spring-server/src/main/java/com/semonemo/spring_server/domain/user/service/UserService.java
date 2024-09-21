package com.semonemo.spring_server.domain.user.service;

import com.semonemo.spring_server.domain.user.entity.Users;

public interface UserService {

	Users findByAddress(String address);

	Users findById(long userId);
}
