package com.code.printer.util;

import com.jfoenix.validation.base.ValidatorBase;
import javafx.scene.control.TextInputControl;

import java.util.regex.Pattern;

public class RegexValidator extends ValidatorBase {

    private String regexPattern;
    private Pattern regexPatternCompiled;

    public RegexValidator(){}

    /**
     * {@inheritDoc}
     */
    @Override
    protected void eval() {
        if (srcControl.get() instanceof TextInputControl) {
            evalTextInputField();
        }
    }

    private void evalTextInputField() {
        TextInputControl textField = (TextInputControl) srcControl.get();
        if (regexPatternCompiled.matcher(textField.getText()).matches()) {
            hasErrors.set(false);
        } else {
            hasErrors.set(true);
        }
    }

    public void setRegexPattern(String regexPattern) {
        this.regexPattern = regexPattern;
        this.regexPatternCompiled = Pattern.compile(regexPattern);
    }

    public String getRegexPattern() {
        return regexPattern;
    }
}
