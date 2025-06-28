package io.github.htshame.change.log;

import io.github.htshame.change.set.ChangeSetElement;
import io.github.htshame.change.set.YamlChangeSetElement;
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
    public List<ChangeSetElement> parseChangeLog(final File changeLogFile) throws ChangeLogParseException {
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

            List<ChangeSetElement> changeSetElements = new ArrayList<>();

            for (Object entry : changeLogEntries) {
                if (!(entry instanceof Map<?, ?>)) {
                    continue;
                }
                Map<?, ?> entryMap = (Map<?, ?>) entry;

                for (Map.Entry<?, ?> changeSetWrapper : entryMap.entrySet()) {
                    String key = changeSetWrapper.getKey().toString();
                    if (!CHANGE_SET_TAG_NAME.equals(key)) {
                        continue;
                    }

                    Object changeSetBody = changeSetWrapper.getValue();
                    ChangeSetElement element = buildElement(CHANGE_SET_TAG_NAME, changeSetBody);
                    changeSetElements.add(element);
                }
            }

            return changeSetElements;
        } catch (Exception e) {
            throw new ChangeLogParseException(changeLogFile.getName(), e);
        }
    }

    private ChangeSetElement buildElement(final String name,
                                          final Object node) {
        if (node instanceof Map<?, ?>) {
            Map<?, ?> map = (Map<?, ?>) node;
            Map<String, String> properties = new LinkedHashMap<>();
            List<ChangeSetElement> children = new ArrayList<>();

            for (Map.Entry<?, ?> entry : map.entrySet()) {
                String key = entry.getKey().toString();
                Object val = entry.getValue();

                if (val instanceof Map || val instanceof List) {
                    children.add(buildElement(key, val));
                } else {
                    properties.put(key, val != null ? val.toString() : null);
                }
            }

            return new YamlChangeSetElement(name, properties, children, null);

        } else if (node instanceof List<?>) {
            List<?> list = (List<?>) node;
            List<ChangeSetElement> children = new ArrayList<>();

            for (Object item : list) {
                if (item instanceof Map<?, ?> && ((Map<?, ?>) item).size() == 1) {
                    Map<?, ?> itemMap = (Map<?, ?>) item;
                    String childName = itemMap.keySet().iterator().next().toString();
                    Object childVal = itemMap.values().iterator().next();
                    children.add(buildElement(childName, childVal));
                } else {
                    children.add(buildElement("item", item));
                }
            }
            return new YamlChangeSetElement(name, null, children, null);
        } else {
            return new YamlChangeSetElement(name, null, null, node != null ? node.toString() : null);
        }
    }
}
