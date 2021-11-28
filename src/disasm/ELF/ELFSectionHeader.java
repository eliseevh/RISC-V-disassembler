package disasm.ELF;

import static disasm.util.ByteFileReader.getNumLittleEndian;

public class ELFSectionHeader {
    private final int sh_name;
    private final int sh_type;
    private final int sh_flags;
    private final int sh_addr;
    private final int sh_offset;
    private final int sh_size;
    private final int sh_link;
    private final int sh_info;
    private final int sh_addralign;
    private final int sh_entsize;

    public ELFSectionHeader(byte[] header, int shoff) {
        sh_name = getNumLittleEndian(header, shoff, 4);
        sh_type = getNumLittleEndian(header, shoff + 4, 4);
        sh_flags = getNumLittleEndian(header, shoff + 8, 4);
        sh_addr = getNumLittleEndian(header, shoff + 0xc, 4);
        sh_offset = getNumLittleEndian(header, shoff + 0x10, 4);
        sh_size = getNumLittleEndian(header, shoff + 0x14, 4);
        sh_link = getNumLittleEndian(header, shoff + 0x18, 4);
        sh_info = getNumLittleEndian(header, shoff + 0x1c, 4);
        sh_addralign = getNumLittleEndian(header, shoff + 0x20, 4);
        sh_entsize = getNumLittleEndian(header, shoff + 0x24, 4);
    }
}
