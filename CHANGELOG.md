# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),  
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

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

