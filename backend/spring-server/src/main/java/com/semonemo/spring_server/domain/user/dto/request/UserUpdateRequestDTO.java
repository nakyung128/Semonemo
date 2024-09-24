package com.semonemo.spring_server.domain.user.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class UserUpdateRequestDTO {
	private String nickname;
	@Setter
	private String profileImage;
}
