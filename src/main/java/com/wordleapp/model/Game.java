package com.wordleapp.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false)
    private String username;

    @Setter
    @Column(nullable = false)
    private int attempts;

    @Setter
    @ManyToOne
    private SecretWord secretWord;

    @CreationTimestamp
    private LocalDateTime createdAt;

    // Constructors
    public Game() {
    }

    public Game(String username, int attempts, SecretWord secretWord, LocalDateTime createdAt) {
        this.attempts = attempts;
        this.secretWord = secretWord;
        this.username = username;
        this.createdAt = createdAt;
    }
}
