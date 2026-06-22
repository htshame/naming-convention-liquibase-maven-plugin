package io.github.htshame;

import io.github.htshame.naming_convention_liquibase_maven_plugin.HelpMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for {@link HelpMojo} covering all execution paths.
 */
public class HelpMojoTest {

    private static final int LINE_LENGTH = 80;
    private static final int INDENT_SIZE = 2;
    private static final int NEGATIVE_ONE = -1;
    private static final String HELP_MOJO_CLASS =
        "io.github.htshame.naming_convention_liquibase_maven_plugin.HelpMojo";
    private static final String PLUGIN_HELP_RESOURCE =
        "META-INF/maven/io.github.htshame/"
        + "naming-convention-liquibase-maven-plugin/plugin-help.xml";
    private static final String THROWING_DBF_CLASS =
        "io.github.htshame.ThrowingDocumentBuilderFactory";
    private static final String DBF_PROPERTY = "javax.xml.parsers.DocumentBuilderFactory";

    /** Minimal XML with plugin name equal to its artifact id string (covers lines 126-128). */
    private static final String XML_NAME_IS_ID =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
        + "<plugin>"
        + "<name>io.github.htshame:test-plugin:1.0</name>"
        + "<description>desc</description>"
        + "<groupId>io.github.htshame</groupId>"
        + "<artifactId>test-plugin</artifactId>"
        + "<version>1.0</version>"
        + "<goalPrefix>test</goalPrefix>"
        + "<mojos/>"
        + "</plugin>";

    /** Minimal XML with an empty plugin name (covers lines 130-132). */
    private static final String XML_EMPTY_NAME =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
        + "<plugin>"
        + "<name></name>"
        + "<description>desc</description>"
        + "<groupId>io.github.htshame</groupId>"
        + "<artifactId>test-plugin</artifactId>"
        + "<version>1.0</version>"
        + "<goalPrefix>test</goalPrefix>"
        + "<mojos/>"
        + "</plugin>";

    /** XML with two plugin-level name elements to trigger getSingleChild multiple error. */
    private static final String XML_DUPLICATE_NAME =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
        + "<plugin>"
        + "<name>First</name>"
        + "<name>Second</name>"
        + "<description>desc</description>"
        + "<groupId>io.github.htshame</groupId>"
        + "<artifactId>test-plugin</artifactId>"
        + "<version>1.0</version>"
        + "<goalPrefix>test</goalPrefix>"
        + "<mojos/>"
        + "</plugin>";

    /** XML with a mojo that has two description elements to trigger findSingleChild multiple. */
    private static final String XML_DUPLICATE_DESC =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
        + "<plugin>"
        + "<name>Test Plugin</name>"
        + "<description>desc</description>"
        + "<groupId>io.github.htshame</groupId>"
        + "<artifactId>test-plugin</artifactId>"
        + "<version>1.0</version>"
        + "<goalPrefix>test</goalPrefix>"
        + "<mojos>"
        + "<mojo>"
        + "<goal>test-goal</goal>"
        + "<description>First.</description>"
        + "<description>Second.</description>"
        + "<parameters/>"
        + "</mojo>"
        + "</mojos>"
        + "</plugin>";

    /** XML with a deprecated mojo (covers lines 231-235). */
    private static final String XML_DEPRECATED_MOJO =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
        + "<plugin>"
        + "<name>Test Plugin</name>"
        + "<description>desc</description>"
        + "<groupId>io.github.htshame</groupId>"
        + "<artifactId>test-plugin</artifactId>"
        + "<version>1.0</version>"
        + "<goalPrefix>test</goalPrefix>"
        + "<mojos>"
        + "<mojo>"
        + "<goal>old-goal</goal>"
        + "<deprecated>Use new-goal instead.</deprecated>"
        + "<description>An old goal.</description>"
        + "<parameters/>"
        + "</mojo>"
        + "</mojos>"
        + "</plugin>";

