# WordleApp v1.2.0 - 2025-04-16

This release introduces full support for using a dictionary of 5-letter words loaded into the database on application startup. 
It validates user input against this dictionary, ensuring only known words are accepted. Invalid entries are handled gracefully with visual feedback in the UI. 
Backend services were extended and refactored accordingly to support this behavior.

## ✨ New Features

- Dictionary loader from `dictionary.txt` to populate `AvailableWord` table.
- New `SecretWordInitializer` loads 200 "easier" secret words from `words.txt`.
- Added validation logic: words must exist in the `AvailableWord` table to be guessed.
- New frontend animation and behavior when user inputs invalid words.
- Custom `DictionaryNormalizer` utility to transform word sources into valid input files.
- UI logic enhanced to reset input row and show styled error if guess is not in dictionary.

## 🛠 Improvements

- Backend selects random secret word from `SecretWord` table instead of using `AvailableWord`.
- Wordle frontend behavior aligned with real-world game (invalid guesses are not counted).
- All insertions are now deduplicated and uppercased during initialization.

## 🐞 Fixes

- Fixed bug where cursor remained in wrong position after submitting invalid words.
- Fixed exception when secret word list was empty on startup.



## 📌 How to Deploy

```bash
docker-compose down -v
docker-compose up --build --remove-orphans
```

Visit: [http://localhost:8080](http://localhost:8080)

---

## 🔍 Notes

This release is part of the TFG Phase 2: Dictionary-based validation and backend initialization. Released on: 2025-04-16