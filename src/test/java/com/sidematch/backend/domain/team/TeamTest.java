package com.sidematch.backend.domain.team;

import com.sidematch.backend.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TeamTest {

    @DisplayName("팀은 제목과 설명, 타입을 갖는다.")
    @Test
    void createTeam() throws Exception {
        //given
        User leader = User.builder()
                .email("test@test.com")
                .build();

        String title = "test title";
        String desc = "test description";
        TeamType type = TeamType.PROJECT;

        //when
        Team team = Team.builder()
                .title(title)
                .description(desc)
                .type(type)
                .leader(leader)
                .build();

        //then
        assertThat(team).extracting("title", "description", "type")
                .containsExactly(title, desc, type);
        assertThat(team.isDeleted()).isFalse();
        assertThat(team.isFinished()).isFalse();
    }
}