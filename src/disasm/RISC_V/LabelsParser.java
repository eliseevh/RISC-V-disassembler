package disasm.RISC_V;

import disasm.ELF.SymtabEntry;
import disasm.ELF.SymtabSection;

import java.util.HashMap;
import java.util.Map;

public class LabelsParser {
    private final Map<Integer, String> labels;
    public LabelsParser(SymtabSection symtab) {
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
    }

    public Map<Integer, String> getLabels() {
        return labels;
    }
}
