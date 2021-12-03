package disasm.util;

import static disasm.util.BytesOperations.getSignExtension;
import static disasm.util.InstructionDecoding.getRegisterName;

public final class CompressedInstructionDecoding {
    private CompressedInstructionDecoding() {}

    public static String getCompressedRegisterName(int register) {
        if (register < 2) {
            return "s" + register;
        }
        return "a" + (register - 2);
    }


    // TODO: replace "throw new AssertionError(...)" with getting unknown_command
    public static String getCompressedInstructionRepresentation(short command) {
        byte b2to4 =   (byte) ((command & 0b0000000000011100) >>> 2);
        byte b5to6 =   (byte) ((command & 0b0000000001100000) >>> 5);
        byte b7to9 =   (byte) ((command & 0b0000001110000000) >>> 7);
        byte b10to12 = (byte) ((command & 0b0001110000000000) >>> 10);
        byte b13to15 = (byte) ((command & 0b1110000000000000) >>> 13);
        byte b5to12 = (byte) (b5to6 | (b7to9 << 2) | (b10to12 << 5));
        byte b2to6 = (byte) (b2to4 | (b5to6 << 3));
        byte b7to11 = (byte) (b7to9 | ((b10to12 & 0b011) << 3));
        byte b12 = (byte) ((b10to12 & 0b100) >>> 2);
        byte b7to12 = (byte) (b7to11 | (b12 << 5));
        short b2to12 = (short) (b2to6 | (b7to11 << 5) | (b12 << 10));
        byte b10to11 = (byte) (b10to12 & 0b11);
        byte opcode = (byte) (command & 0b11);

        int jimm = getSignExtension(
                ((b2to12 & 0b10000000000) << 1) +//11
                ((b2to12 & 0b01000000000) >>> 5) +   //4
                ((b2to12 & 0b00110000000) << 1) +    //9:8
                ((b2to12 & 0b00001000000) << 4) +    //10
                ((b2to12 & 0b00000100000) << 1) +    //6
                ((b2to12 & 0b00000010000) << 3) +    //7
                (b2to12 & 0b00000001110) +           //3:1
                ((b2to12 & 0b00000000001) << 5),     //5
        12);
        switch (opcode) {
            case 0b00:
                switch (b13to15) {
                    case 0b000 -> {
                        int nzuimm = ((b5to12 & 0b11000000) >>> 2) +
                                        ((b5to12 & 0b00111100) << 4) +
                                        ((b5to12 & 0b00000010) << 1) +
                                        ((b5to12 & 0b00000001) << 3);
                        // rd is 2-4
                        return String.format("addi4spn %s, sp, %d", getCompressedRegisterName(b2to4), nzuimm);
                    }
                    case 0b010, 0b110 -> {
                        int uimm = ((b5to12 & 0b11100000) >>> 2) +
                                    ((b5to12 & 0b00000010) << 1) +
                                    ((b5to12 & 0b00000001) << 6);
                        final String inst;
                        if (b13to15 == 0b010) {
                            inst = "c.lw";
                        } else {
                            inst = "c.sw";
                        }
                        // rd is 2-4, rs is 7-9
                        return String.format("%s %s, %d(%s)",
                                inst, getCompressedRegisterName(b2to4), uimm, getCompressedRegisterName(b7to9));
                    }
                    default -> throw new AssertionError("Unexpected funct3 (" + b13to15 + ")");
                }
            case 0b01:
                switch (b13to15) {
                    case 0b000 -> {
                        if (b7to11 == 0) {
                            return "c.nop";
                        } else {
                            int nzimm = (b2to6) + (b12 << 5);
                            // rd/rs is 7-11
                            return String.format("c.addi %s, %d",
                                    getRegisterName(b7to11), getSignExtension(nzimm, 6));
                        }
                    }
                    case 0b001 -> {
                        return String.format("c.jal %d", jimm);
                    }
                    case 0b010 -> {
                        int imm = (b2to6) + (b12 << 5);
                        // rd is 7-11
                        return String.format("c.li %s, %d",
                                getRegisterName(b7to11), getSignExtension(imm, 6));
                    }
                    case 0b011 -> {
                        if (b7to11 == 2) {
                            int nzimm =  (b12 << 9) +           //9
                                    ((b2to6 & 0b10000)) +       //4
                                    ((b2to6 & 0b01000) << 3) +  //6
                                    ((b2to6 & 0b00110) << 6) +  //8:7
                                    ((b2to6 & 0b00001) << 5);   //5
                            return String.format("c.addi16sp sp, %d", getSignExtension(nzimm, 10));
                        } else {
                            int nzimm = (b12 << 17) +           //17
                                    ((b2to6) << 10);            //16:12
                            // rd is 7-11
                            return String.format("c.lui %s, %d",
                                    getRegisterName(b7to11), getSignExtension(nzimm, 18));
                        }
                    }
                    case 0b100 -> {
                        switch (b10to11) {
                            case 0b00 -> {
                                int shamt = (b12 << 5) + b2to6;
                                // rd/rs is 7-9
                                return String.format("c.srli %s, %d",
                                        getCompressedRegisterName(b7to9), shamt);
                            }
                            case 0b01 -> {
                                int shamt = (b12 << 5) + b2to6;
                                // rd/rs is 7-9
                                return String.format("c.srai %s, %d",
                                        getCompressedRegisterName(b7to9), shamt);
                            }
                            case 0b10 -> {
                                int imm = (b12 << 5) + b2to6;
                                // rd/rs is 7-9
                                return String.format("c.andi %s, %d",
                                        getCompressedRegisterName(b7to9), getSignExtension(imm, 6));
                            }
                            case 0b11 -> {
                                String inst = switch (b5to6) {
                                    case 0b00 -> "c.sub";
                                    case 0b01 -> "c.xor";
                                    case 0b10 -> "c.or";
                                    case 0b11 -> "c.and";
                                    default -> throw new AssertionError("Wrong b5to6");
                                };
                                // rd/rs1 is 7-9, rs2 is 2-4
                                return String.format("%s %s, %s",
                                        inst, getCompressedRegisterName(b7to9), getCompressedRegisterName(b2to4));
                            }
                            default -> throw new AssertionError("Wrong b10to11");
                        }
                    }
                    case 0b101 -> {
                        return String.format("c.j %d", jimm);
                    }
                    case 0b110, 0b111 -> {
                        int imm = ((b10to12 & 0b100) << 6) +   //8
                                ((b10to12 & 0b011) << 3) +     //4:3
                                ((b2to6 & 0b11000) << 3) +     //7:6
                                ((b2to6 & 0b00110)) +          //2:1
                                ((b2to6 & 0b00001) << 5);      //5
                        String inst;
                        if (b13to15 == 0b110) {
                            inst = "c.beqz";
                        } else {
                            inst = "c.bnez";
                        }
                        // rs is 7-9
                        return String.format("%s %s, %d",
                                inst, getCompressedRegisterName(b7to9), getSignExtension(imm, 9));
                    }
                    default -> throw new AssertionError("Unexpected funct3 (" + b13to15 + ")");
                }
            case 0b10:
                switch (b13to15) {
                    case 0b000 -> {
                        int shamt = (b12 << 5) + b2to6;
                        // rd/rs is 7-11
                        return String.format("c.slli %s, %d",
                                getRegisterName(b7to11), shamt);
                    }
                    case 0b010 -> {
                        int uimm = (b12 << 5) +           //5
                                ((b2to6 & 0b11100)) +     //4:2
                                ((b2to6 & 0b00011) << 6); //7:6
                        // rd is 7-11
                        return String.format("c.lwsp %s, %d", getRegisterName(b7to11), uimm);
                    }
                    case 0b100 -> {
                        if (b12 == 0) {
                            if (b2to6 == 0) {
                                // rs is 7-11
                                return String.format("c.jr %s", getRegisterName(b7to11));
                            } else {
                                // rd is 7-11, rs is 2-6
                                return String.format("c.mv %s, %s", getRegisterName(b7to11), getRegisterName(b2to6));
                            }
                        } else {
                            if (b7to11 == 0) {
                                if (b2to6 == 0) {
                                    return "c.ebreak";
                                } else {
                                    // rs2 is 2-6
                                    return String.format("c.add zero, %s", getRegisterName(b2to6));
                                }
                            } else {
                                if (b2to6 == 0) {
                                    // rs is 7-11
                                    return String.format("c.jalr %s", getRegisterName(b7to11));
                                } else {
                                    // rd/rs1 is 7-11, rs2 is 2-6
                                    return String.format("c.add %s, %s",
                                            getRegisterName(b7to11), getRegisterName(b2to6));
                                }
                            }
                        }
                    }
                    case 0b110 -> {
                        int uimm = (b7to12 & 0b111100) +     //5:2
                                ((b7to12 & 0b000011) << 6);  //7:6
                        // rs is 2-6
                        return String.format("c.swsp %s, %d", getRegisterName(b2to6), uimm);
                    }
                    default -> throw new AssertionError("Unexpected funct3 (" + b13to15 + ")");
                }
            default:
                throw new AssertionError("Wrong opcode");
        }
    }
}
