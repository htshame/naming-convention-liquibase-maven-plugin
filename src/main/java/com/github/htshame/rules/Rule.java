package com.github.htshame.rules;

import com.github.htshame.enums.RuleEnum;
import com.github.htshame.exception.ValidationException;
import org.w3c.dom.Document;

import java.io.File;

public interface Rule {

    RuleEnum getName();

    void validate(Document doc, File file) throws ValidationException;
}
