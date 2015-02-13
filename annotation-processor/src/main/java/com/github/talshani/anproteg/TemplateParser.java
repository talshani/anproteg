package com.github.talshani.anproteg;

import com.google.common.collect.ImmutableList;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.talshani.anproteg.TemplateInstruction.Builder.*;

/**
 * @author Tal Shani
 */
final class TemplateParser {

    static final Pattern TAG_FINDER = Pattern.compile("(.*?)\\{\\{([^.]*?)}}");

    static final Pattern TAG = Pattern.compile("^\\s*(?:(if|else\\sif|for|call)\\s(.*)|(else|end\\sif|end\\sfor))\\s*$");

    /**
     * Breaks a template into template nodes that can be transformed into java code
     *
     * @param template
     * @return
     */
    public static ImmutableList<TemplateInstruction> parse(String template) {
        Matcher matcher = TAG_FINDER.matcher(template);
        ImmutableList.Builder<TemplateInstruction> builder = ImmutableList.builder();

        int endMatch = 0;
        while (matcher.find()) {
            endMatch = matcher.end();
            String prefix = matcher.group(1);
            String tagContents = matcher.group(2);
            if (!prefix.isEmpty()) {
                builder.add(string(prefix));
            }
            Matcher expressionTagMatcher = TAG.matcher(tagContents);
            if (expressionTagMatcher.matches()) {
                String type = expressionTagMatcher.group(1);
                String expression = null;
                if (type == null) {
                    type = expressionTagMatcher.group(3);
                } else {
                    expression = expressionTagMatcher.group(2).trim();
                }
                if (type.equals("if")) {
                    builder.add(ifInstruction(expression));
                } else if (type.equals("else if")) {
                    builder.add(elseIfInstruction(expression));
                } else if (type.equals("for")) {
                    builder.add(forInstruction(expression));
                } else if (type.equals("call")) {
                    assert expression != null;
                    String[] parts = expression.trim().split("\\s", 2);
                    if (parts.length == 2) {
                        builder.add(call(parts[0], parts[1]));
                    } else {
                        builder.add(call(parts[0], ""));
                    }
                } else if (type.equals("else")) {
                    builder.add(elseInstruction());
                } else if (type.equals("end if")) {
                    builder.add(endIfInstruction());
                } else if (type.equals("end for")) {
                    builder.add(endForInstruction());
                } else {
                    throw new RuntimeException("Unknown tag type: " + type + " with expression: " + expression);
                }
            } else {
                builder.add(expression(tagContents));
            }
        }
        // add last constant
        if (endMatch < template.length()) {
            builder.add(string(template.substring(endMatch)));
        }
        return builder.build();
    }

}
