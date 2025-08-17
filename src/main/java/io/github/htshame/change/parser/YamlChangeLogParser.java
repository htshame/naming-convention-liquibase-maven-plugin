package io.github.htshame.change.parser;

import io.github.htshame.change.element.ChangeLogElement;
import io.github.htshame.change.element.YamlChangeLogElement;
import io.github.htshame.exception.ChangeLogParseException;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static io.github.htshame.util.ChangeSetUtil.CHANGE_SET_TAG_NAME;
import static io.github.htshame.util.ChangeSetUtil.DATABASE_CHANGELOG_NAME;

/**
 * YAML changeLog parser.
 */
public class YamlChangeLogParser implements ChangeLogParser {

    private static final String PLACEHOLDER_ELEMENT = "item";

    /**
     * Default constructor.
     */
    public YamlChangeLogParser() {

    }

    /**
     * Parse changeLog.
     *
     * @param changeLogFile - changeLog file.
     * @return list of changeSets.
     * @throws ChangeLogParseException - thrown if parsing fails.
     */
    @Override
    public List<ChangeLogElement> parseChangeSets(final File changeLogFile) throws ChangeLogParseException {
        try {
            Yaml yaml = new Yaml();
            Object loaded = yaml.load(new FileInputStream(changeLogFile));

            if (!(loaded instanceof Map<?, ?>)) {
                throw new IllegalArgumentException("Invalid YAML structure: Expected a top-level map");
            }
            Map<?, ?> rootMap = (Map<?, ?>) loaded;

            Object dbChangeLog = rootMap.get(DATABASE_CHANGELOG_NAME);
            if (!(dbChangeLog instanceof List<?>)) {
                throw new IllegalArgumentException("Missing 'databaseChangeLog'");
            }
            List<?> changeLogEntries = (List<?>) dbChangeLog;

            List<ChangeLogElement> changeSetElements = new ArrayList<>();

            for (Object changeLogEntry : changeLogEntries) {
                if (!(changeLogEntry instanceof Map<?, ?>)) {
                    continue;
                }
                Map<?, ?> entryMap = (Map<?, ?>) changeLogEntry;

                for (Map.Entry<?, ?> changeSetEntry : entryMap.entrySet()) {
                    String key = changeSetEntry.getKey().toString();
                    if (!CHANGE_SET_TAG_NAME.equals(key)) {
                        continue;
                    }

                    ChangeLogElement element = buildChangeSetElement(CHANGE_SET_TAG_NAME, changeSetEntry.getValue());
                    changeSetElements.add(element);
                }
            }

            return changeSetElements;
        } catch (Exception e) {
            throw new ChangeLogParseException(changeLogFile.getName(), e);
        }
    }

    private ChangeLogElement buildChangeSetElement(final String name,
                                                   final Object node) {
        if (node instanceof Map<?, ?>) {
            Map<?, ?> map = (Map<?, ?>) node;
            Map<String, String> properties = new LinkedHashMap<>();
            List<ChangeLogElement> children = new ArrayList<>();

            for (Map.Entry<?, ?> entry : map.entrySet()) {
                String key = entry.getKey().toString();
                Object value = entry.getValue();

                if (value instanceof Map || value instanceof List) {
                    children.add(buildChangeSetElement(key, value));
                } else {
                    properties.put(key, value != null ? value.toString() : null);
                }
            }

            return new YamlChangeLogElement(name, properties, children, null);

        } else if (node instanceof List<?>) {
            List<?> list = (List<?>) node;
            List<ChangeLogElement> children = new ArrayList<>();

            for (Object item : list) {
                if (item instanceof Map<?, ?> && ((Map<?, ?>) item).size() == 1) {
                    Map<?, ?> itemMap = (Map<?, ?>) item;
                    String childName = itemMap.keySet().iterator().next().toString();
                    Object childVal = itemMap.values().iterator().next();
                    children.add(buildChangeSetElement(childName, childVal));
                } else {
                    children.add(buildChangeSetElement(PLACEHOLDER_ELEMENT, item));
                }
            }
            return new YamlChangeLogElement(name, null, children, null);
        }
        return new YamlChangeLogElement(name, null, null, node != null ? node.toString() : null);
    }

    /**
     * Parse non-changeSet elements of changeLog.
     *
     * @param changeLogFile - changeLog file.
     * @return list of non-changeSet elements.
     * @throws ChangeLogParseException - if parsing goes wrong.
     */
    public List<ChangeLogElement> parseNonChangeSets(final File changeLogFile) throws ChangeLogParseException {
        try {
            Yaml yaml = new Yaml();
            Object loaded = yaml.load(new FileInputStream(changeLogFile));

            if (!(loaded instanceof Map<?, ?>)) {
                throw new IllegalArgumentException("Invalid YAML structure: Expected a top-level map");
            }
            Map<?, ?> rootMap = (Map<?, ?>) loaded;

            Object dbChangeLog = rootMap.get(DATABASE_CHANGELOG_NAME);
            if (!(dbChangeLog instanceof List<?>)) {
                throw new IllegalArgumentException("Missing 'databaseChangeLog'");
            }
            List<?> changeLogEntries = (List<?>) dbChangeLog;

            List<ChangeLogElement> nonChangeSetElements = new ArrayList<>();

            for (Object changeLogEntry : changeLogEntries) {
                if (!(changeLogEntry instanceof Map<?, ?>)) {
                    continue;
                }
                Map<?, ?> entryMap = (Map<?, ?>) changeLogEntry;

                for (Map.Entry<?, ?> entry : entryMap.entrySet()) {
                    String key = entry.getKey().toString();
                    if (CHANGE_SET_TAG_NAME.equals(key)) {
                        continue;
                    }

                    ChangeLogElement element = buildChangeSetElement(key, entry.getValue());
                    nonChangeSetElements.add(element);
                }
            }

            return nonChangeSetElements;
        } catch (Exception e) {
            throw new ChangeLogParseException(changeLogFile.getName(), e);
        }
    }
}
