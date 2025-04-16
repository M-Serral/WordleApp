-- available-words.sql

-- Pre-loaded dictionary to validate words in Wordle
-- Only 5-letter words are allowed, in uppercase letters

DELETE FROM available_word;

-- Secret word
INSERT INTO available_word (word) VALUES ('SEXTO');
INSERT INTO available_word (word) VALUES ('NOBLE');

-- WordleIntegrationTest
INSERT INTO available_word (word) VALUES ('APPLE');
INSERT INTO available_word (word) VALUES ('GRAPE');
INSERT INTO available_word (word) VALUES ('MANGO');
INSERT INTO available_word (word) VALUES ('LEMON');
INSERT INTO available_word (word) VALUES ('SUGAR');
INSERT INTO available_word (word) VALUES ('CAMEL');
INSERT INTO available_word (word) VALUES ('BREAK');
INSERT INTO available_word (word) VALUES ('BRICK');

-- WordleAttemptsRestTest
INSERT INTO available_word (word) VALUES ('WRONG');
INSERT INTO available_word (word) VALUES ('NACER');

-- WordleCluesTest
INSERT INTO available_word (word) VALUES ('CLIMA');
INSERT INTO available_word (word) VALUES ('EUROS');
INSERT INTO available_word (word) VALUES ('SESGO');
INSERT INTO available_word (word) VALUES ('RESTO');
INSERT INTO available_word (word) VALUES ('TEXTO');
INSERT INTO available_word (word) VALUES ('TEXTA');
INSERT INTO available_word (word) VALUES ('TEJAS');
INSERT INTO available_word (word) VALUES ('EXTRA');
INSERT INTO available_word (word) VALUES ('CESTO');
INSERT INTO available_word (word) VALUES ('SIETE');

-- WordleUITest
INSERT INTO available_word (word) VALUES ('JUNIO');
INSERT INTO available_word (word) VALUES ('NORTE');
INSERT INTO available_word (word) VALUES ('CUSTA');
INSERT INTO available_word (word) VALUES ('VIGIA');
INSERT INTO available_word (word) VALUES ('MARCA');
INSERT INTO available_word (word) VALUES ('TURMA');
INSERT INTO available_word (word) VALUES ('CAPAR');
INSERT INTO available_word (word) VALUES ('RUMIA');

-- WordleControllerTest
INSERT INTO available_word (word) VALUES ('WRONB');
INSERT INTO available_word (word) VALUES ('WRONC');
INSERT INTO available_word (word) VALUES ('WROND');
INSERT INTO available_word (word) VALUES ('WRONE');
INSERT INTO available_word (word) VALUES ('WRONF');





