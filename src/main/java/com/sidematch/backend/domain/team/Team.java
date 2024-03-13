package com.sidematch.backend.domain.team;

import com.sidematch.backend.domain.team.controller.TeamCreateOrUpdateRequest;
import com.sidematch.backend.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private TeamType type;
    private String title;
    private String description;
    private String city;
    private String detailSpot;
    private String meetingTime;
    private boolean isDeleted;
    private boolean isFinished;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leader_id")
    private User leader;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    private List<TeamPosition> teamPositions = new ArrayList<>();

    @Builder
    private Team(String title, String description, TeamType type, User leader, List<TeamPosition> teamPositions) {
        if (title == null || description == null || type == null || leader == null) {
            throw new IllegalArgumentException("팀 정보를 입력해주세요.");
        }

        this.title = title;
        this.description = description;
        this.type = type;
        this.leader = leader;
        this.teamPositions = teamPositions;
    }

    public void addTeamPositions(List<TeamPosition> teamPositions) {
        this.teamPositions = teamPositions;
    }

    public void update(TeamCreateOrUpdateRequest request) {
        this.type = request.getType() != null ? TeamType.valueOf(request.getType()) : this.type;
        this.title = request.getTitle() != null ? request.getTitle() : this.title;
        this.description = request.getDescription() != null ? request.getDescription() : this.description;
        this.meetingTime = request.getMeetingTime()!= null ? request.getDescription() : this.description;
        this.teamPositions = request.getTeamPositions(this);
        this.isDeleted = false;
        this.isFinished = false;
    }

    public List<TeamStack> getTeamStacks() {
        return teamPositions.stream()
                .flatMap(teamPosition ->
                        teamPosition.getStacks().stream())
                .distinct().toList();
    }
}
