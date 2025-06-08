package com.github.htshame.exception;

public class RuleParserException extends Exception {

    public RuleParserException(String message) {
        super(message);
    }

    public RuleParserException(String message, Throwable e) {
        super(message, e);
    }
}
