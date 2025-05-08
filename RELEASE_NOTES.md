# Release Notes - WordleApp v1.5.1

## 🧼 Technical Improvements

- Refactored `RankingService` to reduce cognitive complexity and meet SonarCloud quality gates.
- Replaced manual sorting logic with a clean and efficient stream-based pipeline.
- Introduced a custom repository method to fetch filtered and sorted `Game` entries directly from the database.

## 🧪 Test Coverage Enhancements

- Added integration test class for non-tied ranking scenarios (`RankingServiceNoTieTest`).
- Added UI tests for Leaderboard and Ranking pages using Selenium WebDriver.
- Ensured all new functionalities are covered by unit or integration tests to maintain high test coverage metrics.

## 🐳 Docker & Deployment

- Created `docker-compose.prod.yml` for production-grade deployment using prebuilt DockerHub image (`mserral/wordleapp:latest`).
- Added persistent volume to the MySQL container for data retention.
- Automated tagging and pushing of the `latest` Docker image in GitHub Actions workflow after merging to `master`.
- Published `docker-compose.prod.yml` as a downloadable release artifact for quick, portable deployments.

## 🔐 Safety Measures

- The `/api/test/insert-selected-word-games` endpoint is only available under the `local` or `docker` Spring profile.
- Protected the test data endpoint to prevent accidental execution using a `?confirm=true` flag.

## 📦 Release Artifacts

- Docker image: `mserral/wordleapp:latest`
- Downloadable files:
    - `docker-compose.prod.yml`
    - `docker-compose.yml`
    - `Dockerfile`

