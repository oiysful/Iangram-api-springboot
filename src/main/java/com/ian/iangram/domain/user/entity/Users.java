package com.ian.iangram.domain.user.entity;

import com.ian.iangram.domain.user.dto.UpdateProfileDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, length = 50)
    private String password;

    @Lob
    private String profileImg;

    @Column(length = 30)
    private String nickname;

    @ColumnDefault("0")
    private int enabled;

    @Column(length = 16)
    private String authCode;

    @Builder
    public Users(String email, String password, String authCode) {
        this.email = email;
        this.password = password;
        this.authCode = authCode;
    }

    public void enableUpdate() {
        this.enabled = 1;
    }

    public Users update(UpdateProfileDto request) {
        this.profileImg = request.getProfileImg();
        this.nickname = request.getNickname();

        return this;
    }
}
