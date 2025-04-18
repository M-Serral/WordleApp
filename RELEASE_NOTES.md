# WordleApp v1.2.1 - 2025-04-18

This patch release fixes a minor yet important visual feedback issue in the Wordle UI.

## 🐞 Bug Fixes

- Resolved a UI bug where entering an invalid word multiple times showed no visual feedback after the first attempt.
- Removed static blue coloring and replaced it with a subtle animation on the input row to indicate invalid guesses.
- The error message now flashes when repeated, ensuring consistent feedback for users.

---

This change improves the overall user experience and game clarity by reinforcing the rejection of non-dictionary words even when the same message is repeated.
