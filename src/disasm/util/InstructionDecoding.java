package disasm.util;

import disasm.RISC_V.InstructionFormat;

public final class InstructionDecoding {
    private InstructionDecoding() {}

    public static byte getOpcode(byte lowByte) {
        // in compressed instruction opcode is 2 lowest bits, in standard instruction opcode is 7 lowest bits
        if (isCompressedLowByte(lowByte)) {
            return (byte) (lowByte & 0b11);
        } else {
            return (byte) (lowByte & 0b01_11_11_11);
        }
    }

    public static boolean isCompressedLowByte(byte lowByte) {
        byte low2 = (byte) (lowByte & 0b11);
        return low2 != 0b11;
    }

    public static InstructionFormat getStandardFormat(byte opcode) {
        return switch (opcode) {
            case 0b0110011 -> InstructionFormat.R;
            case 0b0010011, 0b0000011, 0b1100111, 0b1110011 -> InstructionFormat.I;
            case 0b0100011 -> InstructionFormat.S;
            case 0b1100011 -> InstructionFormat.B;
            case 0b1101111 -> InstructionFormat.J;
            case 0b0110111, 0b0010111 -> InstructionFormat.U;
            default -> throw new AssertionError("Not a standard format opcode");
        };
    }
}
