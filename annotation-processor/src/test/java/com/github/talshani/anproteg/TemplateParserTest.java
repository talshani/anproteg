package com.github.talshani.anproteg;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;
import static com.github.talshani.anproteg.TemplateInstruction.Builder.*;

public class TemplateParserTest {

    @Test
    public void testSimpleTemplate() {
        assertSingleNode("constant", string("constant"));
        assertSingleNode("{{param}}", expression("param"));
        assertSingleNode("{{if param}}", ifInstruction("param"));
        assertSingleNode("{{else if param}}", elseIfInstruction("param"));
        assertSingleNode("{{else}}", elseInstruction());
        assertSingleNode("{{end if}}", endIfInstruction());
        assertSingleNode("{{for expression}}", forInstruction("expression"));
        assertSingleNode("{{end for }}", endForInstruction());
    }

    @Test
    public void testCompound() {
        assertSingleNode("constant{par}", string("constant{par}"));
        assertSingleNode("constant{{par}}", string("constant"), expression("par"));
        assertSingleNode("{{if true}}code{{else}}other code{{end if}}", ifInstruction("true"),
                string("code"),
                elseInstruction(),
                string("other code"),
                endIfInstruction()
        );
    }


    private void assertSingleNode(String template, TemplateInstruction... expected) {
        List<TemplateInstruction> lst = TemplateParser.parse(template);
        assertEquals(expected.length, lst.size());
        assertArrayEquals(expected, lst.toArray());
    }
}