package disasm.util;

import disasm.RISC_V.InstructionFormat;

public final class InstructionDecoding {
    public static final int OPCODE_MASK = 0x00_00_00_7f;
    public static final int RD_MASK = 0x00_00_0f_80;
    public static final int RD_SHIFT = 7;
    public static final int FUNCT_3_MASK = 0x00_00_70_00;
    public static final int FUNCT_3_SHIFT = 12;
    public static final int RS_1_MASK = 0x00_0f_80_00;
    public static final int RS_1_SHIFT = 15;
    public static final int RS_2_MASK = 0x01_f0_00_00;
    public static final int RS_2_SHIFT = 20;
    public static final int FUNCT_7_MASK = 0xfe_00_00_00;
    public static final int FUNCT_7_SHIFT = 25;

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

    public static String getRegisterName(int register) {
        switch (register) {
            case 0:
                return "zero";
            case 1:
                return "ra";
            case 2:
                return "sp";
            case 3:
                return "gp";
            case 4:
                return "tp";
        }
        if (register <= 7) {
            return "t" + (register - 5);
        }
        if (register <= 9) {
            return "s" + (register - 8);
        }
        if (register <= 17) {
            return "a" + (register - 10);
        }
        if (register <= 27) {
            return "s" + (register - 16);
        }
        return "t" + (register - 25);
    }


    // TODO: replace "throw new AssertionError(...) with getting unknown_command

    public String getRInstructionRepresentation(int instruction) {
        byte rd = (byte) ((instruction & RD_MASK) >>> RD_SHIFT);
        byte funct3 = (byte) ((instruction & FUNCT_3_MASK) >>> FUNCT_3_SHIFT);
        byte rs1 = (byte) ((instruction & RS_1_MASK) >>> RS_1_SHIFT);
        byte rs2 = (byte) ((instruction & RS_2_MASK) >>> RS_2_SHIFT);
        byte funct7 = (byte) ((instruction & FUNCT_7_MASK) >>> FUNCT_7_SHIFT);
        final String inst;
        switch (funct7) {
            case 0x00:
                inst = switch (funct3) {
                    case 0x0 -> "add";
                    case 0x4 -> "xor";
                    case 0x6 -> "or";
                    case 0x7 -> "and";
                    case 0x1 -> "sll";
                    case 0x5 -> "srl";
                    case 0x2 -> "slt";
                    case 0x3 -> "sltu";
                    default -> throw new AssertionError("Unexpected funct3 (" + funct3 + ")");
                };
                break;
            case 0x20:
                inst = switch (funct3) {
                    case 0x0 -> "sub";
                    case 0x5 -> "sra";
                    default -> throw new AssertionError("Unexpected funct3 (" + funct3 + ")");
                };
                break;
            case 0x01:
                inst = switch (funct3) {
                    case 0x0 -> "mul";
                    case 0x1 -> "mulh";
                    case 0x2 -> "mulsu";
                    case 0x3 -> "mulu";
                    case 0x4 -> "div";
                    case 0x5 -> "divu";
                    case 0x6 -> "rem";
                    case 0x7 -> "remu";
                    default -> throw new AssertionError("Unexpected funct3 (" + funct3 + ")");
                };
                break;
            default:
                throw new AssertionError("Unexpected funct7 (" + funct7 + ")");
        }
        final String dest = getRegisterName(rd);
        final String first = getRegisterName(rs1);
        final String second = getRegisterName(rs2);
        return inst + " " + dest + ", " + first + ", " + second;
    }

    public String getIInstructionRepresentation(int instruction) {
        byte opcode = (byte) (instruction & OPCODE_MASK);
        byte rd = (byte) ((instruction & RD_MASK) >>> RD_SHIFT);
        byte funct3 = (byte) ((instruction & FUNCT_3_MASK) >>> FUNCT_3_SHIFT);
        byte rs1 = (byte) ((instruction & RS_1_MASK) >>> RS_1_SHIFT);
        short imm12 = (short) ((instruction & (RS_2_MASK | FUNCT_7_MASK)) >>> RS_2_SHIFT);
        String inst;
        switch (opcode) {
            case 0b0010011:
                switch (funct3) {
                    case 0x0:
                        inst = "addi";
                        break;
                    case 0x1:
                        if ((imm12 & 0b111111100000) == 0) {
                            inst = "slli";
                        } else {
                            throw new AssertionError("Unexpected imm (" + imm12 + ")");
                        }
                        break;
                    case 0x2:
                        inst = "slti";
                        break;
                    case 0x3:
                        inst = "sltiu";
                        break;
                    case 0x4:
                        inst = "xori";
                        break;
                    case 0x5:
                        if ((imm12 & 0b111111100000) == 0) {
                            inst = "srli";
                        } else if ((imm12 & 0b111111100000) == (0x20 << 5)) {
                            inst = "srai";
                        } else {
                            throw new AssertionError("Unexpected imm (" + imm12 + ")");
                        }
                        break;
                    case 0x6:
                        inst = "ori";
                        break;
                    case 0x7:
                        inst = "andi";
                        break;
                    default:
                        throw new AssertionError("Unexpected funct3 (" + funct3 + ")");
                }
                break;
            case 0b0000011:
                inst = switch (funct3) {
                    case 0x0 -> "lb";
                    case 0x1 -> "lh";
                    case 0x2 -> "lw";
                    case 0x4 -> "lbu";
                    case 0x5 -> "lhu";
                    default -> throw new AssertionError("Unexpected funct3 (" + funct3 + ")");
                };
                break;
            case 0b1100111:
                if (funct3 == 0x0) {
                    inst = "jalr";
                } else {
                    throw new AssertionError("Unexpected funct3 (" + funct3 + ")");
                }
                break;
            case 0b1110011:
                if (funct3 == 0x0) {
                    if (imm12 == 0x0) {
                        inst = "ecall";
                    } else if (imm12 == 0x1) {
                        inst = "ebreak";
                    } else {
                        throw new AssertionError("Unexpected imm (" + imm12 + ")");
                    }
                } else {
                    throw new AssertionError("Unexpected funct3 (" + funct3 + ")");
                }
                break;
            default:
                throw new AssertionError("Unexpected opcode (" + opcode + ")");
        }
        return switch (inst) {
            case "ecall", "ebreak" -> inst;
            case "jalr" -> String.format("jalr %s, %d(%s)",
                    getRegisterName(rd), imm12, getRegisterName(rs1));

            case "lb", "lh", "lw", "lbu", "lhu" -> String.format("%s %s, %d(%s)",
                    inst, getRegisterName(rd), imm12, getRegisterName(rs1));

            case "slli", "srli", "srai" -> String.format("%s %s, %s, %d",
                    inst, getRegisterName(rd), getRegisterName(rs1), imm12 & 0b11111);

            default -> String.format("%s %s, %s, %d",
                    inst, getRegisterName(rd), getRegisterName(rs1), imm12);
        };
    }
}
