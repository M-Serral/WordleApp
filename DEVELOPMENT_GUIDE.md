# 📦 WordleApp – Development Workflow

This document describes the internal development workflow used in the WordleApp project, following semantic versioning, continuous integration, and best practices in code quality.

---

## 🧭 Branching Strategy

- Main branch: `master`
- Feature branches: `feature/x.y.z` or `feature/<short-description>`
- Only merge into `master` when a version is complete and stable

---

## 🧪 Development per Version (`semver`)

### Example: Current version in development → `1.1.0`

### 1. Create a feature branch

    git checkout -b feature/1.1.0

Or:

    git checkout -b feature/migrate-to-mysql

---

### 2. Follow commit message conventions

Use short, clear messages based on commit type:

| Type    | Use When...                                                                 |
|---------|------------------------------------------------------------------------------|
| `feat:` | You add a **new feature** visible to the user or that extends functionality. |
| `fix:`  | You fix a **bug** or correct unexpected behavior.                            |
| `refactor:` | You improve code structure **without changing behavior**.               |
| `chore:` | You make internal changes like build scripts, configs, or cleanup.         |
| `docs:` | You create or update **documentation files**.                                |
| `test:` | You add or modify **unit/integration tests**.                                |

---

#### ✅ Example commit flow from WordleApp:

---

| Commit Message                                                      | Prefix      | Justification                                           |
|---------------------------------------------------------------------|-------------|---------------------------------------------------------|
| `feat: create SecretWord entity`                                    | `feat:`     | New domain feature added                                |
| `feat: add SecretWordRepository`                                    | `feat:`     | Enables DB access, new capability                       |
| `feat: initialize DB words with @PostConstruct service`             | `feat:`     | Adds a new startup feature                              |
| `refactor: update WordSelectorService to use MySQL instead of file` | `refactor:` | Changes how a feature works internally                  |
| `chore: remove file-based word loading and words.txt`               | `chore:`    | Removes unused internal code, no user impact            |
| `fix: handle random selection errors when DB is empty`              | `fix:`      | Prevents crash or logic error when DB is empty          |
| `docs: update README for v1.1.0 with Docker and MySQL setup`        | `docs:`     | Pure documentation update                               |
| `docs: add changelog entries for v1.0.0 and v1.1.0`                 | `docs:`     | Maintains version history                               |
| `docs: add release notes for v1.1.0`                                | `docs:`     | Release documentation                                   |
| `docs: add development workflow guide`                              | `docs:`     | Team/internal documentation                             |
| `docs: add release process checklist for version publishing`        | `docs:`     | Practical doc for future maintainers                    |

---

### 3. Update the changelog (`CHANGELOG.md`)

While working on a version, keep track of what's added, changed, or removed:

    ## [v1.1.0] - YYYY-MM-DD

    ### ✨ Added
    - SecretWord entity and repository
    - Service to preload DB at startup

    ### 🧼 Changed
    - WordSelectorService now uses MySQL

    ### 🗑️ Removed
    - File dependency (`words.txt`)

    ### 🧪 Quality
    - Ready for Docker-based deployment

---

### 4. Merge, tag and release

When the version is complete:

    git checkout master
    git merge feature/1.1.0
    git tag -a v1.1.0 -m "Migrate word logic to MySQL and enable Docker deployment"
    git push origin master
    git push origin v1.1.0


## 🔁 Iterating Further

Start a new version:

    git checkout -b feature/1.2.0

Repeat the same commit → changelog → merge → tag → release process.

---

## ✅ Benefits of this workflow

- Clean and controlled releases
- Clear historical trace of features and fixes
- Easy to roll back or re-test a version
- Professional-grade CI/CD compatibility

---