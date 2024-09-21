package com.semonemo.spring_server.domain.user.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.semonemo.spring_server.domain.user.entity.Users;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

	private final UserRepository userRepository;

	private Users user;

	@Autowired
	public UserRepositoryTest(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@BeforeEach
	public void setUp() {
		user = Users.builder()
			.address("testAddress")
			.nickname("testNickname")
			.profileImage("testProfileImage")
			.build();
		userRepository.save(user);
	}

	@Test
	@DisplayName("address 필드를 통해 사용자를 찾는 테스트 - 사용자가 존재하는 경우")
	public void testFindByAddress() {
		// Given - BeforeEach

		// When
		Optional<Users> foundUser = userRepository.findByAddress(user.getAddress());

		// Then
		assertTrue(foundUser.isPresent());
		assertEquals(user.getAddress(), foundUser.get().getAddress());
	}

	@Test
	@DisplayName("address 필드를 통해 사용자를 찾는 테스트 - 사용자가 존재하지 않는 경우")
	public void testFindByAddress_UserNotFound() {
		// Given - BeforeEach

		// When
		Optional<Users> foundUser = userRepository.findByAddress("notFoundAddress");

		// Then
		assertTrue(foundUser.isEmpty());
		assertThrows(NoSuchElementException.class, foundUser::get);
	}

	@Test
	@DisplayName("address 필드를 통해 사용자가 존재하는지 확인하는 테스트")
	public void testExistsByAddress_UserNotFound() {
		// Given - BeforeEach

		// When & Then
		assertTrue(userRepository.existsByAddress("testAddress"));
		assertFalse(userRepository.existsByAddress("nonexistentAddress"));
	}

	@Test
	@DisplayName("nickname 필드를 통해 사용자가 존재하는지 확인하는 테스트")
	public void testExistsByNickname() {
		// Given - BeforeEach

		// When & Then
		assertTrue(userRepository.existsByNickname("testNickname"));
		assertFalse(userRepository.existsByNickname("nonexistentNickname"));
	}
}