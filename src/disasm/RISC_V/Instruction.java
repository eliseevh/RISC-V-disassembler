package disasm.RISC_V;

import disasm.ELF.TextSection;
import disasm.util.BytesOperations;
import disasm.util.CompressedInstructionDecoding;
import disasm.util.InstructionDecoding;
import disasm.util.UnknownCommandError;

import static disasm.util.InstructionDecoding.*;

public class Instruction {
    private final byte[] instruction;
    private final InstructionFormat format;
    private final int address;

    public Instruction(TextSection text, int offset) {
        boolean compressed = isCompressedLowByte(text.getByte(offset));
        address = text.getAddress() + offset;
        byte opcode = getOpcode(text.getByte(offset));
        if (compressed) {
            format = InstructionFormat.COMPRESSED;
        } else {
            format = getStandardFormat(opcode);
        }
        int size = compressed ? 2 : 4;
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
            short op = BytesOperations.getShortLittleEndian(instruction, 0);
            try {
                return CompressedInstructionDecoding.getCompressedInstructionRepresentation(op);
            } catch (UnknownCommandError e) {
                return "unknown_command";
            }
        }
        int operation = BytesOperations.getIntLittleEndian(instruction, 0);
        try {
            return switch (format) {
                case B -> InstructionDecoding.getBInstructionRepresentation(operation);
                case I -> InstructionDecoding.getIInstructionRepresentation(operation);
                case J -> InstructionDecoding.getJInstructionRepresentation(operation);
                case R -> InstructionDecoding.getRInstructionRepresentation(operation);
                case S -> InstructionDecoding.getSInstructionRepresentation(operation);
                case U -> InstructionDecoding.getUInstructionRepresentation(operation);
                case SYSTEM -> InstructionDecoding.getSystemInstructionRepresentation(operation);
                default -> "unknown_command";
            };
        } catch (UnknownCommandError e) {
            return "unknown_command";
        }
    }

    public Integer getJumpOffset() {
        if (format == InstructionFormat.COMPRESSED) {
            short command = BytesOperations.getShortLittleEndian(this.instruction, 0);
            return CompressedInstructionDecoding.getCompressedJumpOffset(command);
        }
        int instruction = BytesOperations.getIntLittleEndian(this.instruction, 0);
        return switch (format) {
            case B -> InstructionDecoding.getBFormatOffset(instruction);
            case J -> InstructionDecoding.getJFormatOffset(instruction);
            default -> null;
        };
    }
}
