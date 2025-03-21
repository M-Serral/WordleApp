package com.wordleapp.util;

public class Constants {

    private Constants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String SECRET_WORD = "SEXTO";
    public static final String ATTEMPTS_KEY = "attempts";
    public static final String GAME_WON_KEY = "gameWon";
    public static final int MAX_ATTEMPTS = 6;
    public static final int WORD_LENGTH = 5;
    public static final String LAST_HINT_KEY = "lastHint";
    public static final String HINT_SESSION_KEY = "wordle_hint";
    public static final String ARROW = " → ";
}
