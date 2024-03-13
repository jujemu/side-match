package com.sidematch.backend.domain.team;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class TeamStack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_position_id")
    private TeamPosition teamPosition;

    @Builder
    private TeamStack(String name, TeamPosition teamPosition) {
        this.name = name;
        this.teamPosition = teamPosition;
    }
}
