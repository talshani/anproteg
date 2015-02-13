package com.github.talshani.anproteg.presets.set3;

import com.github.talshani.anproteg.PrecompiledTemplate;

/**
 * @author Tal Shani
 */
public class SampleClass {
    @PrecompiledTemplate("this is the template {{param1}} with {{param2}}")
    public static void template1(String param1, String param2) {

    }
    @PrecompiledTemplate("call another {{call template1 param1, param2}}")
    public static void template2(String param1, String param2) {

    }
}
