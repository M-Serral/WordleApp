
package com.wordleapp.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class LeaderboardController {

    @GetMapping("/leaderboard")
    public String showLeaderboardPage() {
        return "leaderboard"; // Render leaderboard.mustache
    }
}
