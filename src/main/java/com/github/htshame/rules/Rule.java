package com.github.htshame.rules;

import com.github.htshame.enums.RuleEnum;
import com.github.htshame.exception.ValidationException;
import org.w3c.dom.Document;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public interface Rule {

    List<String> GLOBALLY_EXCLUDED_TAGS = Arrays.asList("databaseChangeLog", "comment", "include");

    RuleEnum getName();

    void validate(Document doc, File file) throws ValidationException;
}
