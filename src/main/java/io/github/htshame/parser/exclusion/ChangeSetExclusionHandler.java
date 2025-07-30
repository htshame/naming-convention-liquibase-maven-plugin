package io.github.htshame.parser.exclusion;

import io.github.htshame.dto.ChangeSetExclusionDto;
import io.github.htshame.enums.ExclusionStructureEnum;
import io.github.htshame.enums.RuleEnum;
import io.github.htshame.parser.ExclusionParser;
import org.w3c.dom.Element;

import java.util.HashSet;

/**
 * ChangeSet exclusion handler.
 */
public class ChangeSetExclusionHandler implements ExclusionHandler {

    /**
     * Default constructor.
     */
    public ChangeSetExclusionHandler() {

    }

    /**
     * Handle changeSet exclusion parsing.
     *
     * @param element - element.
     * @param parser  - exclusion parser.
     */
    @Override
    public void handle(final Element element,
                       final ExclusionParser parser) {
        String fileName = element.getAttribute(ExclusionStructureEnum.FILE_NAME_ATTR.getValue()).trim();
        String rule = element.getAttribute(ExclusionStructureEnum.RULE_ATTR.getValue()).trim();
        String changeSetId = element.getAttribute(ExclusionStructureEnum.CHANGESET_ID_ATTR.getValue()).trim();
        String changeSetAuthor = element.getAttribute(ExclusionStructureEnum.CHANGESET_AUTHOR_ATTR.getValue()).trim();

        ChangeSetExclusionDto changeSetExclusionDto =
                new ChangeSetExclusionDto(fileName, changeSetId, changeSetAuthor);
        parser.getChangeSetRuleExclusions()
                .computeIfAbsent(changeSetExclusionDto, key -> new HashSet<>())
                .add(RuleEnum.fromValue(rule));
    }
}
