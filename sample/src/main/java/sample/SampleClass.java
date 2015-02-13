package sample;

import com.github.talshani.anproteg.PrecompiledTemplate;

/**
 * @author Tal Shani
 */
public class SampleClass {
    private int x;

    @PrecompiledTemplate("this is the template {{param1}} with {{param2}}")
    static void render(StringBuilder $builder, String param1, String param2) {
        PrecompiledTemplate_SampleClass_render.render($builder, param1, param2);
    }

    @PrecompiledTemplate("the template is: {{call render param1, param2}}")
    public static void myTemplate(StringBuilder $builder, String param1, String param2) {
        PrecompiledTemplate_SampleClass_render.render($builder, param1, param2);
    }
}
