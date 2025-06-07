package com.github.htshame.rules;

import com.github.htshame.enums.RuleEnum;
import org.apache.maven.plugin.MojoExecutionException;
import org.w3c.dom.Document;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public interface Rule {

    List<String> GLOBALLY_EXCLUDED_TAGS = Arrays.asList("databaseChangeLog", "comment", "include");

    RuleEnum getName();

    void validate(Document doc, File file) throws MojoExecutionException;
}
