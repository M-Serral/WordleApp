package com.wordleapp.repository;

import com.wordleapp.model.SecretWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SecretWordRepository extends JpaRepository<SecretWord, Long> {

    @Query(value = "SELECT * FROM secret_word ORDER BY RAND() LIMIT 1", nativeQuery = true)
    SecretWord findRandomWord();

}
