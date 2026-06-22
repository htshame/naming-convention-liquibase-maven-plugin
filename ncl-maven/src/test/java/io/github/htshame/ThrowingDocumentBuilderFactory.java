package io.github.htshame;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * A {@link DocumentBuilderFactory} stub that always throws
 * {@link ParserConfigurationException} from {@link #newDocumentBuilder()}.
 * Used to test the parser-configuration-exception handling path in HelpMojo.
 */
public class ThrowingDocumentBuilderFactory extends DocumentBuilderFactory {

    @Override
    public DocumentBuilder newDocumentBuilder() throws ParserConfigurationException {
        throw new ParserConfigurationException("Simulated ParserConfigurationException");
    }

    @Override
    public void setAttribute(final String name, final Object value) {
    }

    @Override
    public Object getAttribute(final String name) {
        return null;
    }

    @Override
    public void setFeature(final String name, final boolean value) throws ParserConfigurationException {
    }

    @Override
    public boolean getFeature(final String name) throws ParserConfigurationException {
        return false;
    }
}
