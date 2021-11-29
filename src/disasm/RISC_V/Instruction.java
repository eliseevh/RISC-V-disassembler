package disasm.RISC_V;

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

//    @Override
//    public String toString() {
//        StringBuilder builder = new StringBuilder();
//    }
}
