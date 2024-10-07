package com.semonemo.spring_server.domain.user.dto.request;

import com.semonemo.spring_server.domain.user.entity.Users;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class UserRegisterRequestDTO {
    private String address;
    private String password;
    private String nickname;
    @Setter
    private String profileImage;

    public Users toEntity() {
        return Users.builder()
                .address(this.address)
                .password(this.password)
                .nickname(this.nickname)
                .profileImage(this.profileImage)
                .balance(0L)
                .build();
    }

    @Schema(hidden = true)
    public boolean isValid() {
        return address != null && password != null && nickname != null &&
                !address.isBlank() && !password.isBlank() && !nickname.isBlank();
    }
}
