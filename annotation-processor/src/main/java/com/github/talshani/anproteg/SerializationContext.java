package com.github.talshani.anproteg;

import com.google.auto.value.AutoValue;

/**
 * @author Tal Shani
 */
@AutoValue
abstract class SerializationContext {
    abstract String classPrefix();

    abstract int initialIndent();

    abstract String builderName();

    public String getClassFor(String methodName) {
        return classPrefix() + "_" + methodName;
    }

    public static SerializationContext create(String classPrefix, String builderName, int initialIndent) {
        return new AutoValue_SerializationContext(classPrefix, initialIndent, builderName);
    }

    public static SerializationContext create(TemplateClassHelper helper, String builderName, int initialIndent) {
        return create(helper.getClassNamePrefix(), builderName, initialIndent);
    }

    public static SerializationContext create(String classPrefix, String builderName) {
        return new AutoValue_SerializationContext(classPrefix, 0, builderName);
    }

}
