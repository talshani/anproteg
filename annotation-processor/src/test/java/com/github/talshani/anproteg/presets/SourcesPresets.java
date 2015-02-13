package com.github.talshani.anproteg.presets;

import autovalue.shaded.com.google.common.common.collect.Lists;

import javax.tools.JavaFileObject;
import java.util.List;

import static com.google.testing.compile.JavaFileObjects.forResource;

/**
 * @author Tal Shani
 */
public class SourcesPresets {
    public static List<JavaFileObject> set1() {
        return Lists.newArrayList(
                forResource("com/github/talshani/anproteg/presets/set1/SampleClass.java")
        );
    }
    public static List<JavaFileObject> set2() {
        return Lists.newArrayList(
                forResource("com/github/talshani/anproteg/presets/set2/SampleClass.java")
        );
    }
    public static List<JavaFileObject> set3() {
        return Lists.newArrayList(
                forResource("com/github/talshani/anproteg/presets/set3/SampleClass.java")
        );
    }
}
