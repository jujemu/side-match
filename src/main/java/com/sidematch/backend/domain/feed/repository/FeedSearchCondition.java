package com.sidematch.backend.domain.feed.repository;

import com.sidematch.backend.domain.feed.ProjectDomain;
import com.sidematch.backend.domain.feed.controller.FeedSearchType;
import lombok.Getter;

@Getter
public class FeedSearchCondition {

    private ProjectDomain domain;
    private FeedSearchType type;
    private String keyword;

    public FeedSearchCondition(ProjectDomain domain, FeedSearchType type, String keyword) {
        this.domain = domain;
        this.type = type;
        this.keyword = keyword;
    }
}
