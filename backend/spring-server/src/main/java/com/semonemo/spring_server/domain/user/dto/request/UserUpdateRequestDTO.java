package com.semonemo.spring_server.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class UserUpdateRequestDTO {
	private String nickname;
	@Setter
	@Schema(hidden = true)
	private String profileImage;
}
