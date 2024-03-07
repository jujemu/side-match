package com.sidematch.backend.domain.team;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class TeamPosition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer currentCount;
    private Integer maxCount;
    @Enumerated(EnumType.STRING)
    private PositionType role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @OneToMany(mappedBy = "teamPosition", cascade = CascadeType.ALL)
    private List<TeamStack> stacks;

    @Builder
    private TeamPosition(Integer maxCount, PositionType role, Team team, List<TeamStack> stacks) {
        this.currentCount = 0;
        this.maxCount = maxCount;
        this.role = role;
        this.team = team;
        this.stacks = stacks;
    }
}
