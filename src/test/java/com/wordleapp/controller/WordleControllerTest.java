package com.wordleapp.controller;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WordleControllerTest {

   @LocalServerPort
   private int port;

   @Autowired
   private TestRestTemplate restTemplate;

   @ParameterizedTest
   @CsvSource({
           "APPLE, Correct!",
           "PEACH, Try again!",
           "APP, Invalid input. The word must be 5 letters long."
   })
   void testWordleGuess(String guess, String expectedResponse) {
      String url = "http://localhost:" + port + "/api/wordle/guess?guess=" + guess;
      ResponseEntity<String> response = restTemplate.postForEntity(url, null, String.class);
      assertEquals(expectedResponse, response.getBody());
   }
}
