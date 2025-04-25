package com.wordleapp.dto;

import java.time.LocalDateTime;

public record LeaderboardEntry(
        String username,
        String word,
        int attempts,
        LocalDateTime date
) {}
