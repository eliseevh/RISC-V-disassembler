package disasm.ELF;

import static disasm.util.BytesOperations.*;

public class ELFHeader {
    private final static int MAGIC_CONST_LITTLE_ENDIAN = 0x46_4c_45_7f;
    private final static byte CLASS_32_BIT = 1;
    private final static byte DATA_LITTLE_ENDIAN = 1;
    private final static short MACHINE_RISC_V = 0xf3;

    private final int e_shoff;
    private final short e_shnum;
    private final short e_shstrndx;

    public ELFHeader(byte[] header) {
        int ei_mag = getIntLittleEndian(header, 0);
        if (ei_mag != MAGIC_CONST_LITTLE_ENDIAN) {
            throw new AssertionError("Not an elf header");
        }
        byte ei_class = header[4];
        if (ei_class != CLASS_32_BIT) {
            throw new AssertionError("Not a 32-bit ELF");
        }
        byte ei_data = header[5];
        if (ei_data != DATA_LITTLE_ENDIAN) {
            throw  new AssertionError("Not a little endian encoded ELF");
        }
        short e_machine = getShortLittleEndian(header, 0x12);
        if (e_machine != MACHINE_RISC_V) {
            throw new AssertionError("Not a RISC-V ELF");
        }
        e_shoff = getIntLittleEndian(header, 0x20);
        short e_shentsize = getShortLittleEndian(header, 0x2e);
        if (e_shentsize != 0x28) {
            throw new AssertionError("Wrong section header table entry size");
        }
        e_shnum = getShortLittleEndian(header, 0x30);
        e_shstrndx = getShortLittleEndian(header, 0x32);
    }

    public int getSectionHeaderOffset() {
        return e_shoff;
    }

    public short getSectionHeaderEntriesNum() {
        return e_shnum;
    }

    public short getSectionStringTableIndex() {
        return e_shstrndx;
    }
}
