package com.github.talshani.anproteg;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static com.github.talshani.anproteg.TemplateInstruction.Builder.*;

public class TemplateSerializerTest {

    @Test
    public void testSerialize() {
        assertEquals(
                "if (true) {\n\t$.append(\"xxx\");\n}\n",
                serialize(
                        ifInstruction("true"),
                        string("xxx"),
                        endIfInstruction()
                )
        );
    }

    public String serialize(TemplateInstruction... instructions) {
        StringBuilder sb = new StringBuilder();
        ArrayList<TemplateInstruction> lst = new ArrayList<TemplateInstruction>(instructions.length);
        for (TemplateInstruction instruction : instructions) {
            lst.add(instruction);
        }

        TemplateSerializer.serialize(sb, lst, SerializationContext.create("cls", "$"));
        return sb.toString();
    }

}