package disasm.RISC_V;

import disasm.ELF.SymtabSection;
import disasm.ELF.TextSection;

import java.util.List;
import java.util.Map;

public class DisassemblerToString {
    private final List<Instruction> instructions;
    private final Map<Integer, String> labels;
    private final SymtabSection symtab;
    private final int maxLabelLen;

    public DisassemblerToString(SymtabSection symtab, TextSection text) {
        instructions = new InstructionsParser(text).getInstructions();
        labels = new LabelsParser(symtab, instructions).getLabels();
        this.symtab = symtab;
        int maxv = 0;
        for (Map.Entry<Integer, String> label : labels.entrySet()) {
            maxv = Math.max(maxv, label.getValue().length());
        }
        maxLabelLen = maxv;
    }

    public String disasm() {
        StringBuilder builder = new StringBuilder();
        builder.append(textSectionDisasm());
        builder.append("\n");
        builder.append(symtabSectionDisasm());
        return builder.toString();
    }

    private String symtabSectionDisasm() {
        StringBuilder builder = new StringBuilder(".symtab\n");
        builder.append(symtab);
        return builder.toString();
    }

    private String textSectionDisasm() {
        StringBuilder builder = new StringBuilder(".text\n");
        for (int i = 0; i < instructions.size(); i++) {
            builder.append(getInstruction(i));
        }
        return builder.toString();
    }
    private String getInstruction(int idx) {
        Instruction inst = instructions.get(idx);
        int addr = inst.getAddress();
        String label = "";
        if (labels.containsKey(addr)) {
            label = labels.get(addr) + ":";
        }
        StringBuilder builder = new StringBuilder(
                String.format("%08x %" + (maxLabelLen + 1) + "s %s", addr, label, inst));


        // add comment with jump destination label
        if (inst.getJumpOffset() != null) {
            builder.append(" # " + labels.get(inst.getAddress() + inst.getJumpOffset()));
        }

        builder.append("\n");
        return builder.toString();
    }
}
