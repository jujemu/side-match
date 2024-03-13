package com.sidematch.backend.domain.team.controller;

import com.sidematch.backend.domain.team.Team;
import com.sidematch.backend.domain.team.TeamPosition;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
public class TeamCreateOrUpdateRequest {

    @NotBlank(message = "타입은 필수 입력 값입니다.")
    private String type;

    @NotBlank(message = "제목은 필수 입력 값입니다.")
    private String title;

    @NotBlank(message = "내용은 필수 입력 값입니다.")
    private String description;

    @Valid
    @NotEmpty(message = "팀원은 필수 입력 값입니다.")
    private List<TeamPositionDto> teamPositions;

    private String city;
    private String detailSpot;
    private String meetingTime;

    @Builder
    private TeamCreateOrUpdateRequest(String type, String title, String description, List<TeamPositionDto> teamPositions, String city, String detailSpot, String meetingTime) {
        this.type = type;
        this.title = title;
        this.description = description;
        this.teamPositions = teamPositions;
        this.city = city;
        this.detailSpot = detailSpot;
        this.meetingTime = meetingTime;
    }

    public List<TeamPosition> getTeamPositions(Team team) {
        if (teamPositions == null) {
            return team.getTeamPositions() != null ? team.getTeamPositions() : null;
        }

        return teamPositions.stream()
                .map(dto -> dto.toEntity(team))
                .toList();
    }
}
