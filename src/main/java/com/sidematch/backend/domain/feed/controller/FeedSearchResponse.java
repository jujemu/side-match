package com.sidematch.backend.domain.feed.controller;

import com.sidematch.backend.domain.feed.Feed;
import com.sidematch.backend.domain.user.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
public class FeedSearchResponse {

    private Long id;
    private Long userId;
    private String title;
    private String content;
    private String userName;
    private Long positionLevel;
    private boolean isLiked;

    @Builder
    public FeedSearchResponse(Feed feed, User user) {
        this.id = feed.getId();
        this.title = feed.getTitle();
        this.content = feed.getContent();
        this.userName = user.getName();
        this.userId = user.getId();
    }
}

