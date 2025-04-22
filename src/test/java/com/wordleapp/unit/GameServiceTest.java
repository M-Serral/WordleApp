package com.wordleapp.unit;

import com.wordleapp.model.Game;
import com.wordleapp.model.SecretWord;
import com.wordleapp.repository.GameRepository;
import com.wordleapp.repository.SecretWordRepository;
import com.wordleapp.service.GameService;
import com.wordleapp.utils.Constants;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private SecretWordRepository secretWordRepository;

    @InjectMocks
    private GameService gameService;

    @Test
    void shouldSaveGameWhenGameIsWonAndSessionIsValid() {
        // Simular HttpSession
        HttpSession session = mock(HttpSession.class);

        when(session.getAttribute(Constants.GAME_WON_KEY)).thenReturn(true);
        when(session.getAttribute(Constants.USERNAME_KEY)).thenReturn("player1");
        when(session.getAttribute(Constants.ATTEMPTS_KEY)).thenReturn(4);
        when(session.getAttribute(Constants.SECRET_WORD_KEY)).thenReturn("HELLO");

        SecretWord secretWord = new SecretWord();
        secretWord.setWord("HELLO");

        when(secretWordRepository.findByWord("HELLO")).thenReturn(Optional.of(secretWord));

        // Ejecutar
        gameService.saveGameIfWon(session);

        // Verificar que el juego se guardó
        verify(gameRepository, times(1)).save(any(Game.class));
    }
}
