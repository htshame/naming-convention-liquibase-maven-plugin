# Changelog

All notable changes to this project will be documented in this file.

The format follows [Keep a Changelog](https://keepachangelog.com/en/1.0.0/)
and [Semantic Versioning](https://semver.org/).

---

## Version 1.0.2

- It is now possible to add excluded attributes to `no-hyphens-in-attributes` rule.
- It is now possible to add excluded attributes to `no-underscore-in-attributes` rule.
- Rule `tag-must-exist`: ```excludedAncestorTags``` setting was renamed to ```excludedTags```.
- Rule `attr-ends-with-conditioned`: ```conditionAttribute``` setting was renamed to ```conditionAttr```.
- Rule `attr-ends-with-conditioned`, `attr-ends-with`, `attr-starts-with`: ```targetAttribute``` setting was renamed to
  ```targetAttr```.
- Bugfixes

---

## Version 1.0.1

- Rule `attr-ends-with-conditioned` is implemented
- Bugfixes

---

## Version 1.0.0

Initial implementation.

- Rule `tag-must-exist` is implemented
- Rule `attr-starts-with` is implemented
- Rule `attr-ends-with` is implemented
- Rule `no-hyphens-in-attributes` is implemented
- Rule `no-underscores-in-attributes` is implemented

---

### Previous Releases

- [1.0.2](https://github.com/htshame/naming-convention-liquibase-maven-plugin/blob/main/docs/releases/1.0.2.md)
- [1.0.1](https://github.com/htshame/naming-convention-liquibase-maven-plugin/blob/main/docs/releases/1.0.1.md)
- [1.0.0](https://github.com/htshame/naming-convention-liquibase-maven-plugin/blob/main/docs/releases/1.0.0.md)
