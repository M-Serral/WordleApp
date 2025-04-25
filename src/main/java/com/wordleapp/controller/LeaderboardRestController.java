package com.wordleapp.controller;

import com.wordleapp.dto.LeaderboardEntry;
import com.wordleapp.service.LeaderboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/wordle")
public class LeaderboardRestController {

    private final LeaderboardService leaderboardService;

    public LeaderboardRestController(LeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
    }

    @GetMapping("/leaderboard")
    public List<LeaderboardEntry> getLeaderboard(@RequestParam String orderBy) {
        return leaderboardService.getLeaderboard(orderBy);
    }
}

