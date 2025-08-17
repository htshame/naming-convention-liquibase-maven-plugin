package io.github.htshame.parser;

import io.github.htshame.exception.RuleParserException;
import io.github.htshame.rule.Rule;
import io.github.htshame.rule.processor.AttrEndsWithConditionedProcessor;
import io.github.htshame.rule.processor.AttrEndsWithProcessor;
import io.github.htshame.rule.processor.AttrMustExistInTagProcessor;
import io.github.htshame.rule.processor.AttrNotEndsWithConditionedProcessor;
import io.github.htshame.rule.processor.AttrNotStartsWithConditionedProcessor;
import io.github.htshame.rule.processor.AttrStartsWithConditionedProcessor;
import io.github.htshame.rule.processor.AttrStartsWithProcessor;
import io.github.htshame.rule.processor.NoHyphensInAttributesProcessor;
import io.github.htshame.rule.processor.NoLowercaseInAttributesProcessor;
import io.github.htshame.rule.processor.NoSpacesInAttributesProcessor;
import io.github.htshame.rule.processor.NoUnderscoresInAttributesProcessor;
import io.github.htshame.rule.processor.NoUppercaseInAttributesProcessor;
import io.github.htshame.rule.processor.TagMustExistProcessor;
import io.github.htshame.rule.processor.changelog.TagMustNotExistInChangeLogProcessor;
import io.github.htshame.rule.processor.changelogfile.ChangeLogFileLinesLimitProcessor;
import io.github.htshame.rule.processor.changelogfile.ChangeLogFileMustMatchRegexpProcessor;
import io.github.htshame.rule.processor.changelogfile.ChangeLogMustEndWithNewlineProcessor;
import io.github.htshame.rule.processor.changelogfile.NoTabsInChangeLogProcessor;
import io.github.htshame.rule.processor.changelogfile.NoTrailingSpacesInChangeLogProcessor;
import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class ChangeSetRuleParserTest {

    private static final int RULE_SET_SIZE = 19;

    /**
     * Test successful case of rules.xml parsing.
     */
    @Test
    public void testParseRulesSuccess() {
        // arrange
        File ruleFile = new File("src/test/resources/rules.xml");
        Set<Class<? extends Rule>> ruleClassNames = Set.of(
                AttrEndsWithConditionedProcessor.class,
                AttrNotEndsWithConditionedProcessor.class,
                AttrEndsWithProcessor.class,
                AttrStartsWithConditionedProcessor.class,
                AttrNotStartsWithConditionedProcessor.class,
                AttrStartsWithProcessor.class,
                NoHyphensInAttributesProcessor.class,
                NoUnderscoresInAttributesProcessor.class,
                TagMustExistProcessor.class,
                NoUppercaseInAttributesProcessor.class,
                NoLowercaseInAttributesProcessor.class,
                AttrMustExistInTagProcessor.class,
                NoSpacesInAttributesProcessor.class,
                ChangeLogFileMustMatchRegexpProcessor.class,
                ChangeLogFileLinesLimitProcessor.class,
                NoTabsInChangeLogProcessor.class,
                NoTrailingSpacesInChangeLogProcessor.class,
                ChangeLogMustEndWithNewlineProcessor.class,
                TagMustNotExistInChangeLogProcessor.class
        );

        // act
        List<Rule> actual = RuleParser.parseRules(ruleFile);

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
        File ruleFile = new File("src/test/resources/wrongRules.xml");

        // act & assert
        assertThrows(RuleParserException.class, () -> RuleParser.parseRules(ruleFile));
    }
}
