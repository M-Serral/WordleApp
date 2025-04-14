# WordleApp v1.1.0 - 2025-04-13

This release introduces persistent storage and startup initialization:

## ✨ Features
- Words are now stored and retrieved from a MySQL database.
- The database is auto-populated with words from `words.txt` if empty.
- A random secret word is selected each time the application starts.

## 🐳 Deployment
- Full Docker support added (`docker-compose.yml`).
- New `application-docker.properties` and `application-local.properties` for profile-based configuration.

## 🧪 Quality
- Integrated `@PostConstruct` initialization to bootstrap the DB.
- Maintained test compatibility with UI and session game logic.
- Added integration tests with H2 and unit tests mocking repository behavior.



## 📌 How to Deploy

```bash
docker-compose down -v
docker-compose up --build --remove-orphans
```

Visit: [http://localhost:8080](http://localhost:8080)

---

## 🔍 Notes

This release finalizes the database migration objective and prepares the foundation for storing game data and implementing rankings in future versions.
