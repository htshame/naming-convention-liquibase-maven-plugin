package io.github.htshame.change.log;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.github.htshame.change.set.ChangeSetElement;
import io.github.htshame.change.set.YamlChangeSetElement;
import io.github.htshame.exception.ChangeLogParseException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
            JsonNode listNode = new ObjectMapper(new YAMLFactory())
                    .readTree(changeLogFile)
                    .get(DATABASE_CHANGELOG_NAME);

            List<ChangeSetElement> changeSetElements = new ArrayList<>();
            if (listNode == null || !listNode.isArray()) {
                return changeSetElements;
            }
            for (JsonNode itemNode : listNode) {
                JsonNode myCustomNameTagNode = itemNode.get(CHANGE_SET_TAG_NAME);
                if (myCustomNameTagNode != null) {
                    changeSetElements.add(new YamlChangeSetElement(myCustomNameTagNode));
                }
            }
            return changeSetElements;
        } catch (IOException e) {
            throw new ChangeLogParseException("Error parsing file: " + changeLogFile.getName(), e);
        }
    }
}
