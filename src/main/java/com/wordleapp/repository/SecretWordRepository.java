package com.wordleapp.repository;

import com.wordleapp.model.SecretWord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SecretWordRepository extends JpaRepository<SecretWord, Long> {
}
