package disasm.RISC_V;

import disasm.ELF.TextSection;

import static disasm.util.InstructionDecoding.*;

public class Instruction {
    private final byte[] instruction;
    private final InstructionFormat format;
    private final int size;
    public Instruction(byte[] bytes, int offset) {
        boolean compressed = isCompressedLowByte(bytes[offset]);
        byte opcode = getOpcode(bytes[offset]);
        if (compressed) {
            format = InstructionFormat.COMPRESSED;
        } else {
            format = getStandardFormat(opcode);
        }
        size = compressed ? 2 : 4;
        instruction = new byte[size];
        System.arraycopy(bytes, offset, instruction, 0, size);
    }

    public Instruction(TextSection text, int offset) {
        boolean compressed = isCompressedLowByte(text.getByte(offset));
        byte opcode = getOpcode(text.getByte(offset));
        if (compressed) {
            format = InstructionFormat.COMPRESSED;
        } else {
            format = getStandardFormat(opcode);
        }
        size = compressed ? 2 : 4;
        instruction = text.getBytes(offset, size);
    }

    public boolean isCompressed() {
        return format == InstructionFormat.COMPRESSED;
    }
}
