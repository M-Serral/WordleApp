# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),  
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.1.0] - 2025-04-10
### Added
- MySQL integration: replaced in-memory word list with database-based word selection.
- Docker Compose configuration for production deployment.
- Spring profile switching for environments (`local`, `docker`).
- Application connects to MySQL container in production via environment variables.
- Added environment-based `application-docker.properties`.

### Fixed
- Resolved timezone compatibility error in JDBC connection with `serverTimezone`.

## [0.x.y] - 2024-04-01
### Added
- Initial release of WordleApp core functionality.
- Game logic with word validation and user attempts.
- TDD process with tests for each feature: word validation, result feedback, and attempt limits.
- Functional implementation with possible technical debt to evaluate SonarCloud feedback.
- Final refactor to pass SonarCloud quality gates.
- Mustache templating for UI and Spring Boot backend.

