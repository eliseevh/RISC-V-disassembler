package disasm.RISC_V;

import disasm.ELF.TextSection;
import disasm.util.BytesOperations;
import disasm.util.InstructionDecoding;

import static disasm.util.InstructionDecoding.*;

public class Instruction {
    private final byte[] instruction;
    private final InstructionFormat format;
    private final int address;
    private final int size;
//    public Instruction(byte[] bytes, int offset) {
//        boolean compressed = isCompressedLowByte(bytes[offset]);
//        byte opcode = getOpcode(bytes[offset]);
//        if (compressed) {
//            format = InstructionFormat.COMPRESSED;
//        } else {
//            format = getStandardFormat(opcode);
//        }
//        size = compressed ? 2 : 4;
//        instruction = new byte[size];
//        System.arraycopy(bytes, offset, instruction, 0, size);
//    }

    public Instruction(TextSection text, int offset) {
        boolean compressed = isCompressedLowByte(text.getByte(offset));
        address = text.getAddress() + offset;
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

    public int getAddress() {
        return address;
    }

    @Override
    public String toString() {
        if (isCompressed()) {
            return "compressed_command (" + BytesOperations.getShortLittleEndian(instruction, 0) + ")";
        }
        int operation = BytesOperations.getIntLittleEndian(instruction, 0);
        return switch (format) {
            case B -> InstructionDecoding.getBInstructionRepresentation(operation);
            case I -> InstructionDecoding.getIInstructionRepresentation(operation);
            case J -> InstructionDecoding.getJInstructionRepresentation(operation);
            case R -> InstructionDecoding.getRInstructionRepresentation(operation);
            case S -> InstructionDecoding.getSInstructionRepresentation(operation);
            case U -> InstructionDecoding.getUInstructionRepresentation(operation);
            default -> "unknown_command";
        };
    }
}
