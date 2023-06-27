package com.ian.iangram.domain.post.entity;

import com.ian.iangram.domain.user.entity.Users;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Posts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "authorId", nullable = false)
    private Users author;

    @Column(nullable = false)
    private String media;

    private String location;

    @Column(length = 1000)
    private String content;

    @Builder
    public Posts(Users author, String media, String location, String content) {
        this.author = author;
        this.media = media;
        this.location = location;
        this.content = content;
    }
}
