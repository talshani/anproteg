package com.github.talshani.anproteg;

import org.apache.commons.lang.StringEscapeUtils;

/**
 * Serializes a template instruction list to java code
 *
 * @author Tal Shani
 */
final class TemplateSerializer {
    public static void serialize(StringBuilder sb, Iterable<TemplateInstruction> nodes, SerializationContext context) {
        SerializerInstructionVisitor visitor = new SerializerInstructionVisitor(sb, context);
        for (TemplateInstruction node : nodes) {
            node.accept(visitor);
        }
    }

    private static class SerializerInstructionVisitor implements InstructionVisitor {

        private final StringBuilder builder;
        private final String builderVarName;
        private final SerializationContext context;
        private int indentation = 0;

        public SerializerInstructionVisitor(StringBuilder sb, SerializationContext context) {
            this.builder = sb;
            this.context = context;
            this.builderVarName = context.builderName();
            indentation = context.initialIndent();
        }

        @Override
        public void visit(TemplateInstruction.ExpressionInstruction instruction) {
            startline();
            builder.append(builderVarName).append(".append(").append(instruction.expression()).append(");\n");
            endline();
        }

        @Override
        public void visit(TemplateInstruction.StringConstant instruction) {
            startline();
            String constant = StringEscapeUtils.escapeJava(instruction.value());
            builder.append(builderVarName).append(".append(\"").append(constant).append("\");");
            endline();
        }

        @Override
        public void visit(TemplateInstruction.IfInstruction instruction) {
            startline();
            builder.append("if (").append(instruction.condition()).append(") {");
            endline();
            indent();
        }

        @Override
        public void visit(TemplateInstruction.ElseIfInstruction instruction) {
            outdent();
            startline();
            builder.append("} else if (").append(instruction.condition()).append(") {");
            endline();
            indent();
        }

        @Override
        public void visit(TemplateInstruction.ElseInstruction instruction) {
            outdent();
            startline();
            builder.append("} else {");
            endline();
            indent();
        }

        @Override
        public void visit(TemplateInstruction.EndIfInstruction instruction) {
            outdent();
            startline();
            builder.append("}");
            endline();
        }

        @Override
        public void visit(TemplateInstruction.ForInstruction instruction) {
            startline();
            builder.append("for (").append(instruction.expression()).append(") {");
            endline();
            indent();
        }

        @Override
        public void visit(TemplateInstruction.EndForInstruction instruction) {
            outdent();
            startline();
            builder.append("}");
            endline();
        }

        @Override
        public void visit(TemplateInstruction.CallInstruction instruction) {
            startline();
            String params = instruction.expression().trim();
            if(!params.isEmpty()) {
                params = ", " + params;
            }
            builder.append(context.getClassFor(instruction.name())).append(".render(").append(context.builderName())
                    .append(params).append(");");
            endline();
        }

        private void endline() {
            builder.append("\n");
        }

        private void startline() {
            for (int i = 0; i < indentation; i++) {
                builder.append("\t");
            }
        }

        private void indent() {
            indentation++;
        }

        private void outdent() {
            indentation = Math.max(0, indentation - 1);
        }
    }
}
