package com.semonemo.spring_server.config.security;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.semonemo.spring_server.domain.user.entity.Users;
import com.semonemo.spring_server.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String walletAddress) throws UsernameNotFoundException {
		Users user = userRepository.findByAddress(walletAddress)
			.orElseThrow(() -> new UsernameNotFoundException(walletAddress));

		return new User(user.getAddress(), user.getPassword(),
			Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
	}
}
