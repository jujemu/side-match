package com.sidematch.backend.domain.feed.controller;

import com.sidematch.backend.domain.feed.ProjectDomain;

public enum FeedSearchType {
    WRITER, TITLE;

    public static FeedSearchType of(String searchType) {
        return FeedSearchType.valueOf(searchType);
    }
}
