# 📦 Release Notes – v1.4.0

**Release date**: 2025-04-25
**Version**: `1.4.0`  
**Scope**: Frontend + Backend + Persistence + UX  
**Status**: ✅ Completed and validated

## ✨ New Features

- ✅ A new **leaderboard page** allows users to view completed games from all players.
- ✅ Games are now **persisted in the database** upon victory.
- ✅ A **leaderboard button** has been added to the main UI.
- ✅ The leaderboard can be sorted by date or number of attempts.

## 🔁 Improvements

- ✅ The game state (attempts, hints, win/loss status) is now fully **restored after navigation** (e.g., going to `/leaderboard` and returning).
- ✅ The "Reset Game" button now reliably appears when a game ends, even after returning from the leaderboard.
- ✅ Added **defensive checks** to prevent JavaScript crashes on pages where elements may be missing (e.g., sort select on non-leaderboard views).
- ✅ Improved robustness in how frontend and backend maintain the end-of-game state.

## 🐛 Bug Fixes

- 🐞 Fixed an issue where the last tile in the first row would remain highlighted after navigating.
- 🐞 Fixed cases where the game was blocked or unresponsive after clicking reset.
- 🐞 Fixed an edge case where the leaderboard button caused session issues after winning or losing.

## 🛡️ Quality Enhancements

- 🧹 Made backend `HttpSession` usage SonarQube-compliant by storing only `String` values (not entity objects).
- 💾 Improved consistency between frontend game flow and backend persistence logic.
- 🧪 Added client-side guards to ensure DOM is only manipulated if elements exist.

---