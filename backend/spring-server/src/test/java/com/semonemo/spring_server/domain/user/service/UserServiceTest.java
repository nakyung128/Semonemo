package com.semonemo.spring_server.domain.user.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.semonemo.spring_server.domain.user.entity.Users;
import com.semonemo.spring_server.domain.user.repository.UserRepository;
import com.semonemo.spring_server.global.exception.CustomException;
import com.semonemo.spring_server.global.exception.ErrorCode;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserServiceImpl userService;

	private Users testUser;

	@BeforeEach
	void setUp() {
		testUser = Users.builder()
			.id(1L)
			.address("testAddress")
			.nickname("testNickname")
			.profileImage("testProfile")
			.build();
	}

	@Test
	@DisplayName("address 필드를 이용하여 사용자 검색 테스트 - 성공 테스트")
	void test_findByAddress_Success() {
		// Given
		String address = "testAddress";
		when(userRepository.findByAddress(address)).thenReturn(Optional.of(testUser));

		// When
		Users result = userService.findByAddress(address);

		// Then
		assertNotNull(result);
		assertEquals(address, result.getAddress());
		verify(userRepository).findByAddress(address);
	}

	@Test
	@DisplayName("address 필드를 이용하여 사용자 검색 테스트 - 실패 테스트")
	void test_findByAddress_Failure() {
		// Given
		String address = "nonexistentAddress";
		when(userRepository.findByAddress(address)).thenReturn(Optional.empty());

		// When & Then
		assertEquals(ErrorCode.USER_NOT_FOUND_ERROR,
			assertThrows(CustomException.class, () -> userService.findByAddress(address)).getErrorCode());
		verify(userRepository).findByAddress(address);
	}

	@Test
	@DisplayName("id 필드를 이용하여 사용자 검색 테스트 - 성공 테스트")
	void test_findById_Success() {
		// Given
		long userId = 1L;
		when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));

		// When
		Users result = userService.findById(userId);

		// Then
		assertNotNull(result);
		assertEquals(userId, result.getId());
		verify(userRepository).findById(userId);
	}

	@Test
	@DisplayName("id 필드를 이용하여 사용자 검색 테스트 - 실패 테스트")
	void test_findById_Failure() {
		// Given
		long userId = 999L;
		when(userRepository.findById(userId)).thenReturn(Optional.empty());

		// When & Then
		assertEquals(ErrorCode.USER_NOT_FOUND_ERROR,
			assertThrows(CustomException.class, () -> userService.findById(userId)).getErrorCode());
		verify(userRepository).findById(userId);
	}

}