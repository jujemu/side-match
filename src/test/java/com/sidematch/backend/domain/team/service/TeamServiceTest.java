package com.sidematch.backend.domain.team.service;

import com.sidematch.backend.domain.team.*;
import com.sidematch.backend.domain.team.controller.TeamRequest;
import com.sidematch.backend.domain.team.controller.TeamPositionDto;
import com.sidematch.backend.domain.team.repository.TeamRepository;
import com.sidematch.backend.domain.user.User;
import com.sidematch.backend.domain.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

    @Autowired
    TeamRepository teamRepository;

    @DisplayName("제목과 설명, 스택을 가진 구성원으로 팀을 생성한다.")
    @Test
    void createTeam() throws Exception {
        // given
        User leader = getUser("test@test.com");
        userRepository.save(leader);

        String role = "BE";
        int maxCount = 5;
        List<String> stacks = List.of("spring", "nodejs");
        List<TeamPositionDto> teamPositions = List.of(
                TeamPositionDto.builder()
                    .role(role)
                    .maxCount(maxCount)
                    .stacks(stacks)
                    .build());

        String type = "PROJECT";
        String title = "test title";
        String desc = "test description";
        TeamRequest request = TeamRequest.builder()
                .type(type)
                .title(title)
                .description(desc)
                .teamPositions(teamPositions)
                .build();

        // when
        Team team = teamService.create(leader, request);

        // then
        assertThat(team.getId()).isNotNull();
        assertThat(team).extracting("type", "title", "description")
                .containsExactly(TeamType.valueOf(type), title, desc);

        // 팀 구성원들
        assertThat(team.getTeamPositions().get(0).getId()).isNotNull();
        assertThat(team.getTeamPositions()).hasSize(1)
                .extracting("role", "maxCount")
                .contains(tuple(PositionType.valueOf(role), maxCount));

        // 팀 구성원의 스택 정보
        assertThat(team.getTeamPositions().get(0).getStacks().get(0).getId()).isNotNull();
        assertThat(team.getTeamPositions().get(0).getStacks()).hasSize(2)
                .extracting("stack")
                .containsExactlyInAnyOrderElementsOf(stacks);
    }

    @DisplayName("팀을 수정할 수 있다.")
    @Test
    void updateTeam() throws Exception {
        // given
        User leader = getUser("test@test.com");
        userRepository.save(leader);

        Team originalTeam = createOriginalTeam(leader); // prefix: original test
        teamRepository.save(originalTeam);

        TeamRequest request = getTeamCreateRequest(); // prefix: edited test

        // when
        Team editedTeam = teamService.update(leader, originalTeam.getId(), request);

        // then
        assertThat(editedTeam).extracting("type", "title", "description")
                .containsExactly(TeamType.PROJECT, "edited test title", "edited test description");

        // 팀 구성원들
        assertThat(editedTeam.getTeamPositions()).hasSize(1)
                .extracting("role", "maxCount")
                .contains(tuple(PositionType.BE, 5));

        // 팀 구성원의 스택 정보
        assertThat(editedTeam.getTeamPositions().get(0).getStacks()).hasSize(2)
                .extracting("stack")
                .containsExactlyInAnyOrderElementsOf(List.of("edited test spring", "edited test nodejs"));

    }

    @DisplayName("팀의 리더가 아니면 팀을 수정할 수 없다.")
    @Test
    void whenNotLeaderThenCannotUpdateTeam() throws Exception {
        // given
        User leader = getUser("leader@leader.com");
        User other = getUser("other@other.com");
        userRepository.saveAll(List.of(leader, other));

        Team originalTeam = createOriginalTeam(leader); // prefix: original test
        teamRepository.save(originalTeam);

        TeamRequest request = getTeamCreateRequest(); // prefix: edited test

        // when // then
        Assertions.assertThatThrownBy(() ->
                teamService.update(other, originalTeam.getId(), request)
        ).isInstanceOf(IllegalArgumentException.class).hasMessage("팀장만이 수정할 수 있습니다.");

    }

    private TeamRequest getTeamCreateRequest() {
        String role = "BE";
        int maxCount = 5;
        List<String> stacks = List.of("edited test spring", "edited test nodejs");
        List<TeamPositionDto> teamPositions = List.of(
                TeamPositionDto.builder()
                        .role(role)
                        .maxCount(maxCount)
                        .stacks(stacks)
                        .build());

        String title = "edited test title";
        String desc = "edited test description";
        return TeamRequest.builder()
                .title(title)
                .description(desc)
                .teamPositions(teamPositions)
                .build();
    }

    private Team createOriginalTeam(User leader) {
        Team team = Team.builder()
                .title("original title")
                .description("original description")
                .type(TeamType.PROJECT)
                .leader(leader)
                .build();
        String role = "BE";
        int maxCount = 3;
        List<String> stacks = List.of("original test spring", "original test nodejs");
        List<TeamStack> teamStacks = stacks.stream()
                .map(stack ->
                        TeamStack.builder()
                                .stack(stack)
                                .build()).toList();
        TeamPosition teamPosition = TeamPosition.builder()
                .maxCount(maxCount)
                .role(PositionType.valueOf(role))
                .team(team)
                .stacks(teamStacks)
                .build();
        team.addTeamPositions(List.of(teamPosition));

        return team;
    }

    private User getUser(String mail) {
        return User.builder()
                .email(mail)
                .build();
    }
}