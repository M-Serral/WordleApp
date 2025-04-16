package com.wordleapp.repository;

import com.wordleapp.model.AvailableWord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvailableWordRepository extends JpaRepository<AvailableWord, Long> {

    boolean existsByWord(String word);
}
