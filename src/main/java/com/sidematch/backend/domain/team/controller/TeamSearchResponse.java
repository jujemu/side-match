package com.sidematch.backend.domain.team.controller;

import com.sidematch.backend.domain.team.TeamStack;
import com.sidematch.backend.domain.user.User;
import com.sidematch.backend.domain.team.Team;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class TeamSearchResponse {

    private Long id;
    private String title;
    private String description;
    private List<TeamStackDto> teamStacks;
    private Long leaderID;
    private String leaderNickname;
    private boolean isFinished;

    @Builder
    public TeamSearchResponse(Long id, String title, String description, List<TeamStackDto> teamStacks, Long leaderID, String leaderNickname, boolean isFinished) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.teamStacks = teamStacks;
        this.leaderID = leaderID;
        this.leaderNickname = leaderNickname;
        this.isFinished = isFinished;
    }

    public static TeamSearchResponse from(Team team) {
        List<TeamStack> teamStacks = team.getTeamStacks();
        List<TeamStackDto> teamStackDtos = teamStacks.stream()
                .map(TeamStackDto::of).toList();

        return TeamSearchResponse.builder()
                .id(team.getId())
                .title(team.getTitle())
                .description(team.getDescription())
                .teamStacks(teamStackDtos)
                .leaderID(team.getLeader().getId())
                .leaderNickname(team.getLeader().getNickname())
                .isFinished(team.isFinished())
                .build();
    }
}
