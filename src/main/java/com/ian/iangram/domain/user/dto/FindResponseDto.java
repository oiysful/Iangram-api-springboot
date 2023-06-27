package com.ian.iangram.domain.user.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class FindResponseDto {

    private String email;
    private String profileImg;
    private String nickname;

    @Builder
    public FindResponseDto(String email, String profileImg, String nickname) {
        this.email = email;
        this.profileImg = profileImg;
        this.nickname = nickname;
    }
}
