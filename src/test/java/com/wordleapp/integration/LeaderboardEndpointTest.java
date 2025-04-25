package com.wordleapp.integration;

import com.wordleapp.controller.LeaderboardController;
import com.wordleapp.dto.LeaderboardEntry;
import com.wordleapp.service.LeaderboardService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = LeaderboardController.class)

class LeaderboardEndpointTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LeaderboardService leaderboardService;

    @Test
    @DisplayName("Should return a valid leaderboard ordered by attempts")
    void shouldReturnLeaderboardByAttempts() throws Exception {
        List<LeaderboardEntry> mockList = List.of(
                new LeaderboardEntry("Ana", "CASAS", 2, LocalDateTime.of(2025, 4, 24, 15, 30)),
                new LeaderboardEntry("Luis", "RATON", 4, LocalDateTime.of(2025, 4, 24, 12, 0))
        );

        when(leaderboardService.getLeaderboard("attempts")).thenReturn(mockList);

        mockMvc.perform(get("/api/wordle/leaderboard?orderBy=attempts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].username").value("Ana"))
                .andExpect(jsonPath("$[0].word").value("CASAS"))
                .andExpect(jsonPath("$[0].attempts").value(2))
                .andExpect(jsonPath("$[0].date").exists())
                .andExpect(jsonPath("$[1].username").value("Luis"))
                .andExpect(jsonPath("$[1].word").value("RATON"))
                .andExpect(jsonPath("$[1].attempts").value(4));
    }
}
