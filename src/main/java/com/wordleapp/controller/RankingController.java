
package com.wordleapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RankingController {

    @GetMapping("/ranking")
    public String showRankingPage() {
        return "ranking";
    }
}
