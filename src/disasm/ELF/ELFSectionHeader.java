package disasm.ELF;

import static disasm.util.BytesOperations.*;

public class ELFSectionHeader {
    private final static int ENTRY_SIZE = 0x28;
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
        sh_name = getIntLittleEndian(header, shoff);
        sh_type = getIntLittleEndian(header, shoff + 4);
        sh_flags = getIntLittleEndian(header, shoff + 8);
        sh_addr = getIntLittleEndian(header, shoff + 0xc);
        sh_offset = getIntLittleEndian(header, shoff + 0x10);
        sh_size = getIntLittleEndian(header, shoff + 0x14);
        sh_link = getIntLittleEndian(header, shoff + 0x18);
        sh_info = getIntLittleEndian(header, shoff + 0x1c);
        sh_addralign = getIntLittleEndian(header, shoff + 0x20);
        sh_entsize = getIntLittleEndian(header, shoff + 0x24);
    }

    public int getOffset() {
        return sh_offset;
    }

    public int getNameOffset() {
        return sh_name;
    }

    public int getSize() {
        return sh_size;
    }
}
