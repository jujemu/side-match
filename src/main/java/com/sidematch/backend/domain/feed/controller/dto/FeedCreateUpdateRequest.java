package com.sidematch.backend.domain.feed.controller.dto;

import com.sidematch.backend.domain.feed.ProjectDomain;
import com.sidematch.backend.domain.team.TeamType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FeedCreateUpdateRequest {

    @NotBlank(message = "제목은 필수 입력 값입니다.")
    private String title;
    @NotBlank(message = "내용은 필수 입력 값입니다.")
    private String content;
    @NotNull(message = "피드의 타입은 필수 입력 값입니다.")
    private String type;
    private String domain;

    public TeamType getType() {
        return TeamType.valueOf(type);
    }

    public ProjectDomain getDomain() {
        return (this.domain != null) ? ProjectDomain.valueOf(this.domain) : ProjectDomain.전체;
    }
}
