
package com.wordleapp.controller;

import com.wordleapp.dto.LeaderboardEntry;
import com.wordleapp.service.LeaderboardService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wordle")
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    public LeaderboardController(LeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
    }

    @GetMapping("/leaderboard")
    public List<LeaderboardEntry> getLeaderboard(@RequestParam String orderBy) {
        return leaderboardService.getLeaderboard(orderBy);
    }
}
