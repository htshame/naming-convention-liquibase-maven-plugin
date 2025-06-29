### You use [Liquibase](https://github.com/liquibase/liquibase)?

### You work with other developers whom you can't really control with PR reviews?

### Tired of inconsistent names of tables, indexes, foreign keys and other parts of the database schema?

Just use <b>naming-convention-liquibase-maven-plugin</b>!

This plugin allows you to create a set of rules and enforce them.

- If someone names the index 'customer_external_id_idx' instead of 'idx_customer_external_id' (or vice versa), the build
  will fail!

- If someone names the table 'customer-metadata' instead of 'customer_metadata' (or vice versa), the build will fail!

- If someone does not write `<comment>` to the changeSet, the build will fail!

- If someone... - well, you get the point!

---

# How to use it?

1. Create <b>[rules.xml](https://github.com/htshame/naming-convention-liquibase-maven-plugin/blob/main/docs/schema/example/rules_example.xml)</b> (or name it differently) file and provide it in `<pathToRulesFile>`.
2. Create <b>[exclusions.xml](https://github.com/htshame/naming-convention-liquibase-maven-plugin/blob/main/docs/schema/example/exclusions_example.xml)</b> (or name it differently) file (not mandatory) and provide it in `<pathToExclusionsFile>`.
3. Provide the path to the directory with Liquibase XML changeLogs in `<changeLogDirectory>`.
4. Provide `false` in `<shouldFailBuild>` if you want to just see the warnings.
5. Put this into your pom.xml:
    ```xml
    <plugin>
        <groupId>io.github.htshame</groupId>
        <artifactId>naming-convention-liquibase-maven-plugin</artifactId>
        <version>2.3</version>
        <executions>
            <execution>
                <id>validate-changeLog</id>
                <phase>compile</phase>
                <goals>
                    <goal>validate-liquibase-changeLog</goal>
                </goals>
            </execution>
        </executions>
        <configuration>
            <pathToRulesFile>${project.basedir}/src/main/resources/liquibaseNaming/ruleset.xml</pathToRulesFile>
            <pathToExclusionsFile>
                ${project.basedir}/src/main/resources/liquibaseNaming/exclusions.xml
            </pathToExclusionsFile>
            <changeLogDirectory>${project.basedir}/src/main/resources/db</changeLogDirectory>
            <changeLogFormat>xml</changeLogFormat>
            <shouldFailBuild>true</shouldFailBuild>
        </configuration>
    </plugin>
    ```
6. Run your build.

---

## Available rules:

### tag-must-exist

Checks that specified tag exists in every changeSet.

Example:

```xml
<rule name="tag-must-exist">
    <requiredTag>comment</requiredTag>
    <requiredForChildTags>
        <tag>rollback</tag>
    </requiredForChildTags>
</rule>
```

Will check that `<comment>` tag is present inside every changeSet, including child `<rollback>`tag.

---

### attr-starts-with

Checks that specified attribute starts with specified value.

Example:

```xml
<rule name="attr-starts-with">
    <tag>createIndex</tag>
    <targetAttr>indexName</targetAttr>
    <requiredPrefix>idx_</requiredPrefix>
</rule>
```

Will check that each `indexName` attribute of each `<createIndex>` tag starts with `idx_`.

---

### attr-ends-with

Checks that specified attribute ends with specified value.

Example:

```xml
<rule name="attr-ends-with">
    <tag>addForeignKeyConstraint</tag>
    <targetAttr>constraintName</targetAttr>
    <requiredSuffix>_fk</requiredSuffix>
</rule>
```

Will check that each `constraintName` attribute of each `<addForeignKeyConstraint>` tag ends with `__fk`.

---

### attr-ends-with-conditioned

Checks that specified attribute ends with specified value if the certain attribute is present and has certain value.

Example:

```xml
<rule name="attr-ends-with-conditioned">
    <tag>createIndex</tag>
    <conditionAttr>unique</conditionAttr>
    <conditionValue>true</conditionValue>
    <targetAttr>indexName</targetAttr>
    <requiredSuffix>_unique</requiredSuffix>
</rule>
```

Will check that each `indexName` attribute of each `<createIndex>` tag ends with `_unique` if attribute `unique="true"`
is present.

---

### no-hyphens-in-attributes

Checks that hyphens are not present in the changeSet at all.

Example:

```xml
<rule name="no-hyphens-in-attributes">
    <excludedAttrs>
        <attr>defaultValue</attr>
        <attr>defaultValueComputed</attr>
    </excludedAttrs>
</rule>
```

Will check that hyphens `-` are not present in the changeSet at all, except for excluded attributes. Attributes `defaultValue` and `defaultValueComputed` will be ignored.

---

### no-underscores-in-attributes

Checks that underscores are not present in the changeSet at all.

Example:

```xml
<rule name="no-underscores-in-attributes">
    <excludedAttrs>
        <attr>defaultValue</attr>
        <attr>defaultValueComputed</attr>
    </excludedAttrs>
</rule>
```

Will check that underscores `_` are not present in the changeSet at all, except for excluded attributes. Attributes `defaultValue` and `defaultValueComputed` will be ignored.

---

### no-uppercase-in-attributes

Checks that uppercase characters are not present in the changeSet attributes at all.

Example:

```xml
<rule name="no-uppercase-in-attributes">
    <excludedAttrs>
        <attr>defaultValue</attr>
        <attr>defaultValueComputed</attr>
    </excludedAttrs>
</rule>
```

Will check that uppercase characters are not present in the changeSet attributes at all, except for excluded attributes. Attributes `defaultValue` and `defaultValueComputed` will be ignored.

---

### no-lowercase-in-attributes

Checks that lowercase characters are not present in the changeSet attributes at all.

Example:

```xml
<rule name="no-lowercase-in-attributes">
    <excludedAttrs>
        <attr>defaultValue</attr>
        <attr>defaultValueComputed</attr>
    </excludedAttrs>
</rule>
```

Will check that lowercase characters are not present in the changeSet attributes at all, except for excluded attributes. Attributes `defaultValue` and `defaultValueComputed` will be ignored.

---

## Exclusions

You can always add an exclusion to the set of rules. Create a separate `exclusions.xml` (or give it another name).

Example:

To exclude single or all checks the whole changeLog file or a single changeSet, use:
```xml
<fileExclusion fileName="changelog_01.xml" 
               rule="no-underscores-in-attributes"/>
<fileExclusion fileName="changelog_02.xml" 
               rule="*"/>
<changeSetExclusion fileName="changelog_03.xml" 
                    changeSetId="changelog_04-1" changeSetAuthor="test" 
                    rule="tag-must-exist"/>
```

---

## Note: requires Java 11 or later
### Supported changeLog formats: XML, YAML/YML

---

License
[Apache 2.0 License.](https://github.com/htshame/naming-convention-liquibase-maven-plugin/blob/main/LICENSE)

[Link](https://github.com/htshame/naming-convention-liquibase-maven-plugin) to the code repository.

Version [Changelog](https://github.com/htshame/naming-convention-liquibase-maven-plugin/blob/main/CHANGELOG.md).
