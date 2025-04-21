package com.wordleapp.utils;

public class Constants {

    private Constants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String ATTEMPTS_KEY = "attempts";
    public static final String GAME_WON_KEY = "gameWon";
    public static final int MAX_ATTEMPTS = 6;
    public static final int WORD_LENGTH = 5;
    public static final String LAST_HINT_KEY = "lastHint";
    public static final String HINT_SESSION_KEY = "wordle_hint";
    public static final String HINT = ". Hint: ";
    public static final String ARROW = " → ";
    public static final String SECRET_WORD_KEY = "secret_word";
    public static final String USERNAME_KEY = "username";
    public static final String SESSION_ID = "serverSessionId";

}
