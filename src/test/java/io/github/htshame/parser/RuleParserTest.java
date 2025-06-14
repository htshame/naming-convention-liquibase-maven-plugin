package io.github.htshame.parser;

import io.github.htshame.exception.RuleParserException;
import io.github.htshame.rule.Rule;
import io.github.htshame.rule.processor.AttrEndsWithConditionedProcessor;
import io.github.htshame.rule.processor.AttrEndsWithProcessor;
import io.github.htshame.rule.processor.AttrStartsWithProcessor;
import io.github.htshame.rule.processor.NoHyphensInAttributesProcessor;
import io.github.htshame.rule.processor.NoUnderscoresInAttributesProcessor;
import io.github.htshame.rule.processor.TagMustExistProcessor;
import org.junit.Test;

import java.io.File;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class RuleParserTest {

    private static final int RULE_SET_SIZE = 6;

    /**
     * Test successful case of rules.xml parsing.
     *
     * @throws RuleParserException - if parsing fails.
     */
    @Test
    public void testParseRulesSuccess() throws RuleParserException {
        // arrange
        RuleParser ruleParser = new RuleParser();
        File ruleFile = new File("src/test/resources/rules.xml");
        Set<Class<? extends Rule>> ruleClassNames = Set.of(
                AttrEndsWithConditionedProcessor.class,
                AttrEndsWithProcessor.class,
                AttrStartsWithProcessor.class,
                NoHyphensInAttributesProcessor.class,
                NoUnderscoresInAttributesProcessor.class,
                TagMustExistProcessor.class
        );

        // act
        Set<Rule> actual = ruleParser.parseRules(ruleFile);

        // assert
        assertEquals(RULE_SET_SIZE, actual.size());
        Set<Class<? extends Rule>> actualClasses = actual.stream()
                .map(Rule::getClass)
                .collect(Collectors.toSet());
        assertTrue(ruleClassNames.containsAll(actualClasses));
    }

    /**
     * Test failed case of rules.xml parsing.
     */
    @Test
    public void testParseRulesFailure() {
        // arrange
        RuleParser ruleParser = new RuleParser();
        File ruleFile = new File("src/test/resources/wrongRules.xml");

        // act & assert
        assertThrows(RuleParserException.class, () -> ruleParser.parseRules(ruleFile));
    }
}
