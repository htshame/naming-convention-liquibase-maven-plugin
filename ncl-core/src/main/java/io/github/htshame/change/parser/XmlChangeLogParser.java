package io.github.htshame.change.parser;

import io.github.htshame.change.element.ChangeLogElement;
import io.github.htshame.change.element.XmlChangeLogElement;
import io.github.htshame.exception.ChangeLogParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static io.github.htshame.util.ChangeSetUtil.CHANGE_SET_TAG_NAME;
import static io.github.htshame.util.XmlUtil.newXmlDocumentBuilder;

/**
 * XML changeLog parser.
 */
public class XmlChangeLogParser implements ChangeLogParser {

    /**
     * Default constructor.
     */
    public XmlChangeLogParser() {

    }

    /**
     * ChangeLog file parser.
     *
     * @param changeLogFile - changeLog file.
     * @return list of changeSets.
     * @throws ChangeLogParseException - thrown if parsing fails.
     */
    @Override
    public List<ChangeLogElement> parseChangeSets(final File changeLogFile) throws ChangeLogParseException {
        try {
            Document document = newXmlDocumentBuilder().parse(changeLogFile);
            document.getDocumentElement().normalize();
            NodeList changeSetList = document.getElementsByTagName(CHANGE_SET_TAG_NAME);

            List<ChangeLogElement> changeSets = new ArrayList<>();
            for (int i = 0; i < changeSetList.getLength(); i++) {
                changeSets.add(new XmlChangeLogElement((Element) changeSetList.item(i)));
            }
            return changeSets;
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new ChangeLogParseException(changeLogFile.getName(), e);
        }
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
            Document document = newXmlDocumentBuilder().parse(changeLogFile);
            document.getDocumentElement().normalize();

            NodeList allNodes = document.getDocumentElement().getChildNodes();
            List<ChangeLogElement> nonChangeSets = new ArrayList<>();

            for (int i = 0; i < allNodes.getLength(); i++) {
                Node node = allNodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE
                        && !CHANGE_SET_TAG_NAME.equals(node.getNodeName())) {
                    nonChangeSets.add(new XmlChangeLogElement((Element) node));
                }
            }
            return nonChangeSets;
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new ChangeLogParseException(changeLogFile.getName(), e);
        }
    }
}
