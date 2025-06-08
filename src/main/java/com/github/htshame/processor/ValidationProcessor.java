package com.github.htshame.processor;

import com.github.htshame.exception.ValidationException;
import com.github.htshame.parser.ExclusionParser;
import com.github.htshame.rules.Rule;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class is responsible for actual validation based on
 * provided rules XML file,
 * exclusion XML file
 * and contents of the changeLog directory.
 */
public class ValidationProcessor {

    private static final List<String> GLOBALLY_EXCLUDED_TAGS =
            Arrays.asList("databaseChangeLog", "comment", "include");

    /**
     * Commence validation.
     *
     * @param changeLogFiles - changeLog files to validate.
     * @param rules - set of rules to validate against.
     * @param exclusionParser - exclusions.
     * @return list of validation errors. Empty list if there are no errors.
     */
    public List<String> validate(final List<File> changeLogFiles,
                                 final Set<Rule> rules,
                                 final ExclusionParser exclusionParser) {
        List<String> validationErrors = new ArrayList<>();
        for (File changeLogFile : changeLogFiles) {
            Set<Rule> rulesToValidateWith = excludeRulesBasedOnExclusionFile(rules, exclusionParser, changeLogFile);
            validationErrors.addAll(processValidation(changeLogFile, rulesToValidateWith));
        }
        return validationErrors;
    }

    /**
     * Process a single changeLog file.
     *
     * @param changeLogFile - changeLog file.
     * @param rules - set of rules.
     * @return list of validation errors. Empty list of there are no errors.
     */
    private List<String> processValidation(final File changeLogFile,
                                           final Set<Rule> rules) {
        List<String> validationErrors = new ArrayList<>();
        Document doc;
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            doc = builder.parse(changeLogFile);
            doc.getDocumentElement().normalize();
        } catch (Exception e) {
            validationErrors.add("[" + changeLogFile.getName() + "] Failed to parse: " + e.getMessage());
            return validationErrors;
        }

        stripExcludedTagAttributes(doc);

        for (Rule rule : rules) {
            try {
                rule.validate(doc, changeLogFile);
            } catch (ValidationException e) {
                validationErrors.add("[" + changeLogFile.getName() + "] " + e.getMessage());
            }
        }

        return validationErrors;
    }

    /**
     * Strip globally excluded tags from the validation set.
     *
     * @param document - changeLog document.
     */
    private static void stripExcludedTagAttributes(final Document document) {
        for (String tag : GLOBALLY_EXCLUDED_TAGS) {
            NodeList elements = document.getElementsByTagName(tag);
            for (int i = 0; i < elements.getLength(); i++) {
                Element elem = (Element) elements.item(i);
                NamedNodeMap attributes = elem.getAttributes();
                while (attributes.getLength() > 0) {
                    attributes.removeNamedItem(attributes.item(0).getNodeName());
                }
            }
        }
    }

    /**
     * Exclude rules based on the data from the exclusion file.
     *
     * @param rules - set of specified rules.
     * @param exclusionParser - exclusion parser.
     * @param changeLogFile - changeLog file.
     * @return set of rules to apply to the given changeLog file.
     */
    private Set<Rule> excludeRulesBasedOnExclusionFile(Set<Rule> rules,
                                                       ExclusionParser exclusionParser,
                                                       File changeLogFile) {
        Set<Rule> rulesToValidateWith = new HashSet<>();
        for (Rule rule : rules) {
            if (!exclusionParser.isExcluded(changeLogFile.getName(), rule.getName())) {
                rulesToValidateWith.add(rule);
            }
        }
        return rulesToValidateWith;
    }
}
