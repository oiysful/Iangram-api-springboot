package com.ian.iangram.domain.user.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class JoinResponseDto {

    private Long userId;

    @Builder
    public JoinResponseDto(Long userId) {
        this.userId = userId;
    }
}
