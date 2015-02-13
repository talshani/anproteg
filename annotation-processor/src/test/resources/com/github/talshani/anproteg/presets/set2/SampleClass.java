package sample;

import com.github.talshani.anproteg.PrecompiledTemplate;

/**
 * @author Tal Shani
 */
public class SampleClass {
    @PrecompiledTemplate("this is the template {{param1}} with {{param2}}")
    public static void render(String param1, String param2) {

    }
}
