package com.sidematch.backend.domain.feed.repository;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class FeedRepositoryResponse {

    private Long id;
    private String title;
    private String content;
    private Long userId;
    private String userName;

    public FeedRepositoryResponse(Long id, String title, String content, Long userId, String userName) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.userId = userId;
        this.userName = userName;
    }
}
