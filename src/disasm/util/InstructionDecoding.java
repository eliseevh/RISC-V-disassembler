package disasm.util;

import disasm.RISC_V.InstructionFormat;

import static disasm.util.BytesOperations.getSignExtension;

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
            case 0b0010011, 0b0000011, 0b1100111 -> InstructionFormat.I;
            case 0b1110011 -> InstructionFormat.SYSTEM;
            case 0b0100011 -> InstructionFormat.S;
            case 0b1100011 -> InstructionFormat.B;
            case 0b1101111 -> InstructionFormat.J;
            case 0b0110111, 0b0010111 -> InstructionFormat.U;
            default -> throw new AssertionError("Not a standard format opcode");
        };
    }

    public static String getRegisterName(int register) {
        if (register < 0 || register > 31) {
            throw new IllegalArgumentException("In RISC-V register number must be between 0 and 31, not " + register);
        }
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


    public static String getCSRName(int csr) {
        switch (csr) {
            case 0x000:
                return "ustatus";
            case 0x004:
                return "uie";
            case 0x005:
                return "utvec";
            case 0x040:
                return "uscratch";
            case 0x041:
                return "uepc";
            case 0x042:
                return "ucause";
            case 0x043:
                return "utval";
            case 0x044:
                return "uip";
            case 0x001:
                return "fflags";
            case 0x002:
                return "frm";
            case 0x003:
                return "fcsr";
            case 0xc00:
                return "cycle";
            case 0xc01:
                return "time";
            case 0xc02:
                return "instret";
            case 0xc80:
                return "cycleh";
            case 0xc81:
                return "timeh";
            case 0xc82:
                return "instreth";
            case 0x100:
                return "sstatus";
            case 0x102:
                return "sedeleg";
            case 0x103:
                return "sideleg";
            case 0x104:
                return "sie";
            case 0x105:
                return "stvec";
            case 0x106:
                return "scounteren";
            case 0x140:
                return "sscratch";
            case 0x141:
                return "sepc";
            case 0x142:
                return "scause";
            case 0x143:
                return "stval";
            case 0x144:
                return "sip";
            case 0x180:
                return "satp";
            case 0xf11:
                return "mvendorid";
            case 0xf12:
                return "marchid";
            case 0xf13:
                return "mimpid";
            case 0xf14:
                return "mhartid";
            case 0x300:
                return "mstatus";
            case 0x301:
                return "misa";
            case 0x302:
                return "medeleg";
            case 0x303:
                return "mideleg";
            case 0x304:
                return "mie";
            case 0x305:
                return "mtvec";
            case 0x306:
                return "mcounteren";
            case 0x340:
                return "mscratch";
            case 0x341:
                return "mepc";
            case 0x342:
                return "mcause";
            case 0x343:
                return "mtval";
            case 0x344:
                return "mip";
            case 0xb00:
                return "mcycle";
            case 0xb02:
                return "minstret";
            case 0xb80:
                return "mcycleh";
            case 0xb82:
                return "minstreth";
            case 0x320:
                return "mcountinhibit";
            case 0x7a0:
                return "tselect";
            case 0x7b0:
                return "dcsr";
            case 0x7b1:
                return "dpc";
            case 0x7b2:
                return "dscratch0";
            case 0x7b3:
                return "dscratch1";

        }
        if (0xc03 <=  csr && csr <= 0xc1f) {
            return "hpmcounter" + (csr - 0xc00);
        }
        if (0xc83 <= csr && csr <= 0xc9f) {
            return "hpmcounter" + (csr - 0xc80) + "h";
        }
        if (0x3a0 <= csr && csr <= 0x3a3) {
            return "pmpcfg" + (csr - 0x3a0);
        }
        if (0x3b0 <= csr && csr <= 0x3bf) {
            return "pmpaddr" + (csr - 0x3b0);
        }
        if (0xb03 <= csr && csr <= 0xb1f) {
            return "mhpmcounter" + (csr - 0xb00);
        }
        if (0xb83 <= csr && csr <= 0xb9f) {
            return "mhpmcounter" + (csr - 0xb80) + "h";
        }
        if (0x323 <= csr && csr <= 0x33f) {
            return "mhpmevent" + (csr - 0x320);
        }
        if (0x7a1 <= csr && csr <= 0x7a3) {
            return "tdata" + (csr - 0x7a0);
        }
        return Integer.toString(csr);
    }


    public static String getRInstructionRepresentation(int instruction) throws UnknownCommandError {
        byte rd = (byte) ((instruction & RD_MASK) >>> RD_SHIFT);
        byte funct3 = (byte) ((instruction & FUNCT_3_MASK) >>> FUNCT_3_SHIFT);
        byte rs1 = (byte) ((instruction & RS_1_MASK) >>> RS_1_SHIFT);
        byte rs2 = (byte) ((instruction & RS_2_MASK) >>> RS_2_SHIFT);
        byte funct7 = (byte) ((instruction & FUNCT_7_MASK) >>> FUNCT_7_SHIFT);
        final String inst = switch (funct7) {
            case 0x00 -> switch (funct3) {
                case 0x0 -> "add";
                case 0x4 -> "xor";
                case 0x6 -> "or";
                case 0x7 -> "and";
                case 0x1 -> "sll";
                case 0x5 -> "srl";
                case 0x2 -> "slt";
                case 0x3 -> "sltu";
                default -> throw new UnknownCommandError();
            };
            case 0x20 -> switch (funct3) {
                case 0x0 -> "sub";
                case 0x5 -> "sra";
                default -> throw new UnknownCommandError();
            };
            case 0x01 -> switch (funct3) {
                case 0x0 -> "mul";
                case 0x1 -> "mulh";
                case 0x2 -> "mulsu";
                case 0x3 -> "mulu";
                case 0x4 -> "div";
                case 0x5 -> "divu";
                case 0x6 -> "rem";
                case 0x7 -> "remu";
                default -> throw new UnknownCommandError();
            };
            default -> throw new UnknownCommandError();
        };
        final String dest = getRegisterName(rd);
        final String first = getRegisterName(rs1);
        final String second = getRegisterName(rs2);
        return String.format("%s %s, %s, %s", inst, dest, first, second);
    }

    public static String getSystemInstructionRepresentation(int instruction) throws  UnknownCommandError {
        byte rd = (byte) ((instruction & RD_MASK) >>> RD_SHIFT);
        byte funct3 = (byte) ((instruction & FUNCT_3_MASK) >>> FUNCT_3_SHIFT);
        byte rs1 = (byte) ((instruction & RS_1_MASK) >>> RS_1_SHIFT);
        short imm12 = (short) ((instruction & (RS_2_MASK | FUNCT_7_MASK)) >>> RS_2_SHIFT);
        final String inst;
        switch (funct3) {
            case 0b000 -> {
                if (imm12 == 0x0) {
                    inst = "ecall";
                } else if (imm12 == 0x1) {
                    inst = "ebreak";
                } else {
                    throw new UnknownCommandError();
                }
                return inst;
            }
            case 0b001 -> inst = "csrrw";
            case 0b010 -> inst = "csrrs";
            case 0b011 -> inst = "csrrc";
            case 0b100 -> inst = "csrrwi";
            case 0b101 -> inst = "csrrsi";
            case 0b111 -> inst = "csrrci";
            default -> throw new AssertionError("Wrong funct3");
        }
        if (funct3 < 0b100) {
            return String.format("%s %s, %s, %s",
                    inst, getRegisterName(rd), getCSRName(imm12), getRegisterName(rs1));
        } else {
            // imm is in rs1
            return String.format("%s %s, %s, %d",
                    inst, getRegisterName(rd), getCSRName(imm12), rs1);
        }
    }

    public static String getIInstructionRepresentation(int instruction) throws UnknownCommandError {
        byte opcode = (byte) (instruction & OPCODE_MASK);
        byte rd = (byte) ((instruction & RD_MASK) >>> RD_SHIFT);
        byte funct3 = (byte) ((instruction & FUNCT_3_MASK) >>> FUNCT_3_SHIFT);
        byte rs1 = (byte) ((instruction & RS_1_MASK) >>> RS_1_SHIFT);
        short imm12 = (short) ((instruction & (RS_2_MASK | FUNCT_7_MASK)) >>> RS_2_SHIFT);
        final String inst;
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
                            throw new UnknownCommandError();
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
                            throw new UnknownCommandError();
                        }
                        break;
                    case 0x6:
                        inst = "ori";
                        break;
                    case 0x7:
                        inst = "andi";
                        break;
                    default:
                        throw new UnknownCommandError();
                }
                break;
            case 0b0000011:
                inst = switch (funct3) {
                    case 0x0 -> "lb";
                    case 0x1 -> "lh";
                    case 0x2 -> "lw";
                    case 0x4 -> "lbu";
                    case 0x5 -> "lhu";
                    default -> throw new UnknownCommandError();
                };
                break;
            case 0b1100111:
                if (funct3 == 0x0) {
                    inst = "jalr";
                } else {
                    throw new UnknownCommandError();
                }
                break;
            default:
                throw new UnknownCommandError();
        }
        int imm = getSignExtension(imm12, 12);
        return switch (inst) {
            case "jalr" -> String.format("jalr %s, %d(%s)",
                    getRegisterName(rd), imm, getRegisterName(rs1));

            case "lb", "lh", "lw", "lbu", "lhu" -> String.format("%s %s, %d(%s)",
                    inst, getRegisterName(rd), imm, getRegisterName(rs1));

            case "slli", "srli", "srai" -> String.format("%s %s, %s, %d",
                    inst, getRegisterName(rd), getRegisterName(rs1), imm12 & 0b11111);

            default -> String.format("%s %s, %s, %d",
                    inst, getRegisterName(rd), getRegisterName(rs1), imm);
        };
    }

    public static String getSInstructionRepresentation(int instruction) throws UnknownCommandError {
        // imm5 is in the rd place
        byte imm5 = (byte) ((instruction & RD_MASK) >>> RD_SHIFT);
        byte funct3 = (byte) ((instruction & FUNCT_3_MASK) >>> FUNCT_3_SHIFT);
        byte rs1 = (byte) ((instruction & RS_1_MASK) >>> RS_1_SHIFT);
        byte rs2 = (byte) ((instruction & RS_2_MASK) >>> RS_2_SHIFT);
        // imm7 is in the funct7 place
        byte imm7 = (byte) ((instruction & FUNCT_7_MASK) >>> FUNCT_7_SHIFT);
        final String inst = switch (funct3) {
            case 0x0 -> "sb";
            case 0x1 -> "sh";
            case 0x2 -> "sw";
            default -> throw new UnknownCommandError();
        };
        final String source = getRegisterName(rs2);
        final String base = getRegisterName(rs1);
        final String offset = Integer.toString(getSignExtension(imm5 + (imm7 << 5), 12));
        return String.format("%s %s, %s(%s)", inst, source, offset, base);
    }

    public static String getBInstructionRepresentation(int instruction) throws UnknownCommandError {
        byte funct3 = (byte) ((instruction & FUNCT_3_MASK) >>> FUNCT_3_SHIFT);
        byte rs1 = (byte) ((instruction & RS_1_MASK) >>> RS_1_SHIFT);
        byte rs2 = (byte) ((instruction & RS_2_MASK) >>> RS_2_SHIFT);
        final String inst = switch (funct3) {
            case 0x0 -> "beq";
            case 0x1 -> "bne";
            case 0x4 -> "blt";
            case 0x5 -> "bge";
            case 0x6 -> "bltu";
            case 0x7 -> "bgeu";
            default -> throw new UnknownCommandError();
        };
        final String source1 = getRegisterName(rs1);
        final String source2 = getRegisterName(rs2);
        int label = getBFormatOffset(instruction);
        return String.format("%s %s, %s, %d", inst, source1, source2, label);
    }

    public static int getBFormatOffset(int instruction) {
        byte imm5 = (byte) ((instruction & RD_MASK) >>> RD_SHIFT);
        byte imm7 = (byte) ((instruction & FUNCT_7_MASK) >>> FUNCT_7_SHIFT);
        int label =
                ((imm5 & 0b11110))
                        + ((imm7 & 0b0111111) << 5)
                        + ((imm5 & 0b00001) << 11)
                        + ((imm7 & 0b1000000) << 6);
        return  getSignExtension(label, 13);
    }

    public static String getJInstructionRepresentation(int instruction) {
        byte rd = (byte) ((instruction & RD_MASK) >>> RD_SHIFT);
        final String inst = "jal";
        final String dest = getRegisterName(rd);
        int offset = getJFormatOffset(instruction);
        return String.format("%s %s, %d", inst, dest, offset);
    }

    public static int getJFormatOffset(int instruction) {
        int imm20 = (instruction & (FUNCT_3_MASK | RS_1_MASK | RS_2_MASK | FUNCT_7_MASK)) >>> FUNCT_3_SHIFT;
        int offset =
                  ((imm20 & 0b01111111111000000000) >>> 8)
                + ((imm20 & 0b00000000000100000000) << 3)
                + ((imm20 & 0b00000000000011111111) << 12)
                + ((imm20 & 0b10000000000000000000) << 1);
        return BytesOperations.getSignExtension(offset, 21);
    }

    public static String getUInstructionRepresentation(int instruction) throws UnknownCommandError {
        byte opcode = (byte) (instruction & OPCODE_MASK);
        byte rd = (byte) ((instruction & RD_MASK) >>> RD_SHIFT);
        int imm20 = (instruction & (FUNCT_3_MASK | RS_1_MASK | RS_2_MASK | FUNCT_7_MASK)) >>> FUNCT_3_SHIFT;
        final String inst = switch (opcode) {
            case 0b0110111 -> "lui";
            case 0b0010111 -> "auipc";
            default -> throw new UnknownCommandError();
        };
        final String dest = getRegisterName(rd);
        return String.format("%s %s, %d", inst, dest, getSignExtension(imm20, 20));
    }
}
