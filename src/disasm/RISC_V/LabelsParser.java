package disasm.RISC_V;

import disasm.ELF.SymtabEntry;
import disasm.ELF.SymtabSection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LabelsParser {
    private final Map<Integer, String> labels;
    public LabelsParser(SymtabSection symtab, List<Instruction> instructions) {
        labels = new HashMap<>();
        for (SymtabEntry entry : symtab.getEntries()) {
            if (entry.getType().equals("FUNC")) {
                String name;
                if (entry.getName().isEmpty()) {
                    name = String.format("LOC_%05x", entry.getValue());
                } else {
                    name = entry.getName();
                }
                labels.put(entry.getValue(), name);
            }
        }
        for (Instruction instruction : instructions) {
            if (instruction.getJumpOffset() != null) {
                int address = instruction.getAddress() + instruction.getJumpOffset();
                if (!labels.containsKey(address)) {
                    labels.put(address, String.format("LOC_%05x", address));
                }
            }
        }
    }

    public Map<Integer, String> getLabels() {
        return labels;
    }
}
