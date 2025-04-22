package com.wordleapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@Entity
public class SecretWord {

    // Getters & Setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false, unique = true, length = 5)
    private String word;

    // Constructors
    public SecretWord() {
    }

    public SecretWord(String word) {
        this.word = word;
    }
}
