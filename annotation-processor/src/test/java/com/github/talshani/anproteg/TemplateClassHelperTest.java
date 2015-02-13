package com.github.talshani.anproteg;

import com.github.talshani.anproteg.presets.SourcesPresets;
import com.google.auto.common.MoreElements;
import com.google.testing.compile.JavaSourcesSubjectFactory;
import org.junit.Test;
import org.truth0.Truth;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class TemplateClassHelperTest {

    @Test
    public void testNaming() throws Exception {
        List<TemplateClassHelper> helpers = process(SourcesPresets.set1(), new TestingProcessor()).helpers;
        assertEquals(1, helpers.size());
        TemplateClassHelper helper = helpers.get(0);
        assertEquals("PrecompiledTemplate_SampleClass_render", helper.getClassName());
        assertEquals("sample.PrecompiledTemplate_SampleClass_render", helper.getFullyQualifiedClassName());
        assertEquals("sample", helper.getPackageName());
    }

    @Test
    public void testGetParams() throws Exception {
        // test for methods with appendable as first argument
        List<TemplateClassHelper> helpers = process(SourcesPresets.set1(), new TestingProcessor()).helpers;
        assertEquals(1, helpers.size());
        TemplateClassHelper helper = helpers.get(0);
        assertEquals(2, helper.getRawParams().size());
        assertEquals("param1, param2", helper.getParamNamesSignature());

        // test for methods without appendable as first argument
        helper = process(SourcesPresets.set2(), new TestingProcessor()).helpers.get(0);
        assertEquals(2, helper.getRawParams().size());
        assertEquals("param1, param2", helper.getParamNamesSignature());
    }

    @SupportedSourceVersion(SourceVersion.RELEASE_7)
    @SupportedAnnotationTypes("com.github.talshani.anproteg.PrecompiledTemplate")
    static class TestingProcessor extends AbstractProcessor {

        List<TemplateClassHelper> helpers = new ArrayList<>();

        @Override
        public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
            for (Element element : roundEnv.getElementsAnnotatedWith(PrecompiledTemplate.class)) {
                helpers.add(new TemplateClassHelper(MoreElements.asExecutable(element), processingEnv.getTypeUtils()));
            }
            return false;
        }
    }


    private <T extends Processor> T process(Iterable<JavaFileObject> files, T processor) {
        Truth.ASSERT.about(JavaSourcesSubjectFactory.javaSources())
                .that(files)
                .processedWith(processor)
                .compilesWithoutError();
        return processor;
    }
}