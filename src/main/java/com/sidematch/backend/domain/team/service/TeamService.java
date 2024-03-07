package com.sidematch.backend.domain.team.service;

import com.sidematch.backend.domain.team.Team;
import com.sidematch.backend.domain.team.TeamType;
import com.sidematch.backend.domain.team.controller.TeamCreateRequest;
import com.sidematch.backend.domain.team.repository.TeamPositionRepository;
import com.sidematch.backend.domain.team.repository.TeamRepository;
import com.sidematch.backend.domain.team.TeamPosition;
import com.sidematch.backend.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final TeamPositionRepository teamPositionRepository;

    public Team create(User leader, TeamCreateRequest request) {
        Team team = createTeam(leader, request);

        List<TeamPosition> teamPositions = request.getTeamPositions(team);
        team.addTeamPositions(teamPositions);

        teamPositionRepository.saveAll(teamPositions);
        teamRepository.save(team);

        return team;
    }

    private Team createTeam(User leader, TeamCreateRequest request) {
        return Team.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .type(TeamType.valueOf(request.getType()))
                .leader(leader)
                .build();
    }
}
