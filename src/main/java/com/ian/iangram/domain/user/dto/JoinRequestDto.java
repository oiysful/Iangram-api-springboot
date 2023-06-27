package com.ian.iangram.domain.user.dto;

import com.ian.iangram.domain.user.entity.Users;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class JoinRequestDto {

    private String email;
    private String password;
    private String authCode;

    @Builder
    public JoinRequestDto(String email, String password, String authCode) {
        this.email = email;
        this.password = password;
        this.authCode = authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public Users toEntity() {
        return Users.builder()
                .email(email)
                .password(password)
                .authCode(authCode)
                .build();
    }
}
