package com.sidematch.backend.domain.team.service;

import com.sidematch.backend.domain.team.PositionType;
import com.sidematch.backend.domain.team.Team;
import com.sidematch.backend.domain.team.TeamType;
import com.sidematch.backend.domain.team.controller.TeamCreateRequest;
import com.sidematch.backend.domain.team.controller.TeamPositionDto;
import com.sidematch.backend.domain.user.User;
import com.sidematch.backend.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

@Transactional
@SpringBootTest
class TeamServiceTest {

    @Autowired
    TeamService teamService;

    @Autowired
    UserRepository userRepository;

    @DisplayName("제목과 설명, 스택을 가진 구성원으로 팀을 생성한다.")
    @Test
    void createTeam() throws Exception {
        //given
        User leader = User.builder()
                .email("test@test.com")
                .build();
        userRepository.save(leader);

        String role = "BE";
        int maxCount = 5;
        List<String> stacks = List.of("spring", "nodejs");
        List<TeamPositionDto> teamPositions = List.of(
                TeamPositionDto.builder()
                    .role(role)
                    .maxCount(maxCount)
                    .stacks(stacks)
                    .build()
        );

        String type = "PROJECT";
        String title = "test title";
        String desc = "test description";
        TeamCreateRequest request = TeamCreateRequest.builder()
                .type(type)
                .title(title)
                .description(desc)
                .teamPositions(teamPositions)
                .build();

        //when
        Team team = teamService.create(leader, request);

        //then
        assertThat(team.getId()).isNotNull();
        assertThat(team).extracting("type", "title", "description")
                .containsExactly(TeamType.valueOf(type), title, desc);
        assertThat(team.getTeamPositions()).hasSize(1)
                .extracting("role", "maxCount")
                .contains(tuple(PositionType.valueOf(role), maxCount));
        assertThat(team.getTeamPositions().get(0).getStacks()).hasSize(2)
                .extracting("stack")
                .containsExactlyInAnyOrderElementsOf(stacks);
    }
}