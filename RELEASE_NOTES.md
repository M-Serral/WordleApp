# WordleApp v1.3.0 - 2025-04-22

## Overview

This release introduces a **brand new user experience** for player identification and robust session management between frontend and backend. The game now detects if the Spring Boot server has been restarted and safely prompts for a new player name when necessary, preventing incorrect player continuity.

## Key Features

- 🧑‍💻 **Username Modal**: Users now enter their name through a styled modal instead of a native prompt. This improves usability, control, and aesthetics.
- 🔐 **Session Awareness**: The backend generates a `SESSION_ID` for each session, and the frontend compares it against stored session info. If mismatched, it resets all local session data.
- 🎯 **Error Feedback**: Invalid or empty usernames now display a clear message below the input field in red, maintaining layout stability.
- ⌨️ **Enhanced UX**: Users can confirm their name using the Enter key, and only valid inputs (letters, ñ, and numbers) are accepted.
- 🔁 **Session Reset Handling**: If a user closes the game or the server restarts without pressing "Reset Game", a new name will still be requested upon the next access.

## Improvements

- Consistent modal positioning and layout under all viewport conditions.
- Better code structure via controller method refactoring (`ensureSessionId`).
- Full Selenium support through test mode toggle, avoiding modal interruptions during automated testing.

## Upgrade Notes

No manual database changes are required. All session management is handled automatically on both frontend and backend.

---

🛠️ Next up in `1.4.0`: in-game statistics, ranking history, and game history screen!