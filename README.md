### You use [Liquibase](https://github.com/liquibase/liquibase)?

### You work with other developers whom you can't really control with PR reviews?

### Tired of inconsistent names of tables, indexes, foreign keys and other parts of the database schema?

Just use <b>naming-convention-liquibase-maven-plugin</b>!

This plugin allows you to create a set of rules and enforce them.

- If someone names the index 'customer_external_id_idx' instead of 'idx_customer_external_id' (or vice versa), the build
  will fail!

- If someone names the table 'customer-metadata' instead of 'customer_metadata' (or vice versa), the build will fail!

- If someone does not write <comment> to the changeSet, the build will fail!

- If someone... - well, you get the point!

---

# How to use it?

1. Create [rules.xml](https://github.com/htshame/naming-convention-liquibase-maven-plugin/blob/main/docs/schema/example/rules_example.xml) (or name it differently) file and provide it in `<pathToRulesFile>`.
2. Create [exclusions.xml](https://github.com/htshame/naming-convention-liquibase-maven-plugin/blob/main/docs/schema/example/exclusions_example.xml) (or name it differently) file (not mandatory) and provide it in `<pathToExclusionsFile>`.
3. Provide the path to the directory with Liquibase XML changeLogs in `<changeLogDirectory>`.
4. Put this into your pom.xml:
    ```xml
    
    <plugin>
        <groupId>com.github.htshame</groupId>
        <artifactId>naming-convention-liquibase-maven-plugin</artifactId>
        <version>1.0.0</version>
        <executions>
            <execution>
                <id>validate-xml</id>
                <phase>compile</phase>
                <goals>
                    <goal>validate-liquibase-xml</goal>
                </goals>
            </execution>
        </executions>
        <configuration>
            <pathToRulesFile>${project.basedir}/src/main/resources/liquibaseNaming/ruleset.xml</pathToRulesFile>
            <pathToExclusionsFile>
                ${project.basedir}/src/main/resources/liquibaseNaming/exclusions.xml
            </pathToExclusionsFile>
            <changeLogDirectory>${project.basedir}/src/main/resources/db</changeLogDirectory>
        </configuration>
    </plugin>
    ```

5. Run your build.

---

## Available rules:

### tag-must-exist

Checks that specified tag exists in every changeSet.

Example:

```xml

<rule name="tag-must-exist">
    <requiredTag>comment</requiredTag>
    <excludedAncestorTags>
        <tag>databaseChangeLog</tag>
        <tag>include</tag>
    </excludedAncestorTags>
</rule>
```

Will check that `<comment>` tag is present inside every tag, excluding `<databaseChangeLog>` and `<include>` tags.

### attr-starts-with

Checks that specified attribute starts with specified value.

Example:

```xml

<rule name="attr-starts-with">
    <tag>createIndex</tag>
    <targetAttribute>indexName</targetAttribute>
    <requiredPrefix>idx_</requiredPrefix>
</rule>
```

Will check that each `indexName` attribute of each `<createIndex>` tag starts with `idx_`.

### attr-ends-with

Checks that specified attribute ends with specified value.

Example:

```xml

<rule name="attr-ends-with">
    <tag>addForeignKeyConstraint</tag>
    <targetAttribute>constraintName</targetAttribute>
    <requiredSuffix>_fk</requiredSuffix>
</rule>
```

Will check that each `constraintName` attribute of each `<addForeignKeyConstraint>` tag ends with `__fk`.

### attr-ends-with-conditioned

Checks that specified attribute ends with specified value if the certain attribute is present and has certain value.

Example:

```xml

<rule name="attr-ends-with-conditioned">
    <tag>createIndex</tag>
    <conditionAttribute>unique</conditionAttribute>
    <conditionValue>true</conditionValue>
    <targetAttribute>indexName</targetAttribute>
    <requiredSuffix>_unique</requiredSuffix>
</rule>
```

Will check that each `indexName` attribute of each `<createIndex>` tag ends with `_unique` if attribute `unique="true"`
is present.

### no-hyphens-in-attributes

Example:

```xml

<rule name="no-hyphens-in-attributes"/>
```

Checks that `-` are not present in the changeLog at all. `<databaseChangeLog>`, `<comment>`, `<included>` tags are
excluded from the check.

### no-underscores-in-attributes

Example:

```xml

<rule name="no-underscores-in-attributes"/>
```

Checks that `_` are not present in the changeLog at all. `<databaseChangeLog>`, `<comment>`, `<included>` tags are
excluded from the check.

---

## Exclusions

You can always add an exclusion to the set of rules. Create a separate `exclusions.xml` (or give it another name).

Example:

```xml

<exclusion fileName="changelog_03.xml" rule="tag-must-exist"/>
```

License
[Apache 2.0 License.](https://github.com/htshame/naming-convention-liquibase-maven-plugin/blob/main/LICENSE)