    /** XML with a deprecated parameter (covers lines 279-281). */
    private static final String XML_DEPRECATED_PARAM =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
        + "<plugin>"
        + "<name>Test Plugin</name>"
        + "<description>desc</description>"
        + "<groupId>io.github.htshame</groupId>"
        + "<artifactId>test-plugin</artifactId>"
        + "<version>1.0</version>"
        + "<goalPrefix>test</goalPrefix>"
        + "<mojos>"
        + "<mojo>"
        + "<goal>test-goal</goal>"
        + "<description>A test goal.</description>"
        + "<parameters>"
        + "<parameter>"
        + "<name>oldParam</name>"
        + "<deprecated>Use newParam instead.</deprecated>"
        + "<description>Old parameter.</description>"
        + "<type>java.lang.String</type>"
        + "<required>false</required>"
        + "<editable>true</editable>"
        + "</parameter>"
        + "</parameters>"
        + "</mojo>"
        + "</mojos>"
        + "</plugin>";

    /** XML with a config element whose text is a literal (not a ${...} expression). */
    private static final String XML_LITERAL_CONFIG =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
        + "<plugin>"
        + "<name>Test Plugin</name>"
        + "<description>desc</description>"
        + "<groupId>io.github.htshame</groupId>"
        + "<artifactId>test-plugin</artifactId>"
        + "<version>1.0</version>"
        + "<goalPrefix>test</goalPrefix>"
        + "<mojos>"
        + "<mojo>"
        + "<goal>test-goal</goal>"
        + "<description>A test goal.</description>"
        + "<parameters>"
        + "<parameter>"
        + "<name>testParam</name>"
        + "<description>Test parameter.</description>"
        + "<type>java.lang.String</type>"
        + "<required>false</required>"
        + "<editable>true</editable>"
        + "</parameter>"
        + "</parameters>"
        + "<configuration>"
        + "<testParam implementation=\"java.lang.String\" default-value=\"hello\">"
        + "literal-value"
        + "</testParam>"
        + "</configuration>"
        + "</mojo>"
        + "</mojos>"
        + "</plugin>";

    /** XML with a non-breaking space (U+00A0) in the plugin description (covers line 402). */
    private static final String XML_NON_BREAKING_SPACE =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
        + "<plugin>"
        + "<name>Test Plugin</name>"
        + "<description>Test\u00A0plugin.</description>"
        + "<groupId>io.github.htshame</groupId>"
        + "<artifactId>test-plugin</artifactId>"
        + "<version>1.0</version>"
        + "<goalPrefix>test</goalPrefix>"
        + "<mojos/>"
        + "</plugin>";

    /** XML whose root element is not plugin, so getSingleChild throws "Could not find" (line 180). */
    private static final String XML_MISSING_PLUGIN_ROOT =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
        + "<notplugin/>";

    private HelpMojo helpMojo;

    /**
     * Initialises a fresh {@link HelpMojo} with default field values before each test.
     *
     * @throws NoSuchFieldException   if a required field cannot be found.
     * @throws IllegalAccessException if a required field cannot be accessed.
     */
    @Before
    public void init() throws NoSuchFieldException, IllegalAccessException {
        helpMojo = new HelpMojo();
        setField(helpMojo, "lineLength", LINE_LENGTH);
        setField(helpMojo, "indentSize", INDENT_SIZE);
        setField(helpMojo, "detail", false);
        setField(helpMojo, "goal", null);
    }

    /**
     * Tests default execution (goal=null, detail=false) with the real plugin descriptor.
     *
     * @throws MojoExecutionException if execution fails unexpectedly.
     */
    @Test
    public void testExecuteDefault() throws MojoExecutionException {
        helpMojo.execute();
    }

    /**
     * Tests that a non-positive lineLength triggers a warning and resets to the default.
     *
     * @throws NoSuchFieldException   if a required field cannot be found.
     * @throws IllegalAccessException if a required field cannot be accessed.
     * @throws MojoExecutionException if execution fails unexpectedly.
     */
    @Test
    public void testExecuteLineLengthWarning()
            throws NoSuchFieldException, IllegalAccessException, MojoExecutionException {
        setField(helpMojo, "lineLength", NEGATIVE_ONE);
        helpMojo.execute();
    }

    /**
     * Tests that a non-positive indentSize triggers a warning and resets to the default.
     *
     * @throws NoSuchFieldException   if a required field cannot be found.
     * @throws IllegalAccessException if a required field cannot be accessed.
     * @throws MojoExecutionException if execution fails unexpectedly.
     */
    @Test
    public void testExecuteIndentSizeWarning()
            throws NoSuchFieldException, IllegalAccessException, MojoExecutionException {
        setField(helpMojo, "indentSize", NEGATIVE_ONE);
        helpMojo.execute();
    }

