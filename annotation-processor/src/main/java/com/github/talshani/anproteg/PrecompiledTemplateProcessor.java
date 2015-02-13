package com.github.talshani.anproteg;

import autovalue.shaded.com.google.common.common.base.Joiner;
import com.google.auto.common.MoreElements;
import com.google.common.collect.ImmutableList;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.Set;

/**
 * @author Tal Shani
 */
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedAnnotationTypes("com.github.talshani.anproteg.PrecompiledTemplate")
public final class PrecompiledTemplateProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
//        if (roundEnv.processingOver()) return false;

        Messager messager = processingEnv.getMessager();
        messager.printMessage(Diagnostic.Kind.NOTE, "Generating Precompiled Templates");
        for (Element element : roundEnv.getElementsAnnotatedWith(PrecompiledTemplate.class)) {
            generate(MoreElements.asExecutable(element));
        }

        return false;
    }

    private void generate(ExecutableElement method) {
        TemplateClassHelper methodHelper = new TemplateClassHelper(method, processingEnv.getTypeUtils());
        Filer filer = processingEnv.getFiler();
        Messager messager = processingEnv.getMessager();

        messager.printMessage(Diagnostic.Kind.NOTE, "Precompiled Template for " + method + " at " + methodHelper.getFullyQualifiedClassName());

        JavaFileObject sourceFile;
        try {
            sourceFile = filer.createSourceFile(methodHelper.getFullyQualifiedClassName(), method);
        } catch (IOException e) {
            messager.printMessage(Diagnostic.Kind.ERROR, "Failed to generate source file for " + messager, method);
            return;
        }

        ImmutableList<TemplateInstruction> template = TemplateParser.parse(methodHelper.getTemplate());

        StringBuilder builder = new StringBuilder();
        builder.append("package ").append(methodHelper.getPackageName()).append(";\n");
        builder.append("@javax.annotation.Generated(\"").append(PrecompiledTemplateProcessor.class.getCanonicalName()).append("\")\n");
        builder.append("final class ").append(methodHelper.getClassName()).append(" {\n");

        builder.append("\tprivate ").append(methodHelper.getClassName()).append("(){}\n");

        String afterBuilderComma = methodHelper.getParamsSignature().length() > 0 ? ", " : "";

        builder.append("\tstatic void render(").append("\n");
        builder.append("\t\tStringBuilder $").append(afterBuilderComma).append("\n");
        builder.append("\t\t").append(methodHelper.getParamsSignature()).append("\n");
        builder.append("\t) {\n");
        TemplateSerializer.serialize(builder, template, SerializationContext.create(methodHelper, "$", 2));
        builder.append("\t}\n\n");

        builder.append("\tstatic String render(").append("\n");
        builder.append("\t\t").append(methodHelper.getParamsSignature()).append("\n");
        builder.append("\t) {\n");
        builder.append("\t\tStringBuilder $ = new StringBuilder();\n");
        builder.append("\t\trender($").append(afterBuilderComma).append(methodHelper.getParamNamesSignature()).append(");\n");
        builder.append("\t\treturn $.toString();\n");
        builder.append("\t}\n");

        builder.append("}");

        try (Writer writer = sourceFile.openWriter()) {
            writer.write(builder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
