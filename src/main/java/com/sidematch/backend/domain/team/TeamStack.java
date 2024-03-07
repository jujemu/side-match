package com.sidematch.backend.domain.team;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class TeamStack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String stack;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_position_id")
    private TeamPosition teamPosition;

    @Builder
    private TeamStack(String stack, TeamPosition teamPosition) {
        this.stack = stack;
        this.teamPosition = teamPosition;
    }
}
