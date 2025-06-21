# Release Notes - WordleApp v1.5.4
Leaderboard Performance Refactor

## ✅ What’s New

This patch improves the **performance, maintainability, and clarity** of the leaderboard functionality by delegating sorting logic to the persistence layer.

### ✨ Highlights

- **Leaderboard Sorting Optimized**:
  - Replaced in-memory sorting in `LeaderboardService` with database-level ordering using `findAllByOrderByAttemptsAsc()` and `findAllByOrderByCreatedAtDesc()` from Spring Data JPA.
  - Reduces memory consumption and improves query efficiency.

- **Code Quality Aligned with SRP**:
  - Refactored the service to follow the Single Responsibility Principle.
  - Sorting is now handled where it belongs: in the repository.

---

