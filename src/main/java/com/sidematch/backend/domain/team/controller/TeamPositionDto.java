package com.sidematch.backend.domain.team.controller;

import com.sidematch.backend.domain.team.PositionType;
import com.sidematch.backend.domain.team.Team;
import com.sidematch.backend.domain.team.TeamPosition;
import com.sidematch.backend.domain.team.TeamStack;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class TeamPositionDto {

    @NotBlank(message = "팀원의 역할은 필수 값입니다")
    private String role;

    @Positive(message = "역할의 최대 모집 인원은 양수만 가능합니다.")
    private Integer maxCount;

    private List<String> stacks;

    @Builder
    private TeamPositionDto(String role, Integer maxCount, List<String> stacks) {
        this.role = role;
        this.maxCount = maxCount;
        this.stacks = stacks;
    }

    public TeamPosition toEntity(Team team) {
        List<TeamStack> teamStacks = stacks.stream()
                .map(stack ->
                        TeamStack.builder()
                                .name(stack)
                                .build()
                ).toList();

        return TeamPosition.builder()
                .maxCount(maxCount)
                .role(PositionType.valueOf(role))
                .team(team)
                .stacks(teamStacks)
                .build();
    }
}
