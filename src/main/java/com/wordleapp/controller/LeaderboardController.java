package com.wordleapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LeaderboardController {

    @GetMapping("/leaderboard")
    public String showLeaderboard() {
        return "leaderboard";
    }
}
