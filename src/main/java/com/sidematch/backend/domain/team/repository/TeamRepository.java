package com.sidematch.backend.domain.team.repository;

import com.sidematch.backend.domain.team.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
