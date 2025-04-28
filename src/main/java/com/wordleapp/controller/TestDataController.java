package com.wordleapp.controller;

import com.wordleapp.test.TestDataLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestDataController {

    private final TestDataLoader testDataLoader;

    public TestDataController(TestDataLoader testDataLoader) {
        this.testDataLoader = testDataLoader;
    }

    @GetMapping("/insert-selected-word-games")
    public ResponseEntity<String> insertSelectedWordGames() {
        testDataLoader.insertTestGames();
        return ResponseEntity.ok("✅ Test games inserted successfully!");
    }
}
