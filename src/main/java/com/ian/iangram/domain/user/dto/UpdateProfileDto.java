package com.ian.iangram.domain.user.dto;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UpdateProfileDto {

    private String profileImg;
    private String nickname;

    @Builder
    public UpdateProfileDto(String profileImg, String nickname) {
        this.profileImg = profileImg;
        this.nickname = nickname;
    }
}
