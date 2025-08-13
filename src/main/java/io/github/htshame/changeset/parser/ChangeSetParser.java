package io.github.htshame.changeset.parser;

import io.github.htshame.changeset.element.ChangeSetElement;
import io.github.htshame.exception.ChangeLogParseException;

import java.io.File;
import java.util.List;

/**
 * ChangeLog parser interface.
 */
public interface ChangeSetParser {

    /**
     * ChangeLog file parser.
     *
     * @param changeLogFile - changeLog file.
     * @return list of changeSets.
     * @throws ChangeLogParseException - thrown if parsing fails.
     */
    List<ChangeSetElement> parseChangeSets(File changeLogFile) throws ChangeLogParseException;
}
