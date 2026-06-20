# Changelog

All notable changes to this project will be documented in this file.

The format follows [Keep a Changelog](https://keepachangelog.com/en/1.0.0/) and [Semantic Versioning](https://semver.org/).

---

## Version 4.1

- added the ability to fetch `rules` and `exclusions` files from remote URLs. Plugin config parameters `<rulesFileUrl>` and `<exclusionsFileUrl>` were added to support this change
- added support for Gradle version of this plugin

---

## Version 4.0

- added the ability to generate content of the exclusions file.
  If your changeLog has too many errors that can not or should not be fixed, just add parameter
  `<shouldGenerateExclusions>true</shouldGenerateExclusions>`
  to plugin configuration, and the content of your exclusions file will be generated in the logs upon plugin execution.
  Default value of this parameter is `false`.

---

## Version 3.1

- update jackson-databind to version 2.22.0
- update maven-plugin-api to version 3.9.16

---

## Version 3.0

- Implement `tag-must-not-exist-in-changelog` rule
- Implement `changeLogExclusion` exclusion

---

## Version 2.12

- Implement `changelog-must-end-with-newline` rule

---

## Version 2.11

- Implement `no-trailing-spaces-in-changelog` rule

---

### Previous Releases

- [4.1](https://github.com/htshame/naming-convention-liquibase-maven-plugin/blob/main/docs/releases/4.1.md)
- [4.0](https://github.com/htshame/naming-convention-liquibase-maven-plugin/blob/main/docs/releases/4.0.md)
- [3.1](https://github.com/htshame/naming-convention-liquibase-maven-plugin/blob/main/docs/releases/3.1.md)
- [3.0](https://github.com/htshame/naming-convention-liquibase-maven-plugin/blob/main/docs/releases/3.0.md)
- [2.12](https://github.com/htshame/naming-convention-liquibase-maven-plugin/blob/main/docs/releases/2.12.md)
- [2.11](https://github.com/htshame/naming-convention-liquibase-maven-plugin/blob/main/docs/releases/2.11.md)
- [2.10](https://github.com/htshame/naming-convention-liquibase-maven-plugin/blob/main/docs/releases/2.10.md)
- [2.9](https://github.com/htshame/naming-convention-liquibase-maven-plugin/blob/main/docs/releases/2.9.md)
- [2.8](https://github.com/htshame/naming-convention-liquibase-maven-plugin/blob/main/docs/releases/2.8.md)
- [2.7](https://github.com/htshame/naming-convention-liquibase-maven-plugin/blob/main/docs/releases/2.7.md)
- [2.6](https://github.com/htshame/naming-convention-liquibase-maven-plugin/blob/main/docs/releases/2.6.md)
- [2.5](https://github.com/htshame/naming-convention-liquibase-maven-plugin/blob/main/docs/releases/2.5.md)
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
