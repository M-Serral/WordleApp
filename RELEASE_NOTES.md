# WordleApp v1.2.2 - 2025-04-20

This patch release fixes a frontend visual feedback issue related to invalid word inputs.

## 🐞 Bug Fixes

- Fixed a UI bug where invalid words triggered a mixed orange, green or grey tile background.
- Fixed bad behavior when we enter an invalid word just after entering a non dictionary-contained word.
- Ensured that feedback messages refresh even when repeated (e.g., multiple invalid guesses).