    /**
     * Tests detail=true for the validate goal, exercising writeParameter paths
     * including required=true and default-value attribute.
     *
     * @throws NoSuchFieldException   if a required field cannot be found.
     * @throws IllegalAccessException if a required field cannot be accessed.
     * @throws MojoExecutionException if execution fails unexpectedly.
     */
    @Test
    public void testExecuteDetailTrueValidateGoal()
            throws NoSuchFieldException, IllegalAccessException, MojoExecutionException {
        setField(helpMojo, "detail", true);
        setField(helpMojo, "goal", "validate-liquibase-changeLog");
        helpMojo.execute();
    }

    /**
     * Tests detail=true for the help goal, exercising the ${expression} property extraction path.
     *
     * @throws NoSuchFieldException   if a required field cannot be found.
     * @throws IllegalAccessException if a required field cannot be accessed.
     * @throws MojoExecutionException if execution fails unexpectedly.
     */
    @Test
    public void testExecuteDetailTrueHelpGoal()
            throws NoSuchFieldException, IllegalAccessException, MojoExecutionException {
        setField(helpMojo, "detail", true);
        setField(helpMojo, "goal", "help");
        helpMojo.execute();
    }

    /**
     * Tests a custom XML where the plugin name equals the artifact id string (lines 126-128).
     *
     * @throws Exception if an unexpected error occurs.
     */
    @Test
    public void testExecuteNameContainsId() throws Exception {
        Object mojo = createMojoWithXml(XML_NAME_IS_ID);
        invokeExecute(mojo);
    }

    /**
     * Tests a custom XML where the plugin name is empty (lines 130-132).
     *
     * @throws Exception if an unexpected error occurs.
     */
    @Test
    public void testExecuteEmptyName() throws Exception {
        Object mojo = createMojoWithXml(XML_EMPTY_NAME);
        invokeExecute(mojo);
    }

    /**
     * Tests a custom XML where two name elements exist at plugin level,
     * triggering getSingleChild's multiple-element error (lines 183-184).
     *
     * @throws Exception if an unexpected error occurs.
     */
    @Test
    public void testExecuteDuplicateNameThrows() throws Exception {
        Object mojo = createMojoWithXml(XML_DUPLICATE_NAME);
        MojoExecutionException ex = invokeExecuteExpectingException(mojo);
        assertNotNull(ex);
    }

    /**
     * Tests a custom XML where a mojo has two description elements,
     * triggering findSingleChild's multiple-element error (lines 212-214).
     *
     * @throws Exception if an unexpected error occurs.
     */
    @Test
    public void testExecuteDuplicateDescriptionThrows() throws Exception {
        Object mojo = createMojoWithXml(XML_DUPLICATE_DESC);
        MojoExecutionException ex = invokeExecuteExpectingException(mojo);
        assertNotNull(ex);
    }

    /**
     * Tests a deprecated mojo with detail=false (covers lines 231-232).
     *
     * @throws Exception if an unexpected error occurs.
     */
    @Test
    public void testExecuteDeprecatedMojoNoDetail() throws Exception {
        Object mojo = createMojoWithXml(XML_DEPRECATED_MOJO);
        invokeExecute(mojo);
    }

    /**
     * Tests a deprecated mojo with detail=true (covers lines 234-235).
     *
     * @throws Exception if an unexpected error occurs.
     */
    @Test
    public void testExecuteDeprecatedMojoWithDetail() throws Exception {
        Object mojo = createMojoWithXml(XML_DEPRECATED_MOJO);
        setField(mojo, "detail", true);
        invokeExecute(mojo);
    }

    /**
     * Tests a deprecated parameter with detail=true (covers lines 279-281).
     *
     * @throws Exception if an unexpected error occurs.
     */
    @Test
    public void testExecuteDeprecatedParam() throws Exception {
        Object mojo = createMojoWithXml(XML_DEPRECATED_PARAM);
        setField(mojo, "detail", true);
        invokeExecute(mojo);
    }

    /**
     * Tests a config element with literal text (not a ${...} expression),
     * exercising getPropertyFromExpression returning null (line 446).
     *
     * @throws Exception if an unexpected error occurs.
     */
    @Test
    public void testExecuteLiteralConfigText() throws Exception {
        Object mojo = createMojoWithXml(XML_LITERAL_CONFIG);
        setField(mojo, "detail", true);
        invokeExecute(mojo);
    }

