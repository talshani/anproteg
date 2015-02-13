package sample;

import com.github.talshani.anproteg.PrecompiledTemplate;

/**
 * @author Tal Shani
 */
public class MyClass {
    @PrecompiledTemplate("this is the template {{param1}} with {{param2}}")
    public static void render(StringBuilder $builder, String param1, String param2) {
        PrecompiledTemplate_MyClass_render.render($builder, param1, param2);
    }
}
