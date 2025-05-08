package com.wordleapp.controller;

import com.wordleapp.test.TestDataLoader;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * Endpoint for manual insertion of test data only.
 * <p>
 * ⚠️ Protected by explicit confirmation by parameter to avoid accidental executions.
 * <p>
 * This endpoint should only be used in development or validation environments (e.g. TFG Docker environment).
 * To activate it: access http://localhost:8080/api/test/insert-selected-word-games?confirm=true
 */

@Profile({"local", "docker"})
@RestController
@RequestMapping("/api/test")
public class TestDataController {

    private final TestDataLoader testDataLoader;

    public TestDataController(TestDataLoader testDataLoader) {
        this.testDataLoader = testDataLoader;
    }

    /**
     * Inserts test items for a selected secret word.
     * Only executed if the parameter ?confirm=true is received.
     *
     * @param confirm value which must be “true” to run
     * @return response indicating whether it was executed or crashed
     */

    @GetMapping("/insert-selected-word-games")
    public ResponseEntity<String> insertSelectedWordGames(
            @RequestParam(required = false) String confirm) {

        if (!"true".equals(confirm)) {
            return ResponseEntity.status(403).body("🚫 Confirmation required. Add ?confirm=true to execute.");
        }

        testDataLoader.insertTestGames();
        return ResponseEntity.ok("✅ Test games inserted successfully!");
    }

}
