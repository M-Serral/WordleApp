package com.wordleapp.repository;

import com.wordleapp.model.Game;
import com.wordleapp.model.SecretWord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findAllByOrderByAttemptsAsc();
    List<Game> findAllByOrderByCreatedAtDesc();
    List<Game> findBySecretWordOrderByAttempts(SecretWord secretWord);

}
