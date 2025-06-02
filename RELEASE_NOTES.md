# Release Notes - WordleApp v1.5.3 Java 21 Upgrade and Compatibility Fixes

## ✅ What’s New

This patch focuses on **environment compatibility**, ensuring the application is future-proof and aligns with industry standards.

### ✨ Highlights
- **Upgraded Java Version to 21**:
    - Updated project configuration (Maven and Docker) to compile using `--release 21`.
    - Ensures compatibility with the latest LTS Java version, boosting long-term support and performance.

- **Local Development Configuration Improved**:
    - Updated IDE and system settings to point to JDK 21.
    - Fixed build errors such as `release version 21 not supported`.

- **Dockerfile Updated**:
    - Multi-stage Docker build now uses `eclipse-temurin-21` and `openjdk:21-jdk-slim`.

- **CI/CD Reliability Increased**:
    - GitHub Actions now builds and deploys Docker images compatible with Java 21.

---