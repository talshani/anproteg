package com.github.talshani.anproteg;

import com.google.auto.value.AutoValue;

/**
 * @author Tal Shani
 */
abstract class TemplateInstruction {

    static class Builder {
        static StringConstant string(String value) {
            return new AutoValue_TemplateInstruction_StringConstant(value);
        }

        static IfInstruction ifInstruction(String condition) {
            return new AutoValue_TemplateInstruction_IfInstruction(condition);
        }

        static ElseIfInstruction elseIfInstruction(String condition) {
            return new AutoValue_TemplateInstruction_ElseIfInstruction(condition);
        }

        static ElseInstruction elseInstruction() {
            return new AutoValue_TemplateInstruction_ElseInstruction();
        }

        static EndIfInstruction endIfInstruction() {
            return new AutoValue_TemplateInstruction_EndIfInstruction();
        }

        static ForInstruction forInstruction(String expression) {
            return new AutoValue_TemplateInstruction_ForInstruction(expression);
        }

        static EndForInstruction endForInstruction() {
            return new AutoValue_TemplateInstruction_EndForInstruction();
        }

        static ExpressionInstruction expression(String expression) {
            return new AutoValue_TemplateInstruction_ExpressionInstruction(expression);
        }

        static CallInstruction call(String name, String expression) {
            return new AutoValue_TemplateInstruction_CallInstruction(name, expression);
        }
    }

    protected TemplateInstruction() {
    }

    @AutoValue
    public static abstract class StringConstant extends TemplateInstruction {
        abstract String value();

        @Override
        public void accept(InstructionVisitor visitor) {
            visitor.visit(this);
        }
    }

    @AutoValue
    public static abstract class IfInstruction extends TemplateInstruction {
        abstract String condition();

        @Override
        public void accept(InstructionVisitor visitor) {
            visitor.visit(this);
        }
    }

    @AutoValue
    public static abstract class ElseIfInstruction extends TemplateInstruction {
        abstract String condition();

        @Override
        public void accept(InstructionVisitor visitor) {
            visitor.visit(this);
        }
    }

    @AutoValue
    public static abstract class ElseInstruction extends TemplateInstruction {

        @Override
        public void accept(InstructionVisitor visitor) {
            visitor.visit(this);
        }
    }

    @AutoValue
    public static abstract class EndIfInstruction extends TemplateInstruction {

        @Override
        public void accept(InstructionVisitor visitor) {
            visitor.visit(this);
        }
    }

    @AutoValue
    public static abstract class ForInstruction extends TemplateInstruction {
        abstract String expression();

        @Override
        public void accept(InstructionVisitor visitor) {
            visitor.visit(this);
        }
    }

    @AutoValue
    public static abstract class EndForInstruction extends TemplateInstruction {

        @Override
        public void accept(InstructionVisitor visitor) {
            visitor.visit(this);
        }
    }

    @AutoValue
    public static abstract class ExpressionInstruction extends TemplateInstruction {
        abstract String expression();

        @Override
        public void accept(InstructionVisitor visitor) {
            visitor.visit(this);
        }
    }

    @AutoValue
    public static abstract class CallInstruction extends TemplateInstruction {
        abstract String name();

        abstract String expression();

        @Override
        public void accept(InstructionVisitor visitor) {
            visitor.visit(this);
        }
    }

    public abstract void accept(InstructionVisitor visitor);
}
