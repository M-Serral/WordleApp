# WordleApp 🟩🟧⬛

**WordleApp** is a web-based implementation of the classic Wordle game, built with Spring Boot and MySQL, and developed with a strong focus on software quality: TDD, CI/CD, SonarQube analysis, UI testing using Selenium, and API test using RestAssured.
## 🚀 Technologies

- Java 17
- Spring Boot 3.2.x
- MySQL 8
- Maven
- Docker & Docker Compose
- GitHub Actions
- Selenium WebDriver
- Mustache Templates
- SonarQube / SonarCloud

---

## 🐳 Deploy with Docker Compose

### 📦 Prerequisites

- Docker
- Docker Compose
- Git

### ▶️ Run the app

1. Clone the repository:

   git clone https://github.com/M-Serral/WordleApp.git
   cd WordleApp

2. Launch the app and MySQL with Docker:

   "docker-compose up --build --remove-orphans" (use "docker-compose down -v" after first run
    to clean up containers, networks, volumes, etc.)

This will start two containers:
- `wordle-mysql`: MySQL database container
- `wordle-app`: your Spring Boot application on port `8080`

### 🌐 Access the application

Visit:

    http://localhost:8080

---

## ⚙️ Environment variables (in `docker-compose.yml`)

| Variable                     | Description                            |
|-----------------------------|----------------------------------------|
| `SPRING_DATASOURCE_URL`     | JDBC connection URL                    |
| `SPRING_DATASOURCE_USERNAME`| Database username                      |
| `SPRING_DATASOURCE_PASSWORD`| Database password                      |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | Table creation policy            |

These variables are injected into Spring Boot via `application-docker.properties`.

---

## ✅ Software Quality

- ✔️ Unit Testing (JUnit + Mockito)
- ✔️ Integration Testing
- ✔️ UI Testing (Selenium + WebDriverManager)
- ✔️ Static Code Analysis (SonarLint + SonarQube)
- ✔️ Continuous Integration / Deployment via GitHub Actions:
    - Build & test on every pull request
    - Publish Docker image on merge to `master`

---

## 📦 CI/CD Pipeline

On every push to `master`, the GitHub Actions workflow:

- Executes tests
- Runs SonarQube analysis
- Builds Docker image
- Publishes version tag

---

## 📸 Recommended screenshots for the project report

- Output of `docker ps` showing `wordle-app` and `wordle-mysql`
- Application running at `http://localhost:8080`
- SonarQube dashboard with coverage and quality gates
- GitHub Actions pipeline execution
- Sample gameplay UI with results

---

# TestDataLoader - Manual Usage Documentation

## ✨ Purpose

This component was created to manually insert controlled test games into the database for validation purposes, specifically to test the "Ranking by Secret Word" feature without needing manual gameplay.

By isolating test data creation from application startup, we ensure that the production environment remains clean and that the database state is only modified when explicitly intended.

---

## 📆 How It Works

- `TestDataLoader` is a Spring component located under `src.main.java.com.wordleapp.test`.
- It **does not** insert data automatically when the application starts.
- It exposes a **manual method** `insertTestGames()` that can be triggered through a dedicated REST endpoint.

A supporting controller, `TestDataController`, was created for this purpose.

---

## 🔗 Endpoint for Manual Test Data Insertion

| Method | URL | Description |
|:---|:---|:---|
| `GET` | `/api/test/insert-selected-word-games` | Inserts 5 test games associated with the secret word `"LIBRO"`. |

### Example Usage

- Using a web browser:

  ```
  http://localhost:8080/api/test/insert-selected-word-games
  ```

- Or using `curl`:

  ```bash
  curl -X GET http://localhost:8080/api/test/insert-selected-word-games
  ```

If successful, the server will respond:

```text
✅ Test games for LIBRO inserted successfully!
```

---

## 🔄 Data Inserted

- 6 `Game` entries into the database.
- Different usernames (`User1`, `User2`, ..., `User5`).
- Various numbers of attempts (1 to 4).
- Different creation timestamps.

---

## ⚠️ Why Manual Trigger?

- Prevents unintended data pollution during normal application use.
- Supports reproducible test scenarios without impacting production.
- Easy to remove after validation.

---

## ✅ Cleanup

After testing:

- **Delete `TestDataController` and `TestDataLoader`**, or
- **Comment out** the endpoint methods.

No residual changes will affect the application.

---

## 🖋️ Best Practice Justification

Inserting test data via controlled HTTP endpoints is a recognized best practice in professional software testing.
It ensures:

- Separation of concerns.
- Minimal risk to production environments.
- Repeatable validation.

---

## 🔹 Bonus Tip

Include a screenshot of the successful endpoint call (browser or Postman) in your TFG memory to strengthen the validation proof.


## 🧾 License

MIT © 2025 M-Serral

---

## 👨‍🎓 Final Year Project (TFG)

This project was developed as part of a Bachelor's Final Year Project (TFG) focused on software development quality. It demonstrates the use of:

- TDD for functional coverage
- CI/CD to automate build and deployment
- Static code analysis to manage technical debt
- Integration testing with real MySQL and Docker Compose
- Dictionary-based Word Validation (v1.2.0)

    Starting in version 1.2.0, the application uses two separate data files to initialize its database:
    
    - `dictionary.txt`: loaded into the `available_word` table, defines which words are valid to guess.
    - `words.txt`: contains around 300 curated, easier words used as the game's secret word.
    
    All logic is database-driven. If a guessed word isn't in the dictionary, it's rejected and does not count as an attempt.
