package com.github.htshame;

import com.github.htshame.config.ExclusionConfig;
import com.github.htshame.rules.AttrNotEndsWithRule;
import com.github.htshame.rules.AttrNotStartsWithRule;
import com.github.htshame.rules.NoHyphensInAttributesRule;
import com.github.htshame.rules.Rule;
import com.github.htshame.rules.TagMustExistRule;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.htshame.enums.RuleEnum.ATTRIBUTE_NOT_ENDS_WITH;
import static com.github.htshame.enums.RuleEnum.ATTRIBUTE_NOT_STARTS_WITH;
import static com.github.htshame.enums.RuleEnum.NO_HYPHENS_IN_ATTRIBUTES;
import static com.github.htshame.enums.RuleEnum.TAG_MUST_EXIST;
import static com.github.htshame.rules.Rule.GLOBALLY_EXCLUDED_TAGS;

@Mojo(name = "validate-liquibase-xml", defaultPhase = LifecyclePhase.COMPILE)
public class XmlValidatorMojo extends AbstractMojo {

    private static final String XML_EXTENSION = ".xml";

    @Parameter(required = true)
    private File ruleSetPath;

    @Parameter(required = true)
    private File exclusions;

    @Parameter(required = true)
    private File filesToValidatePath;

    /**
     * Starting plugin execution.
     *
     * @throws MojoExecutionException - thrown if execution fails.
     */
    public void execute() throws MojoExecutionException {
        if (!filesToValidatePath.isDirectory()) {
            throw new MojoExecutionException("Invalid path: " + filesToValidatePath);
        }

        Set<Rule> rules = loadRules(ruleSetPath);

        ExclusionConfig exclusionConfig;

        try {
            exclusionConfig = ExclusionConfig.fromFile(exclusions);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        List<File> xmlFiles;
        try (Stream<Path> paths = Files.walk(filesToValidatePath.toPath())) {
            xmlFiles = paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(XML_EXTENSION))
                    .map(Path::toFile)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new MojoExecutionException("Failed to walk directory: " + filesToValidatePath, e);
        }

        List<String> allViolations = new ArrayList<>();

        for (File xmlFile : xmlFiles) {
            Set<Rule> rulesToValidateWith = new HashSet<>();
            for (Rule rule : rules) {
                if (!exclusionConfig.isExcluded(xmlFile.getName(), rule.getName())) {
                    rulesToValidateWith.add(rule);
                }
            }

            getLog().info("Validating: " + xmlFile.getPath());
            allViolations.addAll(validate(xmlFile, rulesToValidateWith));
        }

        if (!allViolations.isEmpty()) {
            getLog().error("====== Liquibase changeset validation failed ======");
            for (String v : allViolations) {
                getLog().error(v);
            }
            throw new MojoExecutionException("Validation failed: " + allViolations.size() + " violation(s) found.");
        } else {
            getLog().info("All XML files passed validation.");
        }
    }


    private Set<Rule> loadRules(File rulesetFile) throws MojoExecutionException {
        Set<Rule> rules = new HashSet<>();
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(rulesetFile);
            NodeList ruleNodes = doc.getElementsByTagName("rule");
            for (int i = 0; i < ruleNodes.getLength(); i++) {
                Element ruleElem = (Element) ruleNodes.item(i);
                String type = ruleElem.getAttribute("type");

                if (TAG_MUST_EXIST.getValue().equalsIgnoreCase(type)) {
                    String requiredChild = getText(ruleElem, "requiredTag");
                    Set<String> excludedParents = new HashSet<>();
                    NodeList excludedTagElements = ((Element) ruleElem.getElementsByTagName("excludedAncestorTags").item(0)).getElementsByTagName("tag");
                    for (int j = 0; j < excludedTagElements.getLength(); j++) {
                        excludedParents.add(excludedTagElements.item(j).getTextContent());
                    }
                    rules.add(new TagMustExistRule(requiredChild, excludedParents));
                } else if (ATTRIBUTE_NOT_STARTS_WITH.getValue().equalsIgnoreCase(type)) {
                    rules.add(new AttrNotStartsWithRule(
                            getText(ruleElem, "tag"),
                            getText(ruleElem, "targetAttribute"),
                            getText(ruleElem, "requiredPrefix")
                    ));
                } else if (ATTRIBUTE_NOT_ENDS_WITH.getValue().equalsIgnoreCase(type)) {
                    rules.add(new AttrNotEndsWithRule(
                            getText(ruleElem, "tag"),
                            getText(ruleElem, "conditionAttribute"),
                            getText(ruleElem, "conditionValue"),
                            getText(ruleElem, "targetAttribute"),
                            getText(ruleElem, "requiredSuffix")));
                } else if (NO_HYPHENS_IN_ATTRIBUTES.getValue().equalsIgnoreCase(type)) {
                    rules.add(new NoHyphensInAttributesRule());
                } else {
                    throw new MojoExecutionException("Unknown rule type: " + type);
                }
            }
        } catch (Exception e) {
            throw new MojoExecutionException("Error parsing ruleset", e);
        }
        return rules;
    }

    private String getText(Element parent, String tagName) {
        NodeList nodes = parent.getElementsByTagName(tagName);
        return nodes.getLength() > 0 ? nodes.item(0).getTextContent().trim() : null;
    }

    private Set<String> loadExclusions(File exclusionFile) throws MojoExecutionException {
        Set<String> excluded = new HashSet<>();
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(exclusionFile);
            NodeList files = doc.getElementsByTagName("file");
            for (int i = 0; i < files.getLength(); i++) {
                excluded.add(files.item(i).getTextContent().trim());
            }
        } catch (Exception e) {
            throw new MojoExecutionException("Failed to parse exclusions file", e);
        }
        return excluded;
    }

    private List<String> validate(File xmlFile, Set<Rule> rules) {
        List<String> violations = new ArrayList<>();
        Document doc;
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();
        } catch (Exception e) {
            violations.add("[" + xmlFile.getName() + "] Failed to parse: " + e.getMessage());
            return violations;
        }

        stripExcludedTagAttributes(doc);

        for (Rule rule : rules) {
            try {
                rule.validate(doc, xmlFile);
            } catch (MojoExecutionException e) {
                violations.add("[" + xmlFile.getName() + "] " + e.getMessage());
            }
        }

        return violations;
    }

    private void stripExcludedTagAttributes(Document doc) {
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

}
