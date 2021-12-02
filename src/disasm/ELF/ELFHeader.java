package disasm.ELF;

import static disasm.util.BytesOperations.*;

public class ELFHeader {
    private final static int MAGIC_CONST_LITTLE_ENDIAN = 0x46_4c_45_7f;
    private final static byte CLASS_32_BIT = 1;
    private final static byte DATA_LITTLE_ENDIAN = 1;
    private final static short MACHINE_RISC_V = 0xf3;

    private final int ei_mag;
    private final byte ei_class;
    private final byte ei_data;
    private final byte ei_version;
    private final byte ei_osabi;
    private final byte ei_abiversion;
    private final byte[] ei_pad;
    private final short e_type;
    private final short e_machine;
    private final int e_version;
    private final int e_entry;
    private final int e_phoff;
    private final int e_shoff;
    private final int e_flags;
    private final short e_ehsize;
    private final short e_phentsize;
    private final short e_phnum;
    private final short e_shentsize;
    private final short e_shnum;
    private final short e_shstrndx;

    public ELFHeader(byte[] header) {
        ei_mag = getIntLittleEndian(header, 0);
        if (ei_mag != MAGIC_CONST_LITTLE_ENDIAN) {
            throw new AssertionError("Not an elf header");
        }
        ei_class = header[4];
        if (ei_class != CLASS_32_BIT) {
            throw new AssertionError("Not a 32-bit ELF");
        }
        ei_data = header[5];
        if (ei_data != DATA_LITTLE_ENDIAN) {
            throw  new AssertionError("Not a little endian encoded ELF");
        }
        ei_version = header[6];
        ei_osabi = header[7];
        ei_abiversion = header[8];
        ei_pad = new byte[7];
        System.arraycopy(header, 9, ei_pad, 0, 7);
        e_type = getShortLittleEndian(header, 0x10);
        e_machine = getShortLittleEndian(header, 0x12);
        if (e_machine != MACHINE_RISC_V) {
            throw new AssertionError("Not a RISC-V ELF");
        }
        e_version = getIntLittleEndian(header, 0x14);
        e_entry = getIntLittleEndian(header, 0x18);
        e_phoff = getIntLittleEndian(header, 0x1c);
        e_shoff = getIntLittleEndian(header, 0x20);
        e_flags = getIntLittleEndian(header, 0x24);
        e_ehsize = getShortLittleEndian(header, 0x28);
        e_phentsize = getShortLittleEndian(header, 0x2a);
        e_phnum = getShortLittleEndian(header, 0x2c);
        e_shentsize = getShortLittleEndian(header, 0x2e);
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

    public int getEntryPoint() {
        return e_entry;
    }
}
