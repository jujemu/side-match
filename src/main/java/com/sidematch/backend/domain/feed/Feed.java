package com.sidematch.backend.domain.feed;

import com.sidematch.backend.domain.feed.controller.FeedCreateUpdateRequest;
import com.sidematch.backend.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Feed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feed_id")
    private Long id;

    @Column(name = "feed_title")
    private String title;

    @Column(name = "feed_content")
    private String content;
    private Long type; // 0 -> project, 1 ->  study

    @Enumerated(EnumType.STRING)
    private ProjectDomain projectDomain;
    private String thumbnailUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Feed(String title, String content, Long type, ProjectDomain projectDomain, String thumbnailUrl, User user) {
        this.title = title;
        this.content = content;
        this.type = type;
        this.projectDomain = projectDomain;
        this.thumbnailUrl = thumbnailUrl;
        this.user = user;
    }

    //- 비즈니스 로직 -//
    public Feed updateFeed(FeedCreateUpdateRequest request) {
        this.title = (request.getTitle() == null) ? this.title : request.getTitle();
        this.content = (request.getContent() == null) ? this.content : request.getContent();
        this.type = request.getType();
        this.projectDomain = (request.getDomain() == null) ? this.projectDomain : request.getDomain();
        return this;
    }
}


