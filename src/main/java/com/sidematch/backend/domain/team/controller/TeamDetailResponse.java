package com.sidematch.backend.domain.team.controller;

import com.sidematch.backend.domain.team.TeamType;
import com.sidematch.backend.domain.user.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class TeamDetailResponse {

    private final Long id;
    private final TeamType type;
    private final String title;
    private final String description;
    private final User leader;
    private final boolean isFinished;

    @Builder
    private TeamDetailResponse(Long id, TeamType type, String title, String description, User leader, boolean isFinished) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.description = description;
        this.leader = leader;
        this.isFinished = isFinished;
    }
}
