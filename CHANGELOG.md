# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),  
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.5.2] - 2025-05-XX

### Changed
- Upgraded Java from 17 to 21 (LTS).
- Updated Dockerfile and GitHub Actions accordingly.

## [1.5.1] - 2025-05-06

### Changed
- Refactored RankingService to reduce complexity and improve database efficiency.
- Improved test coverage: added UI tests and non-tie scenarios for rankings.
- Docker image now tagged as `latest` and published automatically via CI.

### Added
- `docker-compose.prod.yml` for production deployment.
- Persistent volume for MySQL container.
- Release artifacts published with each version.

### Security
- Protected test data insertion endpoint with profile and confirmation flag.


## [v1.5.0] - 2025-04-28

### Added
- New Ranking screen to view players ranked by number of attempts for a given secret word.
- RankingController and RankingRestController created.
- RankingService extended to support top N filtering and handling ties.
- Added dynamic input to allow typing or selecting secret words for filtering.
- Created TestDataLoader and TestDataController for manual test data insertion.

### Changed
- Leaderboard and Ranking tables now share unified styling (`tables.css`).
- Updated existing Leaderboard screen to coexist with new Ranking screen.

### Testing
- Added RankingService integration tests for correct top N and ties handling.
- Manual validation of new REST endpoints via browser and Postman.

### Known Issues
- TestDataLoader must be manually triggered; not used automatically on startup.


## [v1.4.0] - 2025-04-25

### Added

- `index.mustache`: leaderboard navigation button and restore logic
- `leaderboard.mustache`: new view to display ranked games
- `WordleController`: `/leaderboard` endpoint
- `GameService`: save completed games to DB
- `main.css`: leaderboard table styling

### Changed

- `WordleGameService`: updated logic to register game end correctly on 6th attempt
- `SessionGameService`: only stores serializable values (e.g., `String`)
- `WordSelectorService`: exposes `getCurrentWord()` for comparison, but not entity
- `index.mustache`: full game state persistence between pages

### Fixed

- Broken UI state after leaderboard navigation
- Reset button disappearing after final attempt
- JavaScript errors from `null` DOM elements
- Invalid persistence of JPA entities in session

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

