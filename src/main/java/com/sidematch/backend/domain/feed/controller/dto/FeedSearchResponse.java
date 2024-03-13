package com.sidematch.backend.domain.feed.controller.dto;

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
    private boolean isLiked;

    @Builder
    private FeedSearchResponse(Long id, Long userId, String title, String content, String userName, boolean isLiked) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.userName = userName;
        this.isLiked = isLiked;
    }

    public static FeedSearchResponse of(Feed feed) {
        return FeedSearchResponse.builder()
                .id(feed.getId())
                .userId(feed.getUser().getId())
                .title(feed.getTitle())
                .content(feed.getContent())
                .build();
    }
}

