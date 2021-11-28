package disasm.ELF;

public class ELFFile {
    private final ELFHeader header;
    private final ELFSectionHeader[] sectionHeaders;

    private final static int SHEntrySize = 0x28;


    public ELFFile(byte[] file) {
        header = new ELFHeader(file);
        int shoff = header.getShoff();
        int shnum = header.getShnum();
        sectionHeaders = new ELFSectionHeader[shnum];
        for (int i = 0; i < shnum; i++) {
            sectionHeaders[i] = new ELFSectionHeader(file, shoff + i * SHEntrySize);
        }
    }
}
