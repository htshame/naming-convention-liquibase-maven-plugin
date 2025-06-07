package com.github.htshame.config;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ExclusionConfig {
    private final Map<String, Set<String>> fileRuleExclusions = new HashMap<>();

    public static ExclusionConfig fromFile(File file) throws Exception {
        ExclusionConfig config = new ExclusionConfig();

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(file);

        NodeList exclusions = doc.getElementsByTagName("exclusion");
        for (int i = 0; i < exclusions.getLength(); i++) {
            Element exclusion = (Element) exclusions.item(i);
            String fileName = exclusion.getAttribute("fileName").trim();
            String rule = exclusion.getAttribute("rule").trim();

            config.fileRuleExclusions
                    .computeIfAbsent(fileName, k -> new HashSet<>())
                    .add(rule);
        }

        return config;
    }

    public boolean isExcluded(String fileName, String ruleName) {
        Set<String> excludedRules = fileRuleExclusions.get(fileName);
        return excludedRules != null && excludedRules.contains(ruleName);
    }
}
