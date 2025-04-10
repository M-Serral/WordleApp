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

   docker-compose up --build --remove-orphans

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

## 🧾 License

MIT © 2025 M-Serral

---

## 👨‍🎓 Final Year Project (TFG)

This project was developed as part of a Bachelor's Final Year Project (TFG) focused on software development quality. It demonstrates the use of:

- TDD for functional coverage
- CI/CD to automate build and deployment
- Static code analysis to manage technical debt
- Integration testing with real MySQL and Docker Compose
