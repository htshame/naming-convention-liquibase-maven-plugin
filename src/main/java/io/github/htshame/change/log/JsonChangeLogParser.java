package io.github.htshame.change.log;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.htshame.change.set.ChangeSetElement;
import io.github.htshame.change.set.JsonChangeSetElement;
import io.github.htshame.exception.ChangeLogParseException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.github.htshame.util.ChangeSetUtil.CHANGE_SET_TAG_NAME;
import static io.github.htshame.util.ChangeSetUtil.DATABASE_CHANGELOG_NAME;

/**
 * JSON changeLog parser.
 */
public class JsonChangeLogParser implements ChangeLogParser {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Default constructor.
     */
    public JsonChangeLogParser() {

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
            JsonNode root = objectMapper.readTree(changeLogFile);
            JsonNode changeLogArray = root.get(DATABASE_CHANGELOG_NAME);

            if (changeLogArray == null || !changeLogArray.isArray()) {
                throw new ChangeLogParseException(changeLogFile.getName(),
                        new IllegalStateException("Missing or invalid 'databaseChangeLog' array"));
            }

            List<ChangeSetElement> changeSets = new ArrayList<>();

            for (JsonNode entryNode : changeLogArray) {
                JsonNode changeSetNode = entryNode.get(CHANGE_SET_TAG_NAME);
                if (changeSetNode == null || !entryNode.isObject()) {
                    continue;
                }

                for (Map.Entry<String, JsonNode> field : entryNode.properties()) {
                    changeSets.add(new JsonChangeSetElement(field.getKey(), field.getValue()));
                }
            }

            return changeSets;

        } catch (IOException e) {
            throw new ChangeLogParseException(changeLogFile.getName(), e);
        }
    }
}
