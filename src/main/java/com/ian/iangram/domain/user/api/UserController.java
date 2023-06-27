package com.ian.iangram.domain.user.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ian.iangram.domain.user.application.UserService;
import com.ian.iangram.domain.user.dto.JoinRequestDto;
import com.ian.iangram.domain.user.dto.JoinResponseDto;
import com.ian.iangram.domain.user.dto.UpdateProfileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    private final ObjectMapper objectMapper;

    @PostMapping("/api/user/verify")
    public ResponseEntity<String> requestAuthCode(@RequestBody JoinRequestDto request) throws JsonProcessingException {
        Long userId = userService.sendAuthCode(request);

        String responseBody = objectMapper.writeValueAsString(
                JoinResponseDto.builder()
                        .userId(userId)
                        .build()
        );

        return ResponseEntity.ok(responseBody);
    }

    @PutMapping("/api/user/verify/{id}")
    public ResponseEntity<HttpStatus> requestCheckAuth(@PathVariable Long id, @RequestBody JoinRequestDto request) {
        if (userService.checkAuthCode(id, request)) {
            userService.enableUpdate(id);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/api/user/{id}")
    public ResponseEntity<String> joinComplete(@PathVariable Long id, @RequestBody UpdateProfileDto request) throws JsonProcessingException, IllegalAccessException {
        Long userId = userService.updateUserProfile(id, request);

        String responseBody = objectMapper.writeValueAsString(
                JoinResponseDto.builder()
                        .userId(userId)
                        .build());

        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

    @GetMapping("/api/user/{id}")
    public ResponseEntity<String> findUser(@PathVariable Long id) throws JsonProcessingException {
        String responseBody = objectMapper.writeValueAsString(
                userService.findUser(id));

        return ResponseEntity.ok(responseBody);
    }

    @PutMapping("/api/user/{id}")
    public ResponseEntity<HttpStatus> userProfileUpdate(@PathVariable Long id, @RequestBody UpdateProfileDto request) throws IllegalAccessException {
        userService.updateUserProfile(id, request);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/user/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);

        return ResponseEntity.ok().build();
    }
}
