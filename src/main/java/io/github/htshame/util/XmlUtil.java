package io.github.htshame.util;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Utility class for XML processing.
 */
public final class XmlUtil {

    /**
     * Document builder factory.
     */
    private static final DocumentBuilderFactory DOCUMENT_BUILDER_FACTORY = createFactory();

    /**
     * Private constructor.
     */
    private XmlUtil() {

    }

    /**
     * Create XML document builder factory.
     *
     * @return document builder factory.
     */
    private static DocumentBuilderFactory createFactory() {
        return DocumentBuilderFactory.newInstance();
    }

    /**
     * Create new XML document builder.
     *
     * @return document builder.
     * @throws ParserConfigurationException - thrown if there's a configuration error.
     */
    public static DocumentBuilder newXmlDocumentBuilder() throws ParserConfigurationException {
        return DOCUMENT_BUILDER_FACTORY.newDocumentBuilder();
    }
}

