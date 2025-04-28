# Release Notes - WordleApp v1.5.0

## New Features
- 🎯 Introduced a Ranking screen to allow filtering users by secret word, showing also tje number of attempts.
- 🔤 Enhanced the search experience with an editable input field for secret words.
- 🔍 Added Top N filter options (Top 1, Top 3, Top 5) with tie management.

## Improvements
- 🖌️ Unified the table design across Leaderboard and Ranking screens with a new CSS file (`tables.css`).
- 🛠️ Added backend services to support the new features in a modular and scalable way.

## Testing and Validation
- ✅ Successfully validated new endpoints manually and via automated integration tests.
- ✅ Created temporary utilities (`TestDataLoader`, `TestDataController`) for inserting controlled test data.

## Important Notes
- The `TestDataLoader` must be manually triggered and should be removed after validation to avoid polluting production databases.
