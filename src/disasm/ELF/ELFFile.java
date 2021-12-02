package disasm.ELF;

import static disasm.util.BytesOperations.*;

public class ELFFile {
    private final ELFHeader header;
    private final ELFSectionHeader[] sectionHeaders;
    private final ELFSectionHeader stringTableSectionHeader;
    private final ELFSectionHeader stringTableHeader;
    private final String[] sectionNames;

    private final byte[] file;

    private final static int SECTION_HEADER_ENTRY_SIZE = 0x28;


    public ELFFile(byte[] file) {
        this.file = file;
        header = new ELFHeader(file);
        int shoff = header.getSectionHeaderOffset();
        int shnum = header.getSectionHeaderEntriesNum();
        sectionHeaders = new ELFSectionHeader[shnum];
        sectionNames = new String[shnum];
        stringTableSectionHeader = new ELFSectionHeader(file,
                shoff + header.getSectionStringTableIndex() * SECTION_HEADER_ENTRY_SIZE);
        int sectionHeaderNamesOffset = stringTableSectionHeader.getOffset();
        for (int i = 0; i < shnum; i++) {
            sectionHeaders[i] = new ELFSectionHeader(file, shoff + i * SECTION_HEADER_ENTRY_SIZE);
            sectionNames[i] = getNullTerminatedString(file,
                    sectionHeaderNamesOffset + sectionHeaders[i].getNameOffset());
        }
        stringTableHeader = getSectionHeader(".strtab");
    }

    public ELFSectionHeader getSectionHeader(String name) {
        for (int i = 0; i < sectionNames.length; i++) {
            if (sectionNames[i].equals(name)) {
                return sectionHeaders[i];
            }
        }
        throw new AssertionError("There is no section with name \"" + name + "\"");
    }

    public ELFHeader getHeader() {
        return header;
    }

    public byte[] getFile() {
        return file;
    }

    public String getString(int offset) {
        return getNullTerminatedString(file, offset + stringTableHeader.getOffset());
    }
}
