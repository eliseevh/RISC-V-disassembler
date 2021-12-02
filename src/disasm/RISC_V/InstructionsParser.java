package disasm.RISC_V;

import disasm.ELF.TextSection;

import java.util.ArrayList;
import java.util.List;

public class InstructionsParser {
    private final List<Instruction> instructions;
    public InstructionsParser(TextSection text) {
        instructions = new ArrayList<>();
        int pos = 0;
        int size = text.getSize();
        while (pos < size) {
            Instruction inst = new Instruction(text, pos);
            instructions.add(inst);
            pos += inst.isCompressed() ? 2 : 4;
        }
    }

    public List<Instruction> getInstructions() {
        return instructions;
    }
}
