package com.ian.iangram.domain.user.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ian.iangram.domain.user.dto.JoinRequestDto;
import com.ian.iangram.domain.user.dto.UpdateProfileDto;
import com.ian.iangram.domain.user.entity.Users;
import com.ian.iangram.domain.user.repository.UsersRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@SpringBootTest
class UserControllerTest {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    UsersRepository usersRepository;

    @BeforeEach
    public void setMockMvc() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    private static String authCode;
    private static final String email = "test@email.com";
    private static final String password = "password";
    private static final String profileImg = "profile";
    private static final String nickname = "nickname";

    @Order(1)
    @Test
    void requestAuthCode() throws Exception {
        // given
        final String url = "/api/user/verify";

        final JoinRequestDto request = JoinRequestDto.builder()
                .email(email)
                .password(password)
                .build();

        final String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        // then
        result.andExpect(status().isOk());

        List<Users> users = usersRepository.findAll();

        assertThat(users.size()).isEqualTo(1);
        assertThat(users.get(0).getEmail()).isEqualTo(email);
        assertThat(users.get(0).getPassword()).isEqualTo(password);
        assertThat(users.get(0).getAuthCode()).isNotEmpty();

        authCode = users.get(0).getAuthCode();
    }

    @DisplayName("인증번호 검증 실패")
    @Order(2)
    @Test
    void requestCheckAuthFail() throws Exception {
        // given
        final long id = 1L;
        final String url = "/api/user/verify/" + id;
        final JoinRequestDto request = JoinRequestDto.builder()
                .authCode("authCode")
                .build();

        final String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions result = mockMvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        // then
        result.andExpect(status().isUnauthorized());

        Users user = usersRepository.findById(id).orElseThrow();

        assertThat(user.getEnabled()).isEqualTo(0);
    }

    @DisplayName("인증번호 검증 성공")
    @Order(3)
    @Test
    void requestCheckAuthSuccess() throws Exception {
        // given
        final long id = 1L;
        final String url = "/api/user/verify/" + id;
        final JoinRequestDto request = JoinRequestDto.builder()
                .authCode(authCode)
                .build();

        final String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions result = mockMvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        // then
        result.andExpect(status().isOk());

        Users user = usersRepository.findById(id).orElseThrow();

        assertThat(user.getEnabled()).isEqualTo(1);
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getPassword()).isEqualTo(password);
    }

    @Order(4)
    @Test
    void joinComplete() throws Exception {
        // given
        final long id = 1L;
        final String url = "/api/user/" + id;

        final UpdateProfileDto request = UpdateProfileDto.builder()
                .profileImg(profileImg)
                .nickname(nickname)
                .build();

        final String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        // then
        result.andExpect(status().isCreated());

        Users user = usersRepository.findById(id).orElseThrow();

        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getPassword()).isEqualTo(password);
        assertThat(user.getProfileImg()).isEqualTo(profileImg);
        assertThat(user.getNickname()).isEqualTo(nickname);
    }

    @Order(5)
    @Test
    void findUser() throws Exception {
        // given
        final long id = 1L;
        final String url = "/api/user/" + id;

        final UpdateProfileDto request = UpdateProfileDto.builder()
                .profileImg(profileImg)
                .nickname(nickname)
                .build();

        final String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions result = mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        // then
        result.andExpect(status().isOk());
        String resultBody = result.andReturn().getResponse().getContentAsString();
        assertThat(resultBody).contains(email);
        assertThat(resultBody).contains(profileImg);
        assertThat(resultBody).contains(nickname);
    }

    @Order(6)
    @Test
    void updateProfile() throws Exception {
        // given
        final long id = 1L;
        final String url = "/api/user/" + id;

        final UpdateProfileDto request = UpdateProfileDto.builder()
                .profileImg(profileImg + "New")
                .nickname(nickname + "New")
                .build();

        final String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions result = mockMvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        // then
        result.andExpect(status().isOk());

        Users user = usersRepository.findById(id).orElseThrow();

        assertThat(user.getProfileImg()).isEqualTo(profileImg + "New");
        assertThat(user.getNickname()).isEqualTo(nickname + "New");
    }

    @Order(7)
    @Test
    void deleteUser() throws Exception {
        // given
        final long id = 1L;
        final String url = "/api/user/" + id;

        // when
        ResultActions result = mockMvc.perform(delete(url));

        // then
        result.andExpect(status().isOk());

        Users user = usersRepository.findById(id).orElse(null);

        assertThat(user).isNull();
    }
}