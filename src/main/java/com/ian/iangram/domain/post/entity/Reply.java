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
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false, name = "postId")
    private Posts post;

    @ManyToOne
    @JoinColumn(nullable = false, name = "authorId")
    private Users author;

    @Column(nullable = false, length = 200)
    private String content;

    @Builder
    public Reply(Posts post, Users author, String content) {
        this.post = post;
        this.author = author;
        this.content = content;
    }
}
