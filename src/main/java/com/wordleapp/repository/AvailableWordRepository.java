package com.wordleapp.repository;

import com.wordleapp.model.AvailableWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AvailableWordRepository extends JpaRepository<AvailableWord, Long> {
    @Query(value = "SELECT * FROM available_word ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Optional<AvailableWord> findRandomWord();

    boolean existsByWord(String word);
}
