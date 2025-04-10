# ✅ Release Process Guide – WordleApp

This document outlines the official steps to publish a new version of WordleApp, including Git tagging, GitHub Releases, and DockerHub integration.

---

## 🚀 1. Complete your version development

- Finish all code and documentation changes in your `feature/x.y.z` branch.
- Ensure tests pass and SonarQube analysis is clean.

---

## 📦 2. Merge into `master`

```bash
git checkout master
git merge feature/x.y.z
```

---

## 🏷️ 3. Tag the release

```bash
git tag -a vX.Y.Z -m "Describe the release briefly"
git push origin vX.Y.Z
```

---

## 🐳 4. Let the CI/CD workflow build Docker image

- Your GitHub Actions workflow will detect the push to `master`
- It will automatically build and push the Docker image to DockerHub
- DockerHub tag should match `vX.Y.Z`, for example: `1.1.0-{commit_hash}`

---

## 📝 5. Publish GitHub release

1. Go to the **"Releases"** tab in your GitHub repository.
2. Click **"Draft a new release"**.
3. Fill the form:
   - **Tag**: `vX.Y.Z` (must match the pushed Git tag)
   - **Target branch**: `master`
   - **Title**: `WordleApp vX.Y.Z`
   - **Description**: Paste the contents of `RELEASE_NOTES.md`
4. Click **"Publish release"**

---

## ✅ 6. Verify everything

- `git tag` exists and is pushed
- Release appears in GitHub with notes
- DockerHub shows the image with the correct version
- `CHANGELOG.md` is updated and committed
- Project is ready for the next feature branch

---

## 💡 Notes

- Use semantic versioning: `MAJOR.MINOR.PATCH`
- Release tags and Docker image tags should always match
- Never tag a version directly on `feature/*` branches — always on `master`

