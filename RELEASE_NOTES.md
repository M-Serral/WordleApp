# 🚀 Release Notes – v1.1.0

WordleApp version 1.1.0 introduces backend persistence using MySQL and a production-ready deployment setup via Docker Compose. This version marks the beginning of the second development phase (TFG Phase 2).

---

## ✨ New Features

- Added `SecretWord` JPA entity to manage secret words in the database.
- Initialized secret words at startup using a `@PostConstruct` service.
- Replaced file-based word loading with database-backed word selection logic.
- Docker Compose setup with `wordle-app` and `wordle-mysql` services.
- Environment-specific configuration using `application-docker.properties`.

## 🛠 Improvements

- Spring profiles allow switching between local and production environments.
- Full Docker support for deployment and testing.

## 🐞 Fixes

- Fixed timezone issues in JDBC connection with `serverTimezone=Europe/Madrid`.

## 📌 How to Deploy

```bash
docker-compose down -v
docker-compose up --build --remove-orphans
```

Visit: [http://localhost:8080](http://localhost:8080)

---

## 🔍 Notes

This release finalizes the database migration objective and prepares the foundation for storing game data and implementing rankings in future versions.
