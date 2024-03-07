package com.sidematch.backend.domain.team.controller;

import com.sidematch.backend.domain.team.Team;
import com.sidematch.backend.domain.team.service.TeamService;
import com.sidematch.backend.domain.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class TeamController {

    private final TeamService teamService;

    @PostMapping("/team")
    public ResponseEntity<Void> create(@Valid @RequestBody TeamCreateOrUpdateRequest request,
                                       Authentication authentication) {
        User leader = getUser(authentication);
        Team team = teamService.create(leader, request);
        log.info(request.getTitle() + "팀이 생성되었습니다");

        return ResponseEntity.created(URI.create("/team/" + team.getId())).build();
    }

    @GetMapping("/team/{teamId}/info")
    public ResponseEntity<TeamDetailResponse> showDetail(@PathVariable Long teamId) {
        return ResponseEntity.ok(teamService.showDetail(teamId));
    }

    @PutMapping("/team/{teamId}")
    public ResponseEntity<Void> update(@PathVariable Long teamId,
                                       @Valid @RequestBody TeamCreateOrUpdateRequest request,
                                       Authentication authentication) {
        User leader = getUser(authentication);
        teamService.update(leader, teamId, request);
        log.info("팀이 수정되었습니다.");

        return ResponseEntity.ok().build();
    }

    private User getUser(Authentication authentication) {
        if (authentication == null) {
            throw new IllegalArgumentException("등록되지 않은 사용자는 팀을 생성할 수 없습니다.");
        }

        User leader = (User) authentication.getPrincipal();
        return leader;
    }
}
