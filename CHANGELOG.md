# Changelog

All notable changes to this project will be documented in this file.

The format follows [Keep a Changelog](https://keepachangelog.com/en/1.0.0/)
and [Semantic Versioning](https://semver.org/).

---

## Version 2.1

- Rule `tag-must-exist`, tag `<excludedTags>`, which was used to exclude tags from the check was removed.
- Rule `tag-must-exist`, tag `<requiredForChildTags>` was added. It's designed to enforce the rule on child tags. 
- Improve logging.
- Improve comments.

---

## Version 2.0

- Refactor rules to make them agnostic of changeLog format.
- Improve logging.

---

## Version 1.0.5

- Fix issue with exclusion processing for `tag-must-be-present` rule
- Improve logging
- Strengthen XSD schema for the ruleSet file

---

## Version 1.0.4

- Fix issue with changeSet processing.
- Improve logging.
- Add the ability to exclude separate changeSet.

---

## Version 1.0.3

- Resolved issue causing exclusion rules to be ignored.

---

## Version 1.0.2

- It is now possible to add excluded attributes to `no-hyphens-in-attributes` rule.
- It is now possible to add excluded attributes to `no-underscore-in-attributes` rule.
- Rule `tag-must-exist`: ```excludedAncestorTags``` setting was renamed to ```excludedTags```.
- Rule `attr-ends-with-conditioned`: ```conditionAttribute``` setting was renamed to ```conditionAttr```.
- Rule `attr-ends-with-conditioned`, `attr-ends-with`, `attr-starts-with`: ```targetAttribute``` setting was renamed to
  ```targetAttr```.
- Added plugin configuration: 
```xml
<shouldFailBuild>true</shouldFailBuild>
```
Default value is `true`. Build will not fail if set to `false`. 
- Bugfixes

---

### Previous Releases

- [2.1](https://github.com/htshame/naming-convention-liquibase-maven-plugin/blob/main/docs/releases/2.1.md)
- [2.0](https://github.com/htshame/naming-convention-liquibase-maven-plugin/blob/main/docs/releases/2.0.md)
- [1.0.5](https://github.com/htshame/naming-convention-liquibase-maven-plugin/blob/main/docs/releases/1.0.5.md)
- [1.0.4](https://github.com/htshame/naming-convention-liquibase-maven-plugin/blob/main/docs/releases/1.0.4.md)
- [1.0.3](https://github.com/htshame/naming-convention-liquibase-maven-plugin/blob/main/docs/releases/1.0.3.md)
- [1.0.2](https://github.com/htshame/naming-convention-liquibase-maven-plugin/blob/main/docs/releases/1.0.2.md)
- [1.0.1](https://github.com/htshame/naming-convention-liquibase-maven-plugin/blob/main/docs/releases/1.0.1.md)
- [1.0.0](https://github.com/htshame/naming-convention-liquibase-maven-plugin/blob/main/docs/releases/1.0.0.md)
