package com.sidematch.backend.domain.team.service;

import com.sidematch.backend.domain.team.Team;
import com.sidematch.backend.domain.team.TeamType;
import com.sidematch.backend.domain.team.controller.TeamCreateOrUpdateRequest;
import com.sidematch.backend.domain.team.controller.TeamDetailResponse;
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

    public Team create(User leader, TeamCreateOrUpdateRequest request) {
        Team team = createTeam(leader, request);

        List<TeamPosition> teamPositions = request.getTeamPositions(team);
        team.addTeamPositions(teamPositions);

//        teamPositionRepository.saveAll(teamPositions);
        teamRepository.save(team);

        return team;
    }

    @Transactional(readOnly = true)
    public TeamDetailResponse showDetail(Long teamId) {
        Team team = getTeam(teamId);
        return getResponse(team);
    }

    public Team update(User leader, Long teamId, TeamCreateOrUpdateRequest request) {
        Team team = getTeam(teamId);
        isLeaderOfTeam(leader, team);
        team.update(request);
        return team;
    }

    private Team getTeam(Long teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("팀을 찾을 수 없습니다."));
    }

    private Team createTeam(User leader, TeamCreateOrUpdateRequest request) {
        return Team.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .type(TeamType.valueOf(request.getType()))
                .leader(leader)
                .build();
    }

    private TeamDetailResponse getResponse(Team team) {
        return TeamDetailResponse.builder()
                .id(team.getId())
                .type(team.getType())
                .title(team.getTitle())
                .description(team.getDescription())
                .leader(team.getLeader())
                .isFinished(team.isFinished())
                .build();
    }

    private void isLeaderOfTeam(User leader, Team team) {
        if (!leader.equals(team.getLeader())) {
            throw new IllegalArgumentException("팀장만이 수정할 수 있습니다.");
        }
    }
}
