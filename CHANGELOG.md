# Changelog

All notable changes to this project will be documented in this file.

The format follows [Keep a Changelog](https://keepachangelog.com/en/1.0.0/)
and [Semantic Versioning](https://semver.org/).

---

## Version 2.4

- Add support for JSON changeLogs
- Add `latest` XSD schema

---

## Version 2.3

- Add `no-uppercase-in-attributes` rule
- Add `no-lowercase-in-attributes` rule
- Add `*` exclusion to exclude all rules
- Improve XSD schema
- Improve logging
- Change mojo name from XML-specific `validate-liquibase-xml` to agnostic `validate-liquibase-changeLog`

---

## Version 2.2

- Add support for YAML/YML changeLog format
- Improve logging.

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

### Previous Releases

- [2.4](https://github.com/htshame/naming-convention-liquibase-maven-plugin/blob/main/docs/releases/2.4.md)
- [2.3](https://github.com/htshame/naming-convention-liquibase-maven-plugin/blob/main/docs/releases/2.3.md)
- [2.2](https://github.com/htshame/naming-convention-liquibase-maven-plugin/blob/main/docs/releases/2.2.md)
- [2.1](https://github.com/htshame/naming-convention-liquibase-maven-plugin/blob/main/docs/releases/2.1.md)
- [2.0](https://github.com/htshame/naming-convention-liquibase-maven-plugin/blob/main/docs/releases/2.0.md)
- [1.0.5](https://github.com/htshame/naming-convention-liquibase-maven-plugin/blob/main/docs/releases/1.0.5.md)
- [1.0.4](https://github.com/htshame/naming-convention-liquibase-maven-plugin/blob/main/docs/releases/1.0.4.md)
- [1.0.3](https://github.com/htshame/naming-convention-liquibase-maven-plugin/blob/main/docs/releases/1.0.3.md)
- [1.0.2](https://github.com/htshame/naming-convention-liquibase-maven-plugin/blob/main/docs/releases/1.0.2.md)
- [1.0.1](https://github.com/htshame/naming-convention-liquibase-maven-plugin/blob/main/docs/releases/1.0.1.md)
- [1.0.0](https://github.com/htshame/naming-convention-liquibase-maven-plugin/blob/main/docs/releases/1.0.0.md)
