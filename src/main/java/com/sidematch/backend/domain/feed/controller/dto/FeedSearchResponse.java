package com.sidematch.backend.domain.feed.controller.dto;

import com.sidematch.backend.domain.feed.Feed;
import com.sidematch.backend.domain.feed.repository.FeedRepositoryResponse;
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

    @Builder
    private FeedSearchResponse(Long id, Long userId, String title, String content, String userName) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.userName = userName;
    }

    public static FeedSearchResponse of(FeedRepositoryResponse repositoryResponses) {
        return FeedSearchResponse.builder()
                .id(repositoryResponses.getId())
                .userId(repositoryResponses.getId())
                .title(repositoryResponses.getTitle())
                .content(repositoryResponses.getContent())
                .build();
    }
}

