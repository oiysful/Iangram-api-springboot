package com.ian.iangram.domain.user.application;

import com.ian.iangram.domain.user.dto.FindResponseDto;
import com.ian.iangram.domain.user.dto.JoinRequestDto;
import com.ian.iangram.domain.user.dto.UpdateProfileDto;
import com.ian.iangram.domain.user.entity.Users;
import com.ian.iangram.domain.user.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UsersRepository usersRepository;

    public Long sendAuthCode(JoinRequestDto request) {
        String authCode = new AuthCode().getAuthCode();

        request.setAuthCode(authCode);

        return usersRepository.save(request.toEntity()).getId();
    }

    public boolean checkAuthCode(Long userId, JoinRequestDto request) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("회원가입 오류: 확인되지 않는 ID"));

        return user.getAuthCode().equals(request.getAuthCode());
    }

    @Transactional
    public void enableUpdate(Long userId) {
        usersRepository.findById(userId).ifPresent(Users::enableUpdate);
    }

    @Transactional
    public Long updateUserProfile(Long userId, UpdateProfileDto request) throws IllegalAccessException {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("회원가입 오류: 확인되지 않는 ID"));

        if (user.getEnabled() != 1) throw new IllegalAccessException("회원가입 오류: 인증되지 않은 회원");

        return user.update(request).getId();
    }

    public FindResponseDto findUser(Long userId) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("회원가입 오류: 확인되지 않는 ID"));

        return FindResponseDto.builder()
                .email(user.getEmail())
                .profileImg(user.getProfileImg())
                .nickname(user.getNickname())
                .build();
    }

    public void deleteUser(Long userId) {
        usersRepository.deleteById(userId);
    }

}
