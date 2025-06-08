package com.github.htshame.processor;

import com.github.htshame.exception.ValidationException;
import com.github.htshame.parser.ExclusionParser;
import com.github.htshame.rules.Rule;
import org.apache.maven.plugin.MojoExecutionException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.github.htshame.rules.Rule.GLOBALLY_EXCLUDED_TAGS;

public class ValidationProcessor {

    public List<String> validate(List<File> changeLogFiles, Set<Rule> rules, ExclusionParser exclusionParser) {
        List<String> violations = new ArrayList<>();
        for (File changeLogFile : changeLogFiles) {
            Set<Rule> rulesToValidateWith = excludeRulesBasedOnExclusionFile(rules, exclusionParser, changeLogFile);
            violations.addAll(processValidation(changeLogFile, rulesToValidateWith));
        }
        return violations;
    }

    private List<String> processValidation(File changeLogFile, Set<Rule> rules) {
        List<String> violations = new ArrayList<>();
        Document doc;
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            doc = builder.parse(changeLogFile);
            doc.getDocumentElement().normalize();
        } catch (Exception e) {
            violations.add("[" + changeLogFile.getName() + "] Failed to parse: " + e.getMessage());
            return violations;
        }

        stripExcludedTagAttributes(doc);

        for (Rule rule : rules) {
            try {
                rule.validate(doc, changeLogFile);
            } catch (ValidationException e) {
                violations.add("[" + changeLogFile.getName() + "] " + e.getMessage());
            }
        }

        return violations;
    }

    private static void stripExcludedTagAttributes(Document doc) {
        for (String tag : GLOBALLY_EXCLUDED_TAGS) {
            NodeList elements = doc.getElementsByTagName(tag);
            for (int i = 0; i < elements.getLength(); i++) {
                Element elem = (Element) elements.item(i);
                NamedNodeMap attributes = elem.getAttributes();
                while (attributes.getLength() > 0) {
                    attributes.removeNamedItem(attributes.item(0).getNodeName());
                }
            }
        }
    }

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
