package disasm.RISC_V;

import disasm.ELF.SymtabSection;
import disasm.ELF.TextSection;

import java.util.List;
import java.util.Map;

public class AssemblerWriter {
    private final List<Instruction> instructions;
    private final Map<Integer, String> labels;
    private final int maxLabelLen;

    public AssemblerWriter(SymtabSection symtab, TextSection text) {
        instructions = new InstructionsParser(text).getInstructions();
        labels = new LabelsParser(symtab).getLabels();
        int maxv = 0;
        for (Map.Entry<Integer, String> label : labels.entrySet()) {
            maxv = Math.max(maxv, label.getValue().length());
        }
        maxLabelLen = maxv;
    }


    public String write() {
        StringBuilder builder = new StringBuilder();
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
        return String.format("%08x %" + maxLabelLen + "s %s\n", addr, label, inst);
    }
}
