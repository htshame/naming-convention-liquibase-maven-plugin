package io.github.htshame.change.log;

import io.github.htshame.change.set.ChangeSetElement;
import io.github.htshame.change.set.XmlChangeSetElement;
import io.github.htshame.exception.ChangeLogParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static io.github.htshame.util.ChangeSetUtil.CHANGE_SET_TAG_NAME;

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
    public List<ChangeSetElement> parseChangeLog(final File changeLogFile) throws ChangeLogParseException {
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(changeLogFile);
            document.getDocumentElement().normalize();
            NodeList changeSetList = document.getElementsByTagName(CHANGE_SET_TAG_NAME);

            List<ChangeSetElement> changeSets = new ArrayList<>();
            for (int i = 0; i < changeSetList.getLength(); i++) {
                changeSets.add(new XmlChangeSetElement((Element) changeSetList.item(i)));
            }
            return changeSets;
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new ChangeLogParseException(changeLogFile.getName(), e);
        }
    }
}
