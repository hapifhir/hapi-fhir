# Contributors Guide — HAPI FHIR

Welcome to the **HAPI FHIR** project!  
We’re happy that you’re interested in contributing to the world's best FHIR implementation.  
This document outlines how to contribute effectively and how to record your changes for release tracking.

---

## How to Contribute

We welcome contributions of all kinds:

- 🐛 Bug fixes and improvements
- ✨ New features
- 📘 Documentation updates
- 🧪 Test coverage enhancements

### 1. Fork and Branch

1. Fork the [HAPI FHIR repository](https://github.com/hapifhir/hapi-fhir)
2. Create a feature branch:

```bash
git checkout -b feature/your-feature-name
```

### 2. Code Style

- Read our [hacking guide documentation](hapi-fhir-docs/src/main/resources/ca/uhn/hapi/fhir/docs/contributing/hacking_guide.md).
- We are currently using Java 17.
- Format your code to match style with `mvn spotless:apply`
- Use descriptive commit messages
- Include JUnit 5 tests for all new functionality

### 3. Submitting a Pull Request

- Ensure your PR description clearly states the intent of your change
- Reference any relevant issues with `Fixes #1234`
- Each PR **must include a changelog entry** (see below)

---

## 🧾 Changelog Entries (Required)

HAPI FHIR uses a **YAML-based changelog** to maintain a structured record of changes.  
Each contribution must include a new file in:

```
hapi-fhir-docs/src/main/resources/ca/uhn/hapi/fhir/changelog/[CURRENT_RELEASE]
```

Significant changes must also be described in the release upgrade notes in 
```
hapi-fhir-docs/src/main/resources/ca/uhn/hapi/fhir/changelog/[CURRENT_RELEASE]/upgrade.md
```

### Changelog File Naming

```
PPPP-short-description.yaml
```
Where PPPP is the number of the PR or Issue.

### Changelog File Format

Each changelog entry must include:

```yaml
---
type: add|fix|perf|change|remove|security
issue: 1234          # GitHub issue/PR number
title: :"Changelog summary.
    For bugs, describe the broken behaviour, and how it has changed.  
    For features, describe the new feature."
```

**Example:**

[7297-improve-include-canonical-detection.yaml](hapi-fhir-docs/src/main/resources/ca/uhn/hapi/fhir/changelog/8_6_0/7297-improve-include-canonical-detection.yaml)

```yaml
---
type: perf
issue: 7297
title: "The code which detects whether a reference SearchParameter could potentially
   target a CanonicalReference (as opposed to only a standard reference) has been
   improved. This should result in fewer `_include` and `_revinclude` queries being
   processed with an additional canonical lookup, improving speed in these cases."
```
---

## 📘 Documentation

The documentation for Hapi FHIR is compiled from Markdown files in [hapi-fhir-docs/src/main/resources/ca/uhn/hapi/fhir/docs](hapi-fhir-docs/src/main/resources/ca/uhn/hapi/fhir/docs).
Substantial changes are documented there.
Any changes to the JPA schema are documented in [schema.md]
(hapi-fhir-docs/src/main/resources/ca/uhn/hapi/fhir/docs/server_jpa/schema.md).
Document any new operations, or important features.

---

## 🧹 Code Review Process

- All PRs require at least one reviewer approval
- CI checks (tests, linting, changelog validation) must pass
- Reviewers may request updates for clarity, style, or documentation

---

## 🏷️ Attribution

Contributors are listed in the `<developers>` section of the root `pom.xml`.  


## Thank You

Thank you for contributing to Hapi.
