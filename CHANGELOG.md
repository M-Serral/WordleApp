# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),  
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [v1.3.0] - 2025-04-22

### Added
- Custom modal for capturing the player's name on game startup.
- Real-time username validation with restricted character set (letters, ñ, numbers).
- Error feedback below input field when name is empty.
- SESSION_ID generation logic to detect backend restarts.
- Frontend logic to detect session mismatch and clear stale data.
- Support for using Enter key to confirm username.
- Session-aware reset logic using `sessionStorage`.

### Changed
- Game no longer uses native `prompt()`; replaced with styled modal.
- `resetGame` no longer triggers modal if username exists and is valid.

### Fixed
- Username persistence between Spring Boot restarts without confirmation.
- UI layout issues caused by dynamic content (e.g. error message shifting layout).
- Modal not centered vertically on certain screen sizes.

### Removed
- Native JavaScript `prompt()` usage.

## [v1.2.2] - 2025-04-20

### 🐞 Bug Fixes
- Fixed a UI bug where invalid words triggered a mixed orange, green or grey tile background.
- Fixed bad behavior when we enter an invalid word just after entering a non dictionary-contained word.
- Ensured that feedback messages refresh even when repeated (e.g., multiple invalid guesses).


## [v1.2.1] - 2025-04-18

### 🐞 Bug Fixes
- Fixed a visual feedback bug where invalid word messages did not reappear when entering the same error multiple times.
- Added animation to invalid input rows to replace static blue coloring.


## [v1.2.0] - 2025-04-16

### ✨ Added
- Word validation using a preloaded dictionary (`dictionary.txt`)
- Automatic loading of 200 secret words from `words.txt`
- Frontend error animations for invalid guesses
- Blue highlight and row reset when guessing unknown words
- Utility class `DictionaryNormalizer` to normalize word sources

### 🧼 Changed
- Backend now selects secret word from `SecretWord` table, not `AvailableWord`
- Deduplicated and uppercased word initialization
- UI behavior: invalid guesses do not count as an attempt

### 🐞 Fixed
- Bug where cursor didn’t reset after entering an invalid word
- Crash on game start when secret word list was empty

## [v1.1.0] - 2025-04-13

### Added
- Secret words are now stored in a MySQL database instead of being read from a file.
- If the database is empty, initial words are loaded from `words.txt` during application startup.
- A random word is selected from the database each time the game starts.
- Integration and unit tests for the new services.

### Changed
- The logic of the word selection has been refactored to use `SecretWordRepository`.

### Technical Improvements
- Introduced `@PostConstruct` and `@DependsOn` for deterministic startup order.
- Externalized configuration using `application-{profile}.properties`.
- Dockerized the application with MySQL using `docker-compose`.


## [v0.x.y] - 2024-04-01

### Added
- Initial release of WordleApp core functionality.
- Game logic with word validation and user attempts.
- TDD process with tests for each feature: word validation, result feedback, and attempt limits.
- Functional implementation with possible technical debt to evaluate SonarCloud feedback.
- Final refactor to pass SonarCloud quality gates.
- Mustache templating for UI and Spring Boot backend.

