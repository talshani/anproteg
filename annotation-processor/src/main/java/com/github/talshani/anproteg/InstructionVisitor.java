package com.github.talshani.anproteg;

/**
 * @author Tal Shani
 */
interface InstructionVisitor {
    void visit(TemplateInstruction.ExpressionInstruction instruction);

    void visit(TemplateInstruction.StringConstant instruction);

    void visit(TemplateInstruction.IfInstruction instruction);

    void visit(TemplateInstruction.ElseIfInstruction instruction);

    void visit(TemplateInstruction.ElseInstruction instruction);

    void visit(TemplateInstruction.EndIfInstruction instruction);

    void visit(TemplateInstruction.ForInstruction instruction);

    void visit(TemplateInstruction.EndForInstruction instruction);

    void visit(TemplateInstruction.CallInstruction instruction);
}
