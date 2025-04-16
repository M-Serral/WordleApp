package com.wordleapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@ToString
@Entity
public class AvailableWord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false, unique = true, length = 5)
    private String word;

    // Constructors
    public AvailableWord() {
    }

    public AvailableWord(String word) {
        this.word = word;
    }
}
