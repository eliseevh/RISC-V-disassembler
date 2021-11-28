package ELF;

public class ELFHeader {
    private final static int MAGIC_CONST = 0x7f454c46;
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

    private static int getNumLittleEndian(byte[] bytes, int offset, int numBytes) {
        int num = 0;
        for (int i = offset; i < offset + numBytes; i++) {
            num <<= 8;
            num |= bytes[i];
        }
        return num;
    }

    public ELFHeader(byte[] header) {
        ei_mag = getNumLittleEndian(header, 0, 4);
        if (ei_mag != MAGIC_CONST) {
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
        e_type = (short) getNumLittleEndian(header, 0x10, 2);
        e_machine = (short) getNumLittleEndian(header, 0x12, 2);
        if (e_machine != MACHINE_RISC_V) {
            throw new AssertionError("Not a RISC-V ELF");
        }
        e_version = getNumLittleEndian(header, 0x14, 4);
        e_entry = getNumLittleEndian(header, 0x18, 4);
        e_phoff = getNumLittleEndian(header, 0x1c, 4);
        e_shoff = getNumLittleEndian(header, 0x20, 4);
        e_flags = getNumLittleEndian(header, 0x24, 4);
        e_ehsize = (short) getNumLittleEndian(header, 0x28, 2);
        e_phentsize = (short) getNumLittleEndian(header, 0x2a, 2);
        e_phnum = (short) getNumLittleEndian(header, 0x2c, 2);
        e_shentsize = (short) getNumLittleEndian(header, 0x2e, 2);
        e_shnum = (short) getNumLittleEndian(header, 0x30, 2);
        e_shstrndx = (short) getNumLittleEndian(header, 0x32, 2);
    }
}
