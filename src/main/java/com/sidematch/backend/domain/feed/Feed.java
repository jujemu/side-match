package com.sidematch.backend.domain.feed;

import com.sidematch.backend.domain.feed.controller.dto.FeedCreateUpdateRequest;
import com.sidematch.backend.domain.team.TeamType;
import com.sidematch.backend.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private TeamType type;

    @Enumerated(EnumType.STRING)
    private ProjectDomain projectDomain;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Feed(String title, String content, TeamType type, ProjectDomain projectDomain, User user) {
        this.title = title;
        this.content = content;
        this.type = type;
        this.projectDomain = projectDomain;
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


