package disasm.ELF;

import static disasm.util.BytesOperations.*;

public class ELFSectionHeader {
    private final int sh_name;
    private final int sh_addr;
    private final int sh_offset;
    private final int sh_size;

    public ELFSectionHeader(byte[] header, int shoff) {
        sh_name = getIntLittleEndian(header, shoff);
        sh_addr = getIntLittleEndian(header, shoff + 0xc);
        sh_offset = getIntLittleEndian(header, shoff + 0x10);
        sh_size = getIntLittleEndian(header, shoff + 0x14);
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

    public int getAddress() {
        return sh_addr;
    }
}