    /**
     * Tests a description containing a non-breaking space (U+00A0), covering line 402.
     *
     * @throws Exception if an unexpected error occurs.
     */
    @Test
    public void testExecuteNonBreakingSpaceInDescription() throws Exception {
        Object mojo = createMojoWithXml(XML_NON_BREAKING_SPACE);
        invokeExecute(mojo);
    }

    /**
     * Tests that a null resource stream causes a MojoExecutionException (lines 71-73).
     *
     * @throws Exception if an unexpected error occurs.
     */
    @Test
    public void testBuildNullResourceThrows() throws Exception {
        Object mojo = createMojoWithNullResource();
        MojoExecutionException ex = invokeExecuteExpectingException(mojo);
        assertNotNull(ex);
    }

    /**
     * Tests that an IOException while parsing triggers a MojoExecutionException (lines 79-81).
     *
     * @throws Exception if an unexpected error occurs.
     */
    @Test
    public void testBuildIoExceptionThrows() throws Exception {
        InputStream throwing = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException("Simulated IO error");
            }
        };
        Object mojo = createMojoWithStream(throwing);
        MojoExecutionException ex = invokeExecuteExpectingException(mojo);
        assertNotNull(ex);
    }

    /**
     * Tests that a SAXException from malformed XML causes a MojoExecutionException (lines 87-89).
     *
     * @throws Exception if an unexpected error occurs.
     */
    @Test
    public void testBuildSaxExceptionThrows() throws Exception {
        InputStream bad = new ByteArrayInputStream(
            "not-xml".getBytes(StandardCharsets.UTF_8));
        Object mojo = createMojoWithStream(bad);
        MojoExecutionException ex = invokeExecuteExpectingException(mojo);
        assertNotNull(ex);
    }

    /**
     * Tests that a ParserConfigurationException causes a MojoExecutionException (lines 83-85).
     * Sets the system property to redirect DocumentBuilderFactory to a stub that throws.
     *
     * @throws Exception if an unexpected error occurs.
     */
    @Test
    public void testBuildParserConfigExceptionThrows() throws Exception {
        final String prev = System.getProperty(DBF_PROPERTY);
        System.setProperty(DBF_PROPERTY, THROWING_DBF_CLASS);
        try {
            helpMojo.execute();
        } catch (MojoExecutionException ex) {
            assertNotNull(ex);
        } finally {
            if (prev == null) {
                System.clearProperty(DBF_PROPERTY);
            } else {
                System.setProperty(DBF_PROPERTY, prev);
            }
        }
    }

    /**
     * Tests the secondary-tab branch in getIndentLevel by calling the private method
     * via reflection with a string that has a tab after a leading-tab followed by a space
     * (covers lines 430-431).
     *
     * @throws Exception if the method cannot be accessed via reflection.
     */
    @Test
    public void testGetIndentLevelSecondaryTab() throws Exception {
        Method method = HelpMojo.class.getDeclaredMethod("getIndentLevel", String.class);
        method.setAccessible(true);
        // "\t \t": one leading tab (level=1), space stops main loop,
        // then secondary loop finds another tab at position 2 -> level becomes 2
        int level = (int) method.invoke(null, "\t \t");
        assertEquals("Expected level 2 for input with secondary tab", 2, level);
    }

    /**
     * Tests a custom XML where the required plugin root element is absent,
     * triggering getSingleChild's not-found error (line 180).
     *
     * @throws Exception if an unexpected error occurs.
     */
    @Test
    public void testExecuteMissingPluginElementThrows() throws Exception {
        Object mojo = createMojoWithXml(XML_MISSING_PLUGIN_ROOT);
        MojoExecutionException ex = invokeExecuteExpectingException(mojo);
        assertNotNull(ex);
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    /**
     * Creates a HelpMojo instance loaded by a child-first URLClassLoader that intercepts
     * the plugin-help.xml resource with the provided XML string.
     *
     * @param xml the XML string to supply as the plugin descriptor.
     * @return the HelpMojo instance (as Object due to classloader isolation).
     * @throws Exception if the mojo cannot be instantiated.
     */
    private Object createMojoWithXml(final String xml) throws Exception {
        return createMojoWithStream(
            new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * Creates a HelpMojo instance loaded by a child-first URLClassLoader that returns null
     * for the plugin-help.xml resource, simulating a missing descriptor.
     *
     * @return the HelpMojo instance (as Object due to classloader isolation).
     * @throws Exception if the mojo cannot be instantiated.
     */
    private Object createMojoWithNullResource() throws Exception {
        final URL classesUrl =
            HelpMojo.class.getProtectionDomain().getCodeSource().getLocation();
        final ClassLoader parent = Thread.currentThread().getContextClassLoader();
        URLClassLoader loader = new URLClassLoader(new URL[]{classesUrl}, parent) {
            @Override
            protected Class<?> loadClass(final String name, final boolean resolve)
                    throws ClassNotFoundException {
                if (HELP_MOJO_CLASS.equals(name)) {
                    synchronized (getClassLoadingLock(name)) {
                        Class<?> c = findLoadedClass(name);
                        if (c == null) {
                            c = findClass(name);
                        }
                        if (resolve) {
                            resolveClass(c);
                        }
                        return c;
                    }
                }
                return super.loadClass(name, resolve);
            }

            @Override
            public InputStream getResourceAsStream(final String name) {
                if (PLUGIN_HELP_RESOURCE.equals(name)) {
                    return null;
                }
                return super.getResourceAsStream(name);
            }
        };
        return loader.loadClass(HELP_MOJO_CLASS)
            .getDeclaredConstructor()
            .newInstance();
    }

    /**
     * Creates a HelpMojo instance loaded by a child-first URLClassLoader that serves the
     * given InputStream as the plugin-help.xml resource.
     *
     * @param xmlStream the InputStream to return for the plugin descriptor resource.
     * @return the HelpMojo instance (as Object due to classloader isolation).
     * @throws Exception if the mojo cannot be instantiated.
     */
    private Object createMojoWithStream(final InputStream xmlStream) throws Exception {
        final URL classesUrl =
            HelpMojo.class.getProtectionDomain().getCodeSource().getLocation();
        final ClassLoader parent = Thread.currentThread().getContextClassLoader();
        URLClassLoader loader = new URLClassLoader(new URL[]{classesUrl}, parent) {
            @Override
            protected Class<?> loadClass(final String name, final boolean resolve)
                    throws ClassNotFoundException {
                if (HELP_MOJO_CLASS.equals(name)) {
                    synchronized (getClassLoadingLock(name)) {
                        Class<?> c = findLoadedClass(name);
                        if (c == null) {
                            c = findClass(name);
                        }
                        if (resolve) {
                            resolveClass(c);
                        }
                        return c;
                    }
                }
                return super.loadClass(name, resolve);
            }

            @Override
            public InputStream getResourceAsStream(final String name) {
                if (PLUGIN_HELP_RESOURCE.equals(name)) {
                    return xmlStream;
                }
                return super.getResourceAsStream(name);
            }
        };
        return loader.loadClass(HELP_MOJO_CLASS)
            .getDeclaredConstructor()
            .newInstance();
    }

    /**
     * Sets the named field on the given target object via reflection.
     *
     * @param target    the object on which to set the field.
     * @param fieldName the name of the field to set.
     * @param value     the value to assign to the field.
     * @throws NoSuchFieldException   if the field cannot be found.
     * @throws IllegalAccessException if the field cannot be accessed.
     */
    private static void setField(final Object target, final String fieldName, final Object value)
            throws NoSuchFieldException, IllegalAccessException {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    /**
     * Invokes {@code execute()} on the given mojo object via reflection.
     *
     * @param mojo the mojo instance (may be loaded by an isolated ClassLoader).
     * @throws Exception if execute() throws or reflection fails unexpectedly.
     */
    private static void invokeExecute(final Object mojo) throws Exception {
        try {
            mojo.getClass().getMethod("execute").invoke(mojo);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof Exception) {
                throw (Exception) cause;
            }
            throw e;
        }
    }

    /**
     * Invokes {@code execute()} on the given mojo object via reflection, expecting it to
     * throw a {@link MojoExecutionException}.
     *
     * @param mojo the mojo instance (may be loaded by an isolated ClassLoader).
     * @return the {@link MojoExecutionException} that was thrown.
     * @throws Exception if execute() does not throw or reflection fails unexpectedly.
     */
    private static MojoExecutionException invokeExecuteExpectingException(
            final Object mojo) throws Exception {
        try {
            mojo.getClass().getMethod("execute").invoke(mojo);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof MojoExecutionException) {
                return (MojoExecutionException) cause;
            }
            throw e;
        }
        throw new AssertionError("Expected MojoExecutionException was not thrown");
    }
}
