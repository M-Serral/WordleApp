# Release Notes - WordleApp v1.5.2

## ☕ Java Version Upgrade

- Upgraded Java runtime from version 17 to version 21 (LTS).
- Updated `pom.xml` to use Java 21 for source and target compatibility.
- Updated GitHub Actions to use Java 21 with `actions/setup-java`.
- Updated Dockerfile to use a base image with Java 21 (Eclipse Temurin).

## 🧪 Validation

- Application fully tested after upgrade with existing test suite.
- No regressions or compatibility issues were found during upgrade.

## 📦 Summary

This version includes an environment-level upgrade to ensure the application is aligned with the latest supported LTS Java version. No functional changes were introduced, but SemVer was incremented to `1.5.2` to reflect the underlying platform update.
