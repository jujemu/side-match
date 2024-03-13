package com.sidematch.backend.domain.feed.controller.dto;

import com.sidematch.backend.domain.feed.ProjectDomain;
import com.sidematch.backend.domain.feed.controller.FeedSearchType;
import com.sidematch.backend.domain.feed.repository.FeedSearchCondition;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class FeedSearchRequest {

    private String searchType; // 작성자 혹은 피드 제목
    @Size(max = 50, message = "검색하려는 단어는 50글자를 넘길 수 없습니다.")
    private String searchValue;
    private String domain;

    public ProjectDomain getDomain() {
        return ProjectDomain.of(domain);
    }

    public FeedSearchType getSearchType() {
        return FeedSearchType.of(searchType);
    }

    public FeedSearchCondition toConditionEntity() {
        return new FeedSearchCondition(
                getDomain(),
                getSearchType(),
                searchValue
        );
    }
}
