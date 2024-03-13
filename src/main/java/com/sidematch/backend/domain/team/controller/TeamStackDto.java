package com.sidematch.backend.domain.team.controller;

import com.sidematch.backend.domain.team.TeamStack;
import lombok.Builder;
import lombok.Getter;

@Getter
public class TeamStackDto {

    private Long id;
    private String name;

    @Builder
    private TeamStackDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static TeamStackDto of(TeamStack teamStack) {
        return TeamStackDto.builder()
                .id(teamStack.getId())
                .name(teamStack.getName())
                .build();
    }
}